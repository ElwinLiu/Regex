package user

import (
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"terminal/models"
	"terminal/request"
	"terminal/response"
	"terminal/util"
)

func ProcessLogin(c *gin.Context) (*response.UserLoginResp, error) {
	userLoginReq := new(request.UserLoginReq)
	if err := c.ShouldBind(&userLoginReq); err != nil {
		return nil, err
	}

	// query whether both the account and password are right
	user := new(models.User)
	if err := models.DB.Where("account = ? AND password = ?", userLoginReq.Account, userLoginReq.Password).First(&user).Error; err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, errors.New("incorrect username or password")
		} else {
			return nil, err
		}
	}

	// generate token
	token, err := util.GenerateToken(user.ID)
	if err != nil {
		return nil, err
	}
	resp := new(response.UserLoginResp)
	resp.ID = user.ID
	resp.Token = token
	return resp, err
}
