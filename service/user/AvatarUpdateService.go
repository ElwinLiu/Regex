package user

import (
	"github.com/gin-gonic/gin"
	"terminal/models"
	"terminal/util"
)

func ProcessAvatarUpload(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	has, err, image := util.VerifyFileExistence(c)
	if err != nil {
		return "", err
	}
	avatarAddress := ""
	if has {
		// the file exists, return the path directly
		avatarAddress = image.Path
	} else {
		// the file doesn't exist
		// upload the file and store its hash and url path into the database
		uploadAddress, err := util.COSUpload(c)
		if err != nil {
			return "", err
		}
		image.Path = uploadAddress
		if err := models.DB.Create(image).Error; err != nil {
			return "", err
		}
		avatarAddress = uploadAddress
	}

	// modify the user's avatar information
	if err := models.DB.Model(&models.User{ID: claim.ID}).Select("avatar").Updates(
		map[string]interface{}{"avatar": avatarAddress}).Error; err != nil {
		return "", err
	}
	return avatarAddress, nil
}
