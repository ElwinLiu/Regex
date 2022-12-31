package moment

import (
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/define"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func ProcessMomentFollowedList(c *gin.Context) ([]response.MomentSquareListResp, error) {
	// process token
	claim, err := util.JwtAuthentication(c)
	if err != nil && err.Error() == "invalid Authorization" {
		return nil, err
	}

	userId := 0
	if claim != nil {
		userId = claim.ID
	}

	page, _ := strconv.Atoi(c.Query("page"))
	offset := (page - 1) * define.MomentPageSize

	var resp []response.MomentSquareListResp

	// searching by sql
	if err := models.DB.Raw("select users.nickname, users.avatar, moments.text_content, moments.image, "+
		"moments.like_num, moments.comment_num, moments.view_num, moments.created_time, moments.id as 'moment_id', "+
		"moments.sender_id, CASE WHEN EXISTS(SELECT * FROM likes WHERE user_id = ? AND moment_id = moments.id) "+
		"THEN 1 ELSE 0 END AS is_liked,CASE WHEN EXISTS(SELECT * FROM follows WHERE subscribed_id = "+
		"moments.sender_id AND follower_id = ?) THEN 1 ELSE 0 END AS is_followed "+
		"from moments LEFT JOIN users ON users.id = moments.sender_id "+
		"WHERE moments.sender_id IN (SELECT subscribed_id FROM follows WHERE follower_id = ?) "+
		"ORDER BY moments.created_time desc LIMIT ? OFFSET ?;", userId, userId, userId, define.MomentPageSize, offset).Scan(&resp).Error; err != nil {
		return nil, err
	}

	return resp, nil
}
