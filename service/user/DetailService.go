package user

import (
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"strconv"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func ProcessDetail(c *gin.Context) (*response.UserDetailResp, error) {

	// process token
	claim, err := util.JwtAuthentication(c)
	if err != nil && err.Error() == "invalid Authorization" {
		return nil, err
	}

	idStr := c.Query("id")
	id, err := strconv.Atoi(idStr)
	if err != nil {
		return nil, err
	}
	user := new(models.User)
	// query users table
	user.ID = id
	if err := models.DB.First(&user).Error; err != nil {
		return nil, err
	}

	resp := new(response.UserDetailResp)

	// query follows table
	// if the id of request sender is different from the parameter id, get the subscribing status
	if claim != nil && claim.ID != id {
		//if err := models.DB.Where("follower_id = ? and subscribed_id = ?", claim.ID, id).Find(&models.Follow{}).Error; err != nil {
		//	if err == gorm.ErrRecordNotFound {
		//		resp.IsFollowed = false
		//	} else {
		//		return nil, err
		//	}
		//}
		isFollowed, err := util.QueryFollowStatus(claim.ID, id)
		if err != nil {
			return nil, err
		}
		resp.IsFollowed = isFollowed
	}

	followResult := models.DB.Where("follower_id = ?", id).Find(&[]models.Follow{})
	subscribedResult := models.DB.Where("subscribed_id = ?", id).Find(&[]models.Follow{})
	if followResult.Error != nil && followResult.Error != gorm.ErrRecordNotFound {
		return nil, followResult.Error
	}
	if subscribedResult.Error != nil && subscribedResult.Error != gorm.ErrRecordNotFound {
		return nil, subscribedResult.Error
	}

	var tags []models.Tag
	matchRate := 0
	if claim != nil && claim.ID != id {
		tags, matchRate, err = util.GetMatchRate(claim.ID, id)
	} else {
		tags, matchRate, err = util.GetMatchRate(0, id)
	}

	//// query user_tags table
	//var tags []models.Tag
	//if err := models.DB.Raw("select tags.id, tags.tag_content "+
	//	"from user_tags left join tags on user_tags.tag_id = tags.id "+
	//	"where user_tags.user_id = ?", id).Scan(&tags).Error; err != nil {
	//	return nil, err
	//}
	//
	//matchRate := 0
	//
	//// query match rate
	//if claim != nil && claim.ID != id {
	//	matchRate = 90
	//	var selfTags []models.Tag
	//	if err := models.DB.Raw("select tags.id, tags.tag_content "+
	//		"from user_tags left join tags on user_tags.tag_id = tags.id "+
	//		"where user_tags.user_id = ?", id).Scan(&selfTags).Error; err != nil {
	//		return nil, err
	//	}
	//
	//	hash := make(map[int]bool)
	//	for _, v := range tags {
	//		hash[v.ID] = true
	//	}
	//
	//	for _, v := range selfTags {
	//		if hash[v.ID] == true {
	//			matchRate += 3
	//		}
	//	}
	//
	//	if matchRate > 100 {
	//		matchRate = 100
	//	}
	//
	//}

	resp.ID = id
	resp.Avatar = user.Avatar
	resp.Nickname = user.Nickname
	resp.Tags = tags
	resp.FollowerNum = int(followResult.RowsAffected)
	resp.SubscribedNum = int(subscribedResult.RowsAffected)
	resp.MatchRate = matchRate
	resp.CreatedTime = user.CreatedTime
	return resp, nil
}
