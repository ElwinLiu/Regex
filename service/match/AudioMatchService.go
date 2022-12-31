package match

import (
	rtctokenbuilder "github.com/AgoraIO/Tools/DynamicKey/AgoraDynamicKey/go/src/RtcTokenBuilder"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"terminal/models"
	"terminal/request"
	"terminal/response"
	"terminal/util"
	"time"
)

func ProcessAudioMatch(c *gin.Context) (*response.AudioMatchResp, error) {

	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return nil, err
	}

	// bind data
	audioMatchReq := new(request.AudioMatchReq)
	if err := c.ShouldBind(audioMatchReq); err != nil {
		return nil, err
	}

	resp := new(response.AudioMatchResp)
	call := new(models.Call)

	// query whether exist unexpired audio match request
	if err := models.DB.Where("matched_id = 0 and expired_time < ?",
		time.Now().Add(30*time.Second)).First(&call).Error; err != nil {
		// system error
		if err != gorm.ErrRecordNotFound {
			return nil, err
		} else {
			// there is no unexpired and unmatched audio match request in the database
			// insert it as the first matcher and return token

			// generate agora application token
			token, err := util.GenerateRtcToken(audioMatchReq.Uid, audioMatchReq.ChannelName, rtctokenbuilder.RolePublisher)
			if err != nil {
				return nil, err
			}

			// insert the information into call table
			callID, err := callInsert(claim.ID, audioMatchReq.ChannelName, time.Now().Add(30*time.Second))
			if err != nil {
				return nil, err
			}

			// return token and match id
			resp.Token = token
			resp.CallID = callID
			resp.ChannelName = audioMatchReq.ChannelName
			resp.IsFirst = true
		}
	} else {

		// using the channel name in the database to generate token
		channelName := call.ChannelName
		// generate agora application token
		token, err := util.GenerateRtcToken(audioMatchReq.Uid, channelName, rtctokenbuilder.RoleSubscriber)
		if err != nil {
			return nil, err
		}

		// update the matched_id column in the calls table
		if err := models.DB.Model(&models.Call{ID: call.ID}).Select("matched_id").Updates(
			map[string]interface{}{"matched_id": claim.ID}).Error; err != nil {
			return nil, err
		}

		// query the first user's information
		user := new(models.User)
		if err := models.DB.Where("id = ?", call.UserID).First(&user).Error; err != nil {
			return nil, err
		}

		isFollowed, err := util.QueryFollowStatus(claim.ID, call.MatchedID)
		if err != nil {
			return nil, err
		}
		tags, matchRate, err := util.GetMatchRate(claim.ID, call.MatchedID)
		if err != nil {
			return nil, err
		}

		resp.UserID = user.ID
		resp.Nickname = user.Nickname
		resp.Avatar = user.Avatar
		resp.Tag = tags
		resp.MatchRate = matchRate
		resp.Token = token
		resp.CallID = call.ID
		resp.IsFirst = false
		resp.IsFollowed = isFollowed
		resp.ChannelName = call.ChannelName
	}
	return resp, nil
}

func callInsert(id int, channelName string, expiredTime time.Time) (int, error) {
	call := new(models.Call)
	call.UserID = id
	call.ChannelName = channelName
	call.ExpiredTime = expiredTime
	if err := models.DB.Create(&call).Error; err != nil {
		return 0, err
	}
	return call.ID, nil
}
