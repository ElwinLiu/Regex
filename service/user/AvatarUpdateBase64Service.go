package user

import (
	"encoding/base64"
	"github.com/gin-gonic/gin"
	"terminal/models"
	"terminal/request"
	"terminal/util"
)

func ProcessAvatarUpdateBase64(c *gin.Context) (string, error) {

	// verify token

	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	// bind data
	avatarUploadBase64Req := new(request.AvatarUpdateBase64Req)
	if err := c.ShouldBind(avatarUploadBase64Req); err != nil {
		return "", err
	}

	// decode the base64 string to get the raw image
	decodeBytes, err := base64.StdEncoding.DecodeString(avatarUploadBase64Req.Base64)
	if err != nil {
		return "", err
	}

	// calculate the hash and check whether it exists in the COS
	has, err, image := util.VerifyFileExistenceBase64(decodeBytes)
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
		// upload it to cos
		uploadAddress, err := util.COSBase64Upload(decodeBytes)
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

	// return result
	return avatarAddress, nil
}
