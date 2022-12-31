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

func GetMomentList(c *gin.Context) ([]response.UserMomentListResp, error) {

	// process token
	claim, err := util.JwtAuthentication(c)
	if err != nil && err.Error() == "invalid Authorization" {
		return nil, err
	}

	id, err := strconv.Atoi(c.Query("id"))
	if err != nil {
		return nil, err
	}
	page, err := strconv.Atoi(c.Query("page"))
	if err != nil {
		return nil, err
	}
	offset := (page - 1) * define.MomentPageSize

	// query basic information
	var resp []response.UserMomentListResp
	if err := models.DB.Raw("select moments.id as moment_id , users.id as sender_id, users.avatar, users.nickname, "+
		"moments.text_content, moments.image, moments.like_num, moments.view_num, moments.comment_num, moments.created_time "+
		"from moments left join users on moments.sender_id = users.id where users.id = ? "+
		"ORDER BY moments.created_time desc LIMIT ? OFFSET ?;", id, define.MomentPageSize, offset).Scan(&resp).Error; err != nil {
		return nil, err
	}

	// query subscribe status
	if claim != nil && claim.ID != id {
		if err := models.DB.Where("follower_id = ? and subscribed_id = ?", claim.ID, id).First(&models.Follow{}).Error; err != nil {
			if err != gorm.ErrRecordNotFound {
				return nil, err
			}
		} else {
			for i := 0; i < len(resp); i++ {
				resp[i].IsFollowed = true
			}
		}
	}
	if claim != nil {
		for i := 0; i < len(resp); i++ {
			if err := models.DB.Where("user_id = ? and moment_id = ?", claim.ID, resp[i].MomentId).First(&models.Like{}).Error; err != nil {
				if err != gorm.ErrRecordNotFound {
					return nil, err
				}
			} else {
				resp[i].IsLiked = true
			}
		}
	}

	return resp, nil
}
