package moment

import (
	"errors"
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/models"
	"terminal/request"
	"terminal/util"
)

func ProcessMomentModify(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	// bind data
	momentModifyReq := new(request.MomentModifyReq)
	momentModifyReq.MomentId, err = strconv.Atoi(c.PostForm("moment_id"))
	if err != nil {
		return "", err
	}
	momentModifyReq.TextContent = c.PostForm("text_content")

	// content check
	if momentModifyReq.TextContent == "" {
		return "", errors.New("content is empty")
	}

	// check authority
	var ret int
	models.DB.Model(&models.Moment{ID: momentModifyReq.MomentId}).Select("sender_id").Scan(&ret)
	if ret != claim.ID {
		return "", errors.New("current user has no right to modify others' moment")
	}

	// get new image's address
	imageAddress := c.PostForm("image")
	hasVal, _, _ := c.Request.FormFile("file")

	// if image is uploaded
	if hasVal != nil {
		has, err, image := util.VerifyFileExistence(c)
		if err != nil {
			return "", err
		}

		if has {
			// the file exists, return the path directly
			imageAddress = image.Path
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
			imageAddress = uploadAddress
		}
	}

	// modify in DB
	if err := models.DB.Model(&models.Moment{ID: momentModifyReq.MomentId}).Updates(
		map[string]interface{}{"TextContent": momentModifyReq.TextContent, "image": imageAddress}).Error; err != nil {
		return "", err
	}
	return imageAddress, nil
}
