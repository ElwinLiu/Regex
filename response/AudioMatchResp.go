package response

import (
	"terminal/models"
)

type AudioMatchResp struct {
	CallID      int          `json:"call_id"`
	Token       string       `json:"token"`
	ChannelName string       `json:"channelName"`
	IsFirst     bool         `json:"is_first"`
	UserID      int          `json:"user_id"`
	Nickname    string       `json:"nickname"`
	Avatar      string       `json:"avatar"`
	MatchRate   int          `json:"matchRate"`
	Tag         []models.Tag `json:"tag"`
	IsFollowed  bool         `json:"is_followed"`
}
