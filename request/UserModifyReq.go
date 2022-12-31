package request

type UserModifyReq struct {
	NickName string `json:"nickname"`
	Tags     []int  `json:"tags"`
}
