package response

import "terminal/models"

type NormalMatchResp struct {
	ID         int          `json:"id"`
	Nickname   string       `json:"nickname"`
	Avatar     string       `json:"avatar"`
	MatchRate  int          `json:"matchRate"`
	Tag        []models.Tag `json:"tag"`
	IsFollowed bool         `json:"is_followed"`
}
