package models

import "time"

type Comment struct {
	ID          int
	MomentID    int
	SenderID    int
	ReceiverID  int
	TextContent string
	Image       string
	BelongingID int
	CreatedTime time.Time `gorm:"default:CURRENT_TIMESTAMP"`
}
