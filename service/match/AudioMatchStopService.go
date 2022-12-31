package match

import (
	"errors"
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/models"
	"terminal/util"
)

func AudioMatchStop(c *gin.Context) error {

	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return err
	}
	callID, _ := strconv.Atoi(c.Query("id"))

	// query whether the user has the auth to stop the audio match
	call := new(models.Call)
	call.ID = callID
	if err := models.DB.First(&call).Error; err != nil {
		return err
	}

	if call.UserID != claim.ID {
		return errors.New("do not have the authorization to stop this audio match")
	}
	// if so, delete the record directly
	if err := models.DB.Delete(&call).Error; err != nil {
		return err
	}
	return nil
}
