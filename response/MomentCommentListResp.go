package response

import "time"

type MomentCommentListResp struct {
	UserId      int                        `json:"user_id"`
	CommentId   int                        `json:"comment_id"`
	Nickname    string                     `json:"nickname"`
	Avatar      string                     `json:"avatar"`
	TextContent string                     `json:"text_content"`
	Image       string                     `json:"image"`
	CreatedTime time.Time                  `json:"created_time"`
	SubComment  []MomentSubCommentListResp `json:"sub_comment"`
}
