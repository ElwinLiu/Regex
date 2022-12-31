package request

type UserRegisterReq struct {
	Account  string `json:"account"`
	Password string `json:"password"`
	NickName string `json:"nickname"`
	Tags     []int  `json:"tags"`
}
