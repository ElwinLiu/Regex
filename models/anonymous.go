package models

import "time"

type Anonymous struct {
	ID          int
	UserId      int
	ExpiredTime time.Time
}
