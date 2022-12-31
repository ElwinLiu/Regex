package models

import "time"

type Moment struct {
	ID          int
	SenderID    int
	TextContent string
	Image       string
	ViewNum     int
	LikeNum     int
	CommentNum  int
	CreatedTime time.Time `gorm:"default:CURRENT_TIMESTAMP"`
}
