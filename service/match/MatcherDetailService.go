package match

import (
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func GetMatcherDetail(c *gin.Context) (*response.NormalMatchResp, error) {

	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return nil, err
	}

	id, _ := strconv.Atoi(c.Query("id"))

	// query the matcher's id
	call := new(models.Call)
	call.ID = id
	if err := models.DB.First(&call).Error; err != nil {
		return nil, err
	}

	// get detailed information
	user := new(models.User)
	user.ID = call.MatchedID
	if err := models.DB.First(&user).Error; err != nil {
		return nil, err
	}

	tags, matchRate, err := util.GetMatchRate(claim.ID, call.MatchedID)
	if err != nil {
		return nil, err
	}

	isFollowed, err := util.QueryFollowStatus(claim.ID, call.MatchedID)
	if err != nil {
		return nil, err
	}
	if err != nil {
		return nil, err
	}

	resp := response.NormalMatchResp{
		ID:         user.ID,
		Nickname:   user.Nickname,
		Avatar:     user.Avatar,
		MatchRate:  matchRate,
		Tag:        tags,
		IsFollowed: isFollowed,
	}
	return &resp, nil
}
