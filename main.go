package main

import (
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	_ "github.com/go-sql-driver/mysql"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"terminal/controller"
	"terminal/define"
	_ "terminal/docs"
)

// @title                      Intelligent Terminal Backend API
// @version                    0.1
// @description                This is the API document of Intelligent Terminal Backend
// @securityDefinitions.apikey ApiKeyAuth
// @in                         header
// @name                       Authorization
func main() {
	app := gin.New()
	// use cors to solve the cross domain problem
	app.Use(cors.Default())
	Register(app)
	app.Run(":" + define.Port)
}

func Register(app *gin.Engine) {
	app.POST("/user/login", controller.Login)
	app.POST("/user/register", controller.Register)
	app.GET("/user/detail", controller.Detail)
	app.PUT("/user/modify", controller.Modify)
	app.PUT("/user/passwordChange", controller.PasswordChange)
	app.GET("/user/follow", controller.Follow)
	app.GET("/user/followerList", controller.FollowerList)
	app.GET("/user/subscribedList", controller.SubscribedList)
	app.GET("/user/tagList", controller.TagList)
	app.POST("/user/avatarUpdate", controller.AvatarUpdate)
	app.GET("/user/momentList", controller.MomentList)
	app.POST("/user/avatarUpdateBase64", controller.AvatarUpdateBase64)

	//app.GET("/hello", controller.Hello)
	//app.GET("/test", controller.Test)
	//app.POST("/test/upload", controller.TestUpload)

	app.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	app.POST("/moment/publish", controller.MomentPublish)
	app.POST("/moment/modify", controller.MomentModify)
	app.POST("/moment/delete", controller.MomentDelete)
	app.POST("/moment/like", controller.MomentLike)
	app.POST("/moment/comment", controller.MomentComment)
	app.GET("/moment/squareList", controller.MomentSquareList)
	app.GET("/moment/commentList", controller.MomentCommentList)
	app.GET("/moment/subCommentList", controller.MomentSubCommentList)
	app.GET("/moment/followedList", controller.MomentFollowedList)
	app.GET("/moment/getDetail", controller.MomentGetDetail)

	app.GET("/match/normal", controller.NormalMatch)
	app.POST("/match/audio", controller.AudioMatch)
	app.GET("/match/matcherDetail", controller.MatcherDetail)
	app.GET("/match/audioStop", controller.AudioMatchStop)
	app.GET("/match/anonymous", controller.AnonymousMatch)

	//app.GET("/match/anonymous", controller.AnonymousMatch)
}
