package models

import "time"

type User struct {
	ID          int
	Account     string
	Password    string
	Nickname    string
	Avatar      string
	CreatedTime time.Time `gorm:"default:CURRENT_TIMESTAMP"`
}
