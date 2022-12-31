package response

import "time"

type MomentSquareListResp struct {
	Avatar      string    `json:"avatar"`
	Nickname    string    `json:"nickname"`
	TextContent string    `json:"text_content"`
	Image       string    `json:"image"`
	LikeNum     int       `json:"like_num"`
	IsLiked     bool      `json:"is_liked"`
	CommentNum  int       `json:"comment_num"`
	ViewNum     int       `json:"view_num"`
	CreatedTime time.Time `json:"created_time"`
	MomentId    int       `json:"moment_id"`
	SenderId    int       `json:"sender_id"`
	IsFollowed  bool      `json:"is_follower"`
}
