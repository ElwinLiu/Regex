package moment

import (
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"strconv"
	"terminal/models"
	"terminal/request"
	"terminal/util"
	"time"
)

func ProcessMomentComment(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	// bind data
	momentCommentReq := new(request.MomentCommentReq)
	momentCommentReq.MomentId, err = strconv.Atoi(c.PostForm("moment_id"))
	if err != nil {
		return "", err
	}
	momentCommentReq.ReceiverId, err = strconv.Atoi(c.PostForm("receiver_id"))
	if err != nil {
		return "", err
	}
	momentCommentReq.BelongingId, err = strconv.Atoi(c.PostForm("belonging_id"))
	if err != nil {
		return "", err
	}
	momentCommentReq.TextContent = c.PostForm("text_content")

	// get image's address
	imageAddress := ""
	hasVal, _, err := c.Request.FormFile("file")

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

	// create a model
	comment := new(models.Comment)
	comment.MomentID = momentCommentReq.MomentId
	comment.SenderID = claim.ID
	comment.ReceiverID = momentCommentReq.ReceiverId
	comment.TextContent = momentCommentReq.TextContent
	comment.Image = imageAddress
	comment.BelongingID = momentCommentReq.BelongingId
	comment.CreatedTime = time.Now()

	// insert into comment table
	if comment.ReceiverID == 0 {
		if err := models.DB.Omit("ReceiverID", "BelongingID").Create(&comment).Error; err != nil {
			return "", err
		}
	} else {
		if err := models.DB.Create(&comment).Error; err != nil {
			return "", err
		}
	}

	// modify comment number
	if err := models.DB.Model(&models.Moment{ID: momentCommentReq.MomentId}).UpdateColumn(
		"CommentNum", gorm.Expr("comment_num + ?", 1)).Error; err != nil {
		return "", err
	}

	return "", nil
}
