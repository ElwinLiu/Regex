package user

import (
	"terminal/models"
)

func GetTagList() ([]models.Tag, error) {
	var tags []models.Tag
	// Get all records
	if err := models.DB.Find(&tags).Error; err != nil {
		return nil, err
	} else {
		return tags, nil
	}
}
