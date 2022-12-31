package request

type MomentCommentReq struct {
	MomentId    int    `json:"moment_id"`
	ReceiverId  int    `json:"receiver_id"`
	BelongingId int    `json:"belonging_id"`
	TextContent string `json:"text_content"`
}
