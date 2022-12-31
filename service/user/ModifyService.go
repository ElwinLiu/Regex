package user

import (
	"github.com/gin-gonic/gin"
	"terminal/models"
	"terminal/request"
	"terminal/util"
)

func ProcessModify(c *gin.Context) error {
	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return err
	}

	// bind data
	userModifyReq := new(request.UserModifyReq)
	if err := c.ShouldBind(userModifyReq); err != nil {
		return err
	}

	// update nickname
	if err := models.DB.Model(&models.User{ID: claim.ID}).Select("nickname").Updates(
		map[string]interface{}{"nickname": userModifyReq.NickName}).Error; err != nil {
		return err
	}

	// update tag
	// firstly, delete the tag current user had before
	if err := models.DB.Where("user_id = ? ", claim.ID).Delete(models.UserTag{}).Error; err != nil {
		return err
	}

	// then insert the user tag
	for i := 0; i < len(userModifyReq.Tags); i++ {
		userTag := models.UserTag{
			UserID: claim.ID,
			TagID:  userModifyReq.Tags[i],
		}
		if err := models.DB.Create(&userTag).Error; err != nil {
			return err
		}
	}
	return nil
}
