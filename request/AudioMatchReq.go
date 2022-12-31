package request

type AudioMatchReq struct {
	Uid         uint32 `json:"uid"`
	ChannelName string `json:"channel_name"`
}
