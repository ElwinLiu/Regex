package moment

import (
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func ProcessMomentGetDetail(c *gin.Context) (*response.MomentSquareListResp, error) {
	// process token
	claim, err := util.JwtAuthentication(c)
	if err != nil && err.Error() == "invalid Authorization" {
		return nil, err
	}

	userId := 0
	if claim != nil {
		userId = claim.ID
	}

	momentId, _ := strconv.Atoi(c.Query("moment_id"))

	momentDetail := new(response.MomentSquareListResp)

	// searching by sql
	if err := models.DB.Raw("select "+
		"users.avatar, users.nickname, moments.text_content,"+
		" moments.image, moments.like_num, moments.comment_num, "+
		"moments.view_num, moments.created_time, moments.id as 'moment_id', "+
		"moments.sender_id , CASE WHEN EXISTS(SELECT * FROM likes WHERE "+
		"user_id = ? AND moment_id = moments.id)"+
		" THEN 1 ELSE 0 END AS is_liked ,CASE WHEN "+
		"EXISTS(SELECT * FROM follows WHERE subscribed_id = moments.sender_id AND"+
		" follower_id = ?) THEN 1 ELSE 0 END AS is_followed from moments "+
		"LEFT JOIN users ON users.id = moments.sender_id "+
		"Where moments.id = ?", userId, userId, momentId).Scan(&momentDetail).Error; err != nil {
		return nil, err
	}

	return momentDetail, nil
}
