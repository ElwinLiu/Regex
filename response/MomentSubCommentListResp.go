package response

import "time"

type MomentSubCommentListResp struct {
	UserId       int       `json:"user_id"`
	CommentId    int       `json:"comment_id"`
	Nickname     string    `json:"nickname"`
	Avatar       string    `json:"avatar"`
	TextContent  string    `json:"text_content"`
	Image        string    `json:"image"`
	CreatedTime  time.Time `json:"created_time"`
	ReceiverId   int       `json:"receiver_id"`
	ReceiverName string    `json:"receiver_name"`
}
