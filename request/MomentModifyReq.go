package request

type MomentModifyReq struct {
	MomentId    int    `json:"moment_id"`
	TextContent string `json:"text_content"`
	Image       string `json:"image"`
}
