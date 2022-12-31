package models

import "time"

type History struct {
	ID          int
	SenderID    int
	ReceiverID  int
	MsgType     int
	MsgContent  string
	ReadStatus  bool
	CreatedTime time.Time `gorm:"default:CURRENT_TIMESTAMP"`
}
