package moment

import (
	"errors"
	"fmt"
	"github.com/gin-gonic/gin"
	"terminal/models"
	"terminal/util"
	"time"
)

func ProcessMomentPublish(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	// get image
	imageAddress := ""
	hasVal, _, _ := c.Request.FormFile("file")

	// if image is uploaded
	if hasVal != nil {
		has, err, image := util.VerifyFileExistence(c)
		fmt.Print(c.Request.FormFile("file"))
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

	// content check
	textContent := c.Query("text_content")
	if textContent == "" {
		return "", errors.New("content is empty")
	}

	// insert moment to DB
	moment := new(models.Moment)
	moment.CommentNum = 0
	moment.ViewNum = 0
	moment.LikeNum = 0
	moment.Image = imageAddress
	moment.CreatedTime = time.Now()
	moment.SenderID = claim.ID
	moment.TextContent = textContent
	if err := models.DB.Create(&moment).Error; err != nil {
		return "", err
	}

	return imageAddress, nil
}
