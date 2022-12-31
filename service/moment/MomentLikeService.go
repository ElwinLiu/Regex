package moment

import (
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"terminal/models"
	"terminal/request"
	"terminal/util"
)

func ProcessMomentLike(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	// bind data
	momentLikeReq := new(request.MomentLikeReq)
	if err := c.ShouldBind(momentLikeReq); err != nil {
		return "", err
	}

	// insert or delete like relation
	change := 0

	if momentLikeReq.LikeStatus {
		// delete
		change--
		isExist := models.Like{} // check if the record exists

		if err := models.DB.Where("moment_id = ? AND user_id = ?",
			momentLikeReq.MomentId, claim.ID).First(&isExist).Delete(&models.Like{}).Error; err != nil {
			return "", err
		}
	} else {
		// insert
		like := new(models.Like)
		like.MomentID = momentLikeReq.MomentId
		like.UserID = claim.ID
		if err := models.DB.Create(&like).Error; err != nil {
			return "", err
		}
		change++
	}
	// modify like number
	if err := models.DB.Model(&models.Moment{ID: momentLikeReq.MomentId}).UpdateColumn(
		"LikeNum", gorm.Expr("like_num + ?", change)).Error; err != nil {
		return "", err
	}

	return "", nil
}
