package user

import (
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"strconv"
	"terminal/define"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func ProcessSubscribedList(c *gin.Context) ([]response.UserFollowSubscribedListResp, error) {

	page, err := strconv.Atoi(c.Query("page"))
	if err != nil {
		return nil, err
	}

	id, err := strconv.Atoi(c.Query("id"))
	if err != nil {
		return nil, err
	}

	offset := (page - 1) * define.FollowerListPageSize

	// query basic information
	var resp []response.UserFollowSubscribedListResp
	if err := models.DB.Raw("select users.id, users.nickname, users.avatar, users.created_time "+
		"from follows left join users on follows.follower_id = users.id "+
		"where follows.subscribed_id = ? "+
		"order by follows.created_time desc "+
		"LIMIT ? OFFSET ? ", id, define.FollowerListPageSize, offset).Scan(&resp).Error; err != nil && err != gorm.ErrRecordNotFound {
		return nil, err
	}

	// query comment num
	for i := 0; i < len(resp); i++ {
		momentResult := models.DB.Where("sender_id = ?", resp[i].ID).Find(&[]models.Moment{})
		if momentResult.Error != nil && momentResult.Error != gorm.ErrRecordNotFound {
			return nil, momentResult.Error
		}
		resp[i].MomentNum = int(momentResult.RowsAffected)
	}

	// query subscribe status
	claim, err := util.JwtAuthentication(c)
	if err != nil && err.Error() == "invalid Authorization" {
		return nil, err
	}

	if claim != nil {
		for i := 0; i < len(resp); i++ {
			if err = models.DB.Where("follower_id = ? and subscribed_id = ?", claim.ID, resp[i].ID).First(
				&models.Follow{}).Error; err != nil {
				if err != gorm.ErrRecordNotFound {
					return nil, err
				}
			} else {
				resp[i].SubscribeStatus = true
			}
		}
	}
	return resp, nil
}
