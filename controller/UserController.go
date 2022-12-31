package controller

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"terminal/service/user"
	"terminal/util"
)

// Login
// @Tags    User
// @Summary used to authorize user and return jwt token, id
// @Param   req body request.UserLoginReq true "the passed-in parameter of login function"
// @Router  /user/login [post]
func Login(c *gin.Context) {
	resp, err := user.ProcessLogin(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "login successfully", resp)
}

// AvatarUpdate
// @Tags     User
// @Summary  used to upload image and replace user's avatar as the uploaded image
// @Param    file formData file false "the avatar image file selected by the user"
// @Router   /user/avatarUpdate [post]
// @Security ApiKeyAuth
func AvatarUpdate(c *gin.Context) {
	address, err := user.ProcessAvatarUpload(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "upload avatar successfully", address)
}

// Register
// @Tags    User
// @Summary used to register new account
// @Param   UserRegisterReq body request.UserRegisterReq true "the passed-in parameter of register function"
// @Router  /user/register [post]
func Register(c *gin.Context) {
	// process register pipeline
	id, err := user.ProcessRegister(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "register successfully", id)
}

// Detail
// @Tags     User
// @Summary  used to get the target user's detailed information
// @Param    id query int true "id"
// @Router   /user/detail [get]
// @Security ApiKeyAuth
func Detail(c *gin.Context) {
	resp, err := user.ProcessDetail(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get user detail successfully", resp)
}

// Modify
// @Tags     User
// @Summary  used to modify the user's personal information
// @Param    UserModifyReq body request.UserModifyReq true "the passed-in parameter of modify function"
// @Router   /user/modify [put]
// @Security ApiKeyAuth
func Modify(c *gin.Context) {
	if err := user.ProcessModify(c); err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "modify successfully", "")
}

// PasswordChange
// @Tags     User
// @Summary  used to modify the user's password
// @Param    UserPasswordChangeReq body request.UserPasswordChangeReq true "old password and new password"
// @Router   /user/passwordChange [put]
// @Security ApiKeyAuth
func PasswordChange(c *gin.Context) {
	if err := user.ProcessPasswordChange(c); err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "change password successfully", "")
}

// FollowerList
// @Tags     User
// @Summary  used to get the user's follower list
// @Param    id   query int true "id"
// @Param    page query int true "page"
// @Router   /user/followerList [get]
// @Security ApiKeyAuth
func FollowerList(c *gin.Context) {
	resp, err := user.ProcessFollowerList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get follower list successfully", resp)
}

// SubscribedList
// @Tags     User
// @Summary  used to get the user's subscribed list
// @Param    id   query int true "id"
// @Param    page query int true "page"
// @Router   /user/subscribedList [get]
// @Security ApiKeyAuth
func SubscribedList(c *gin.Context) {
	resp, err := user.ProcessSubscribedList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get subscribed list successfully", resp)
}

// TagList
// @Tags    User
// @Summary used to get all the tag
// @Router  /user/tagList [get]
func TagList(c *gin.Context) {
	tags, err := user.GetTagList()
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get tag list successfully", tags)
}

// Follow
// @Tags     User
// @Summary  used to follow other people
// @Param    id query int true "id"
// @Router   /user/follow [get]
// @Security ApiKeyAuth
func Follow(c *gin.Context) {
	if err := user.ProcessFollow(c); err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "done successfully", "")
}

// MomentList
// @Tags     User
// @Summary  used to show the moment list of specified user
// @Param    id   query int true "id"
// @Param    page query int true "page"
// @Router   /user/momentList [get]
// @Security ApiKeyAuth
func MomentList(c *gin.Context) {
	momentList, err := user.GetMomentList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get moment list successfully", momentList)
}

// AvatarUpdateBase64
// @Tags     User
// @Summary  used to update the avatar in the form of base64 string
// @Param    "base64" body request.AvatarUpdateBase64Req true "base64 string"
// @Router   /user/avatarUpdateBase64 [post]
// @Security ApiKeyAuth
func AvatarUpdateBase64(c *gin.Context) {
	path, err := user.ProcessAvatarUpdateBase64(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "upload successfully", path)
}
