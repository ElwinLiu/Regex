package moment

import (
	"errors"
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/models"
	"terminal/util"
)

func ProcessMomentDelete(c *gin.Context) (string, error) {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return "", err
	}

	momentId, err := strconv.Atoi(c.Query("moment_id"))
	if err != nil {
		return "", err
	}

	// check authority
	var ret int
	models.DB.Model(&models.Moment{ID: momentId}).Select("sender_id").Scan(&ret)
	if ret != claim.ID {
		return "", errors.New("current user has no right to modify others' moment")
	}

	// delete in DB
	if err := models.DB.Delete(&models.Moment{ID: momentId}).Error; err != nil {
		return "", err
	}

	return "", nil
}
