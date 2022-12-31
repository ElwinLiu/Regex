package moment

import (
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/define"
	"terminal/models"
	"terminal/response"
)

func ProcessMomentSubCommentList(c *gin.Context) ([]response.MomentSubCommentListResp, error) {
	page, _ := strconv.Atoi(c.Query("page"))
	offset := (page - 1) * define.MomentSubCommentPageSize
	belongingId, _ := strconv.Atoi(c.Query("belonging_id"))

	var resp []response.MomentSubCommentListResp

	// search by sql, get the subComments of the comment
	if err := models.DB.Raw("SELECT comments.id AS comment_id, comments.text_content, comments.image, "+
		"comments.created_time, user2.id AS receiver_id, user2.nickname AS receiver_name, user1.id AS user_id, user1.nickname AS nickname,"+
		" user1.avatar FROM comments LEFT JOIN users AS user1 on user1.id = comments.sender_id LEFT JOIN "+
		"users AS user2 ON user2.id = comments.receiver_id WHERE comments.belonging_id = ? ORDER BY "+
		"comments.created_time DESC LIMIT ? OFFSET ?", belongingId,
		define.MomentSubCommentPageSize, offset).Scan(&resp).Error; err != nil {
		return nil, err
	}

	return resp, nil
}
