package response

import "time"

type UserFollowSubscribedListResp struct {
	ID              int       `json:"id"`
	Nickname        string    `json:"nickname"`
	Avatar          string    `json:"avatar"`
	MomentNum       int       `json:"momentNum"`
	SubscribeStatus bool      `json:"subscribe_status"`
	CreatedTime     time.Time `json:"created_time"`
}
