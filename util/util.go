package util

import (
	"bytes"
	"context"
	"crypto/md5"
	"errors"
	"fmt"
	rtctokenbuilder "github.com/AgoraIO/Tools/DynamicKey/AgoraDynamicKey/go/src/RtcTokenBuilder"
	"github.com/dgrijalva/jwt-go"
	"github.com/gin-gonic/gin"
	uuid "github.com/satori/go.uuid"
	"github.com/tencentyun/cos-go-sdk-v5"
	"gorm.io/gorm"
	"log"
	"net/http"
	"net/url"
	"path"
	"terminal/define"
	"terminal/models"
	"time"
)

func UniformReturn(c *gin.Context, status int, success bool, message string, content interface{}) {
	c.JSON(status, gin.H{
		"success": success,
		"message": message,
		"content": content,
	})
}

func GenerateToken(id int) (string, error) {
	uc := define.UserClaim{
		ID: id,
	}
	uc.ExpiresAt = time.Now().AddDate(0, 0, 7).Unix()
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, uc)
	log.Println(token)
	tokenString, err := token.SignedString([]byte(define.JwtSecret))
	if err != nil {
		return "", err
	}
	log.Println(tokenString)
	return tokenString, nil
}

func ParseToken(tokenString string) (*jwt.Token, *define.UserClaim, error) {
	uc := &define.UserClaim{}
	token, err := jwt.ParseWithClaims(tokenString, uc, func(token *jwt.Token) (i interface{}, err error) {
		return []byte(define.JwtSecret), nil
	})
	return token, uc, err
}

func JwtAuthentication(ctx *gin.Context) (*define.UserClaim, error) {
	tokenString := ctx.GetHeader("Authorization")
	if tokenString == "" {
		return nil, errors.New("empty Authorization")
	}
	token, claims, err := ParseToken(tokenString)
	if err != nil || !token.Valid {
		return nil, errors.New("invalid Authorization")
	}
	return claims, nil
}

func GetUUID() string {
	return uuid.NewV4().String()
}

// COSUpload is used to upload the passed-in file into COS
func COSUpload(c *gin.Context) (string, error) {
	file, fileHeader, err := c.Request.FormFile("file")
	u, _ := url.Parse(define.BucketPath)
	b := &cos.BaseURL{BucketURL: u}
	client := cos.NewClient(b, &http.Client{
		Transport: &cos.AuthorizationTransport{
			SecretID:  define.SecretID,
			SecretKey: define.SecretKey,
		},
	})

	key := "terminal/" + GetUUID() + path.Ext(fileHeader.Filename)

	_, err = client.Object.Put(
		context.Background(), key, file, nil,
	)
	if err != nil {
		return "", err
	}
	return define.BucketPath + "/" + key, nil
}

func COSBase64Upload(base64 []byte) (string, error) {
	u, _ := url.Parse(define.BucketPath)
	b := &cos.BaseURL{BucketURL: u}
	client := cos.NewClient(b, &http.Client{
		Transport: &cos.AuthorizationTransport{
			SecretID:  define.SecretID,
			SecretKey: define.SecretKey,
		},
	})
	key := "terminal/" + GetUUID() + ".jpg"
	reader := bytes.NewReader(base64)
	_, err := client.Object.Put(
		context.Background(), key, reader, nil,
	)
	if err != nil {
		return "", err
	}
	return define.BucketPath + "/" + key, nil
}

// VerifyFileExistence is used to verify whether the image uploaded already exists in the COS
func VerifyFileExistence(c *gin.Context) (bool, error, *models.Image) {
	image := new(models.Image)
	file, fileHeader, err := c.Request.FormFile("file")
	if err != nil {
		return false, err, image
	}
	// calculate the file's hash mapping
	b := make([]byte, fileHeader.Size)
	_, err = file.Read(b)
	if err != nil {
		return false, err, image
	}
	hash := fmt.Sprintf("%x", md5.Sum(b))
	// query whether the file already exists
	if err := models.DB.Where("hash = ?", hash).First(image).Error; err != nil {
		if err != gorm.ErrRecordNotFound {
			// system error
			return false, err, image
		} else {
			// the file doesn't exist
			image.Hash = hash
			return false, nil, image
		}
	}
	// file exists
	return true, nil, image
}

func VerifyFileExistenceBase64(data []byte) (bool, error, *models.Image) {
	image := new(models.Image)
	hash := fmt.Sprintf("%x", md5.Sum(data))
	// query whether the file already exists
	if err := models.DB.Where("hash = ?", hash).First(image).Error; err != nil {
		if err != gorm.ErrRecordNotFound {
			// system error
			return false, err, image
		} else {
			// the file doesn't exist
			image.Hash = hash
			return false, nil, image
		}
	}
	// file exists
	return true, nil, image
}

func GetMatchRate(selfID, id int) ([]models.Tag, int, error) {

	// query user_tags table
	var tags []models.Tag
	if err := models.DB.Raw("select tags.id, tags.tag_content "+
		"from user_tags left join tags on user_tags.tag_id = tags.id "+
		"where user_tags.user_id = ?", id).Scan(&tags).Error; err != nil {
		return nil, 0, err
	}

	if selfID == 0 {
		return tags, 0, nil
	}

	matchRate := 90
	var selfTags []models.Tag
	if err := models.DB.Raw("select tags.id, tags.tag_content "+
		"from user_tags left join tags on user_tags.tag_id = tags.id "+
		"where user_tags.user_id = ?", selfID).Scan(&selfTags).Error; err != nil {
		return nil, 0, err
	}

	hash := make(map[int]bool)
	for _, v := range tags {
		hash[v.ID] = true
	}

	for _, v := range selfTags {
		if hash[v.ID] == true {
			matchRate += 3
		}
	}

	if matchRate > 100 {
		matchRate = 100
	}
	return tags, matchRate, nil
}

// 使用 RtcTokenBuilder 来生成 RTC Token
func GenerateRtcToken(int_uid uint32, channelName string, role rtctokenbuilder.Role) (string, error) {

	appID := "56247a7e72a6438fb35355d5e2e58349"
	appCertificate := "514fb9d411244621aa643f597cf8f8e9"
	// AccessToken2 过期的时间，单位为秒
	// 当 AccessToken2 过期但权限未过期时，用户仍在频道里并且可以发流，不会触发 SDK 回调。
	// 但一旦用户和频道断开连接，用户将无法使用该 Token 加入同一频道。请确保 AccessToken2 的过期时间晚于权限过期时间。

	//tokenExpireTimeInSeconds := uint32(40)

	// 权限过期的时间，单位为秒。
	// 权限过期30秒前会触发 token-privilege-will-expire 回调。
	// 权限过期时会触发 token-privilege-did-expire 回调。
	// 为作演示，在此将过期时间设为 40 秒。你可以看到客户端自动更新 Token 的过程
	privilegeExpireTimeInSeconds := uint32(30)
	result, err := rtctokenbuilder.BuildTokenWithUID(appID, appCertificate, channelName, int_uid, role, privilegeExpireTimeInSeconds)
	if err != nil {
		return "", err
	}

	fmt.Printf("Token with uid: %s\n", result)
	fmt.Printf("uid is %d\n", int_uid)
	fmt.Printf("ChannelName is %s\n", channelName)
	fmt.Printf("Role is %d\n", role)
	return result, nil
}

func QueryFollowStatus(selfID, id int) (bool, error) {
	if err := models.DB.Where("follower_id = ? and subscribed_id = ?", selfID, id).First(
		&models.Follow{}).Error; err != nil {
		if err == gorm.ErrRecordNotFound {
			return false, nil
		} else {
			return false, err
		}
	}
	return true, nil
}
