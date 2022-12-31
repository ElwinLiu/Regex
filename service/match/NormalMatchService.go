package match

import (
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"terminal/models"
	"terminal/response"
	"terminal/util"
)

func ProcessNormalMatch(c *gin.Context) (*response.NormalMatchResp, error) {

	// verify token
	claim, err := util.JwtAuthentication(c)
	if err != nil {
		return nil, err
	}

	resp := new(response.NormalMatchResp)
	for i := 0; i < 10; i++ {
		// firstly get some random id
		var ids []int
		if err := models.DB.Raw("SELECT t1.id FROM users AS t1 JOIN (SELECT ROUND(RAND()*(SELECT " +
			"MAX(id) FROM users)) AS id) AS t2 WHERE t1.id>=t2.id ORDER BY t1.id LIMIT 5;").Scan(&ids).Error; err != nil {
			return nil, err
		}

		// query whether they have matched before
		match := new(models.Match)
		for _, v := range ids {
			if claim.ID == v {
				continue
			}
			if err := models.DB.Raw("select * from matches where (matcher_id = ? and matched_id = ?)",
				claim.ID, v).Scan(&match).Error; err != nil {
				return nil, err
			} else {
				if match.ID != 0 {
					continue
				}
				// get resp information
				resp, err = getDetailedInfo(claim.ID, v)
				if err != nil {
					return nil, err
				}
				// insert the match information into database
				if err := insertMatchRecord(claim.ID, v); err != nil {
					return nil, err
				}
				return resp, nil
			}
		}
	}
	return nil, errors.New("no suitable matcher currently")
}

func getDetailedInfo(selfID, id int) (*response.NormalMatchResp, error) {
	tags, matchRate, err := util.GetMatchRate(selfID, id)
	if err != nil {
		return nil, err
	}

	// get detailed information
	user := new(models.User)
	if err := models.DB.Where("id = ?", id).First(&user).Error; err != nil {
		return nil, err
	}

	// query is followed status
	isFollowed := false
	follow := new(models.Follow)
	if err := models.DB.Where("subscribed_id = ? and follower_id = ?", id, selfID).First(&follow).Error; err != nil {
		if err != gorm.ErrRecordNotFound {
			return nil, err
		}
	} else {
		isFollowed = true
	}

	resp := response.NormalMatchResp{
		ID:         id,
		Nickname:   user.Nickname,
		Avatar:     user.Avatar,
		MatchRate:  matchRate,
		Tag:        tags,
		IsFollowed: isFollowed,
	}
	return &resp, nil

}

func insertMatchRecord(selfID, id int) error {

	match := models.Match{
		MatcherID: selfID,
		MatchedId: id,
	}
	if err := models.DB.Create(&match).Error; err != nil {
		return err
	}
	return nil
}
