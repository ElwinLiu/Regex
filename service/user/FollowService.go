package user

import (
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"strconv"
	"terminal/models"
	"terminal/util"
)

func ProcessFollow(c *gin.Context) error {
	id, err := strconv.Atoi(c.Query("id"))
	if err != nil {
		return err
	}

	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return err
	}

	if claim.ID == id {
		return errors.New("could not follow yourself")
	}

	// query whether the follow relationship already exists
	follow := new(models.Follow)
	if err := models.DB.Where("follower_id = ? and subscribed_id = ?", claim.ID, id).First(&follow).Error; err != nil {
		if err != gorm.ErrRecordNotFound {
			return nil
		} else {
			// If it does not exist , insert record into the database
			follow.FollowerID = claim.ID
			follow.SubscribedID = id
			if err := models.DB.Create(&follow).Error; err != nil {
				return err
			}
			return nil
		}
	}
	// if so, delete the relationship
	if err := models.DB.Delete(follow).Error; err != nil {
		return err
	}

	return nil
}
