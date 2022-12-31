package request

type MomentLikeReq struct {
	MomentId   int  `json:"moment_id"`
	LikeStatus bool `json:"like_status"`
}
