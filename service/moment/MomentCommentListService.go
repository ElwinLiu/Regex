package moment

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"strconv"
	"terminal/define"
	"terminal/models"
	"terminal/response"
	"time"
)

type OriginComment struct {
	UserId      int       `json:"user_id"`
	CommentId   int       `json:"comment_id"`
	Nickname    string    `json:"nickname"`
	Avatar      string    `json:"avatar"`
	TextContent string    `json:"text_content"`
	Image       string    `json:"image"`
	CreatedTime time.Time `json:"created_time"`
}

func ProcessMomentCommentList(c *gin.Context) ([]response.MomentCommentListResp, error) {
	page, _ := strconv.Atoi(c.Query("page"))
	offset := (page - 1) * define.MomentCommentPageSize
	momentId, _ := strconv.Atoi(c.Query("moment_id"))

	var originComments []OriginComment
	var resp []response.MomentCommentListResp

	// search by sql, get the comments of the moment
	if err := models.DB.Raw("SELECT comments.id AS comment_id, comments.text_content,"+
		" comments.image, comments.created_time, users.id as user_id,"+
		" users.nickname, users.avatar FROM "+
		"comments LEFT JOIN users on users.id = comments.sender_id where "+
		"ISNULL(comments.receiver_id) AND comments.moment_id = ? "+
		"ORDER BY comments.created_time DESC LIMIT ? OFFSET ?",
		momentId, define.MomentCommentPageSize, offset).Scan(&originComments).Error; err != nil {
		return nil, err
	}

	// search by sql, get the subComments of the comments
	for _, item := range originComments {
		fmt.Print("\n111111")
		var momentCommentListResp response.MomentCommentListResp
		momentCommentListResp.UserId = item.UserId
		momentCommentListResp.CommentId = item.CommentId
		momentCommentListResp.Nickname = item.Nickname
		momentCommentListResp.Avatar = item.Avatar
		momentCommentListResp.TextContent = item.TextContent
		momentCommentListResp.Image = item.Image
		momentCommentListResp.CreatedTime = item.CreatedTime

		if err := models.DB.Raw("SELECT comments.id AS comment_id, comments.text_content, comments.image, "+
			"comments.created_time, user2.id AS receiver_id, user2.nickname AS receiver_name, user1.id AS user_id, user1.nickname AS nickname,"+
			" user1.avatar FROM comments LEFT JOIN users AS user1 on user1.id = comments.sender_id LEFT JOIN "+
			"users AS user2 ON user2.id = comments.receiver_id WHERE comments.belonging_id = ? ORDER BY "+
			"comments.created_time DESC LIMIT ? OFFSET ?", item.CommentId, 2, 0).Scan(&momentCommentListResp.SubComment).Error; err != nil {
			return nil, err
		}
		resp = append(resp, momentCommentListResp)
	}

	// update view num
	if page == 1 {
		if err := models.DB.Exec("UPDATE moments set view_num = view_num + 1 where id = ?", momentId).Error; err != nil {
			return nil, err
		}
	}

	return resp, nil
}
