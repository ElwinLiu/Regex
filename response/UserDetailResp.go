package response

import (
	"terminal/models"
	"time"
)

type UserDetailResp struct {
	ID            int          `json:"id"`
	Avatar        string       `json:"avatar"`
	Nickname      string       `json:"nickname"`
	Tags          []models.Tag `json:"tags"`
	FollowerNum   int          `json:"follower_num"`
	SubscribedNum int          `json:"subscribed_num"`
	IsFollowed    bool         `json:"is_followed"`
	MatchRate     int          `json:"match_rate"`
	CreatedTime   time.Time    `json:"created_time"`
}
