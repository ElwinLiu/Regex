package request

type UserLoginReq struct {
	Account  string `json:"account"`
	Password string `json:"password"`
}
