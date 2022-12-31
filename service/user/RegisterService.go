package user

import (
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"terminal/define"
	"terminal/models"
	"terminal/request"
)

func ProcessRegister(c *gin.Context) (int, error) {
	// bind data
	userRegisterReq := new(request.UserRegisterReq)
	if err := c.ShouldBind(userRegisterReq); err != nil {
		return 0, err
	}

	if userRegisterReq.Account == "" || userRegisterReq.Password == "" || userRegisterReq.NickName == "" {
		return 0, errors.New("missing parameters")
	}

	if len(userRegisterReq.Tags) < 3 {
		return 0, errors.New("should select at least three tags")
	}

	// query whether the account exists
	user := new(models.User)
	if err := models.DB.Where("account = ?", userRegisterReq.Account).First(user).Error; err != nil {
		if err != gorm.ErrRecordNotFound {
			// system error
			return 0, err
		}
	} else {
		// the account exists, return error information
		return 0, errors.New("account already exists")
	}

	// query whether the tag exists
	for i := 0; i < len(userRegisterReq.Tags); i++ {
		tag := new(models.Tag)
		if err := models.DB.Where("id = ?", userRegisterReq.Tags[i]).First(tag).Error; err != nil {
			if err != gorm.ErrRecordNotFound {
				// system error
				return 0, err
			} else {
				return 0, errors.New("tag doesn't exist")
			}
		}
	}

	// if the account doesn't exist, insert it into the database
	user.Account = userRegisterReq.Account
	user.Password = userRegisterReq.Password
	user.Nickname = userRegisterReq.NickName
	user.Avatar = define.DefaultAvatar

	// insert the user information
	if err := models.DB.Create(&user).Error; err != nil {
		return 0, err
	}

	// insert the user tag
	for i := 0; i < len(userRegisterReq.Tags); i++ {
		userTag := models.UserTag{
			UserID: user.ID,
			TagID:  userRegisterReq.Tags[i],
		}
		if err := models.DB.Create(&userTag).Error; err != nil {
			return 0, err
		}
	}

	return user.ID, nil
}
