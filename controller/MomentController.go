package controller

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"terminal/service/moment"
	"terminal/util"
)

// MomentPublish
// @Tags     Moment
// @Summary  used to publish new moment and return status
// @Param    text_content query    string true  "the passed-in parameter of moment's content"
// @Param    file         formData file   false "the image that will be posted by user"
// @Router   /moment/publish [post]
// @Security ApiKeyAuth
func MomentPublish(c *gin.Context) {
	token, err := moment.ProcessMomentPublish(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "publish successfully", token)
}

// MomentModify
// @Tags     Moment
// @Summary  used to modify new moment and return status
// @Param    req  formData request.MomentModifyReq true  "the passed-in parameter of moment's id and new content"
// @Param    file formData file                    false "the image that will be posted by user"
// @Router   /moment/modify [post]
// @Security ApiKeyAuth
func MomentModify(c *gin.Context) {
	token, err := moment.ProcessMomentModify(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "modify successfully", token)
}

// MomentDelete
// @Tags     Moment
// @Summary  used to delete new moment and return status
// @Param    moment_id query int true "the passed-in parameter of moment's id who is waiting to be deleted"
// @Router   /moment/delete [post]
// @Security ApiKeyAuth
func MomentDelete(c *gin.Context) {
	token, err := moment.ProcessMomentDelete(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "delete successfully", token)
}

// MomentLike
// @Tags     Moment
// @Summary  used to like or undo like a moment and return status
// @Param    req body request.MomentLikeReq true "the passed-in parameter of moment's 'like' operation"
// @Router   /moment/like [post]
// @Security ApiKeyAuth
func MomentLike(c *gin.Context) {
	token, err := moment.ProcessMomentLike(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "like successfully", token)
}

// MomentComment
// @Tags     Moment
// @Summary  used to comment a moment or others' comment under this moment and return status
// @Param    req  formData request.MomentCommentReq true  "the passed-in parameter of moment's comment information"
// @Param    file formData file                     false "the image that will be posted by user's comment"
// @Router   /moment/comment [post]
// @Security ApiKeyAuth
func MomentComment(c *gin.Context) {
	token, err := moment.ProcessMomentComment(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "comment successfully", token)
}

// MomentGetDetail
// @Tags    Moment
// @Summary given a certain moment id return info of this moment
// @Param   moment_id query int true "the passed-in parameter of moment_id"
// @Router  /moment/getDetail [get]
// @Security ApiKeyAuth
func MomentGetDetail(c *gin.Context) {
	res, err := moment.ProcessMomentGetDetail(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
	}
	util.UniformReturn(c, http.StatusOK, true, "search successfully", res)
}

// MomentSquareList
// @Tags     Moment
// @Summary  given a certain page number and user_id, return moments of square
// @Param    page query int true "the passed-in parameter of page"
// @Router   /moment/squareList [get]
// @Security ApiKeyAuth
func MomentSquareList(c *gin.Context) {
	res, err := moment.ProcessMomentSquareList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
	}
	util.UniformReturn(c, http.StatusOK, true, "search successfully", res)
}

// MomentFollowedList
// @Tags     Moment
// @Summary  given a certain page number and user_id, return moments of the follwed
// @Param    page query int true "the passed-in parameter of page"
// @Router   /moment/followedList [get]
// @Security ApiKeyAuth
func MomentFollowedList(c *gin.Context) {
	res, err := moment.ProcessMomentFollowedList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
	}
	util.UniformReturn(c, http.StatusOK, true, "search successfully", res)
}

// MomentCommentList
// @Tags    Moment
// @Summary given a certain page number and moment_id, return comments of this moment and 2 of its subcommentList
// @Param   page      query int true "the passed-in parameter of page"
// @Param   moment_id query int true "the passed-in parameter of moment_id"
// @Router  /moment/commentList [get]
func MomentCommentList(c *gin.Context) {
	res, err := moment.ProcessMomentCommentList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
	}
	util.UniformReturn(c, http.StatusOK, true, "search successfully", res)
}

// MomentSubCommentList
// @Tags    Moment
// @Summary given a certain page number and belonging_id, return subcomments of this comment
// @Param   page         query int true "the passed-in parameter of page"
// @Param   belonging_id query int true "the passed-in parameter of belonging_id"
// @Router  /moment/subCommentList [get]
func MomentSubCommentList(c *gin.Context) {
	res, err := moment.ProcessMomentSubCommentList(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
	}
	util.UniformReturn(c, http.StatusOK, true, "search successfully", res)
}
