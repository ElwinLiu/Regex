package controller

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"terminal/service/match"
	"terminal/service/user"
	"terminal/util"
)

// NormalMatch
// @Tags     Match
// @Summary  used to get normal match result
// @Router   /match/normal [get]
// @Security ApiKeyAuth
func NormalMatch(c *gin.Context) {
	resp, err := match.ProcessNormalMatch(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "match successfully", resp)
}

// AudioMatch
// @Tags     Match
// @Summary  used to get audio match result
// @Param    "audio match" body request.AudioMatchReq true "audio match request parameter"
// @Router   /match/audio [post]
// @Security ApiKeyAuth
func AudioMatch(c *gin.Context) {
	resp, err := match.ProcessAudioMatch(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "match successfully", resp)
}

// MatcherDetail
// @Tags     Match
// @Summary  used to get audio matcher's detailed information
// @Param    id query int true "call id"
// @Router   /match/matcherDetail [get]
// @Security ApiKeyAuth
func MatcherDetail(c *gin.Context) {
	resp, err := match.GetMatcherDetail(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "get successfully", resp)
}

// AudioMatchStop
// @Tags     Match
// @Summary  used to stop current audio match
// @Param    id query int true "call id"
// @Router   /match/audioStop [get]
// @Security ApiKeyAuth
func AudioMatchStop(c *gin.Context) {
	err := match.AudioMatchStop(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "stop successfully", "")
}

// AnonymousMatch
// @Tags     Match
// @Summary  used to get anonymous match result
// @Router   /match/anonymous [get]
// @Security ApiKeyAuth
func AnonymousMatch(c *gin.Context) {
	resp, err := user.ProcessLogin(c)
	if err != nil {
		util.UniformReturn(c, http.StatusOK, false, err.Error(), "")
		return
	}
	util.UniformReturn(c, http.StatusOK, true, "login successfully", resp)
}
