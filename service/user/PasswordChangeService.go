package user

import (
	"errors"
	"github.com/gin-gonic/gin"
	"terminal/models"
	"terminal/request"
	"terminal/util"
)

func ProcessPasswordChange(c *gin.Context) error {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return err
	}

	// bind data
	userPasswordChangeReq := new(request.UserPasswordChangeReq)
	if err := c.ShouldBind(userPasswordChangeReq); err != nil {
		return err
	}

	// query whether the passed-in old password right
	user := new(models.User)
	if err := models.DB.First(&user, claim.ID).Error; err != nil {
		return err
	}
	if user.Password != userPasswordChangeReq.OldPassword {
		return errors.New("password mismatched")
	}

	// update password
	if err := models.DB.Model(&models.User{ID: claim.ID}).Select("password").Updates(
		map[string]interface{}{"password": userPasswordChangeReq.NewPassword}).Error; err != nil {
		return err
	}

	return nil
}
