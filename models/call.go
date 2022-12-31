package models

import "time"

type Call struct {
	ID          int
	UserID      int
	MatchedID   int `gorm:"default:0"`
	ChannelName string
	ExpiredTime time.Time
}
