package response

type UserLoginResp struct {
	ID    int    `json:"id"`
	Token string `json:"token"`
}
