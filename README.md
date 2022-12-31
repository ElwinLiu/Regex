# 后端接口文档1.4

接口文档地址：http://106.14.78.79:40010/swagger/index.html#/

后端统一返回格式：

```json
{
  "content": 
  "message": "",
  "success": true
}
```

content为模板类型，存放接口的返回内容

message为string类型，返回执行消息，如成功执行或执行所遇到的错误

success为bool类型，表示接口是否被成功调用

## User

### Register

用户注册接口

**调用地址**：http://106.14.78.79:40010/user/register

**接口请求类型**：POST

**传入参数类型**：body,即Android开发中`retrofit2`的`@Body`注记类型

```json
{
  "account": "string",
  "nickname": "string",
  "password": "string",
  "tags": [
    0
  ]
}
```

**返回参数**：success字段代表是否成功注册，若success为false，则message中为错误原因。content始终为空字符串

### Login

用户登录接口

**调用地址**：http://106.14.78.79:40010/user/login

**接口请求类型**：POST

**传入参数类型**：body

```json
{
  "account": "string",
  "password": "string"
}
```

**返回参数**：

若success为false，则message中为错误原因。content为空字符串

若success为true，则content中含有与用户的ID和jwt token

以下为成功调用的返回结果

```json
{
  "content": {
    "id": 1,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZXhwIjoxNjcxMzU1MDIxfQ.kdJS1MTiYD6UCXFzTxfm43tMA2aaUxfrc-t40J81MX0"
  },
  "message": "login successfully",
  "success": true
}
```



### AvatarUpload

用户上传更新头像接口，调用时需在header的Authorization字段中携带用户的jwt token

**调用地址**：http://106.14.78.79:40010/user/avatarUpdate

**接口请求类型**：POST

**传入参数类型**：formData

文件参数名为file，为用户上传的新头像

**返回参数**：若成功调用，则content中为上传的头像的url地址，若失败则content为空字符串。

以下为成功调用的返回结果

```json
{
  "content": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/c042703a-ff3e-41ab-a259-0c6b60a26bae.jpg",
  "message": "upload avatar successfully",
  "success": true
}
```

### Detail

获取目标用户的详细信息，在未登录情况下不携带token，在登陆状态下携带token进行请求

**调用地址**：http://106.14.78.79:40010/user/detail

**接口请求类型**：GET

**传入参数类型**：query  参数名：id 类型：int

**返回参数**：若成功调用，则content中含有该用户的id，头像，昵称，用户喜好标签列表，关注者数量，被关注者数量，是否关注该用户，用户注册日期

tag列表组成为tag的id与tag的内容：

```
[
	{id: "",
	tag_content: ""},
	{}
]
```



以下为成功调用的返回结果

```json
{
  "content": {
    "id": 1,
    "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/c042703a-ff3e-41ab-a259-0c6b60a26bae.jpg",
    "nickname": "string",
    "tags": [
      {
        "id": 1,
        "tag_content": "音乐"
      },
      {
        "id": 2,
        "tag_content": "体育"
      }
    ],
    "follower_num": 0,
    "subscribed_num": 0,
    "subscribe_status": false,
    "created_time": "2022-12-11T01:26:59+08:00"
  },
  "message": "get user detail successfully",
  "success": true
}
```



### Modify

个人信息修改接口，调用时需在header的Authorization字段中携带用户的jwt token

**调用地址**：http://106.14.78.79:40010/user/modify

**接口请求类型**：PUT

**传入参数类型**：body

```json
{
  "nickname": "string",
  "tags": []
}
```

**返回参数**：修改成功状态，content始终为空字符串



### PasswordChange

个人密码修改接口，调用时需在header的Authorization字段中携带用户的jwt token

**调用地址**：http://106.14.78.79:40010/user/passwordChange

**接口请求类型**：PUT

**传入参数类型**：body

```json
{
  "new_password": "string",
  "old_password": "string"
}
```

**返回参数**：修改成功状态，content始终为空字符串



### FollowerList

查看用户的关注者, 调用时可选择在header的Authorization字段中携带用户的jwt token，携带后显示自己与列表中用户的关注状态，不携带则关注状态全部为false。设计为分页请求，每页大小为15条数据

**调用地址**：http://106.14.78.79:40010/user/followerList

**接口请求类型**：GET

**传入参数类型**：

query类型参数 id，参数类型为 int

query类型参数page，参数类型为 int

**返回参数**：

若成功调用接口且用户有关注者，则content中为关注者列表，具体包含关注者的id，昵称，头像，发布动态数量，自身对该用户的关注状态，该用户的注册事件；若该用户无关注者则content为null

以下为成功调用的返回结果

```json
{
  "content": [
    {
      "id": 2,
      "nickname": "test",
      "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/default.png",
      "momentNum": 0,
      "subscribe_status": true,
      "created_time": "2022-12-11T18:05:12+08:00"
    }
  ],
  "message": "get follower list successfully",
  "success": true
}
```



### SubscribedList

查看用户的被关注列表, 调用时可选择在header的Authorization字段中携带用户的jwt token，携带后显示自己与列表中用户的关注状态，不携带则关注状态全部为false。设计为分页请求，每页大小为15条数据

**调用地址**：http://106.14.78.79:40010/user/subscribedList

**接口请求类型**：GET

**传入参数类型**：

query类型参数 id，参数类型为 int

query类型参数page，参数类型为 int

**返回参数**：

若成功调用接口且用户有关注者，则content中为关注者列表，具体包含关注者的id，昵称，头像，发布动态数量，自身对该用户的关注状态，该用户的注册事件；若该用户无关注者则content为null

以下为成功调用的返回结果

```json
{
  "content": [
    {
      "id": 2,
      "nickname": "test",
      "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/default.png",
      "momentNum": 0,
      "subscribe_status": true,
      "created_time": "2022-12-11T18:05:12+08:00"
    }
  ],
  "message": "get subscribed list successfully",
  "success": true
}
```



### TagList

获取所有tag id与内容的接口

**调用地址**：http://106.14.78.79:40010/user/tagList

**接口请求类型**：GET

**传入参数类型**：无

**返回参数**：

content为tag数组，以下为成功调用的返回结果

```json
{
  "content": [
    {
      "id": 1,
      "tag_content": "音乐"
    },
    {
      "id": 2,
      "tag_content": "体育"
    },
    {
      "id": 3,
      "tag_content": "美术"
    }
  ],
  "message": "get tag list successfully",
  "success": true
}
```



### Follow

关注或取消关注接口。调用时需在header的Authorization字段中携带用户的jwt token。

若未关注对应id的用户，则调用后将关注该用户。若已关注对应id的用户，则调用后将取消关注该用户。

**调用地址**：http://106.14.78.79:40010/user/follow

**接口请求类型**：GET

**传入参数类型**：query  id  int

**返回参数**：调用成功状态，content始终为空字符串

---



## Moment

### Publish

用户发布动态接口

**调用地址**：http://106.14.78.79:40010/moment/publish

**接口请求类型**：POST

**传入参数类型**：

query类型参数 text_content，参数类型为 string

formData类型参数file

**返回参数**：success字段代表是否成功发布，若success为false，则message中为错误原因。content始终为空字符串



### Modify

用户修改动态接口

若原动态无图片，需添加图片：image为空字符串，file传图片

若原动态无图片，不需要添加新图片：image为空字符串，file空

若原动态有图片，需删除图片：image为空字符串，file空

若原动态有图片，需修改图片：image为原图地址，file传图片

若原动态有图片，不希望改动图片：image为原图地址，file空

**调用地址：**http://106.14.78.79:40010/moment/modify

**接口请求类型**：POST

**传入参数类型**：

formData类型参数moment_id，参数类型为int

formData类型参数text_content，参数类型为string

formData类型参数image，参数类型为string

formData类型参数file

**返回参数**：success字段代表是否编辑成功，若success为false，则message中为错误原因。content始终为空字符串



### Delete

用户删除动态接口

**调用地址：**http://106.14.78.79:40010/moment/delete

**接口请求类型**：POST

**传入参数类型**： 

query类型参数moment_id，参数类型为int

**返回参数**：success字段代表是否删除成功，若success为false，则message中为错误原因。content始终为空字符串



### Like

点赞某条动态

like_status是当前是否已点赞，如果传入true，则后端将点赞操作取消，反之亦然

**调用地址：**http://106.14.78.79:40010/moment/like

**接口请求类型**：POST

**传入参数类型**： 

body类型参数，json格式

```
{
  "like_status": "bool",
  "moment_id": "int"
}
```

**返回参数**：success字段代表是否点赞成功，若success为false，则message中为错误原因。content始终为空字符串



### Comment

评论某条动态，或评论该动态下的评论

如果回复的是动态，则receiver_id和belonging_id传入0即可

如果回复的是评论（为方便表述，这里规定B评论为被评论一方），则receiver_id应为B评论所属的用户id，belonging_id则为B评论的id

**调用地址：**http://106.14.78.79:40010/moment/Comment

**接口请求类型**：POST

**传入参数类型**： 

formData类型参数moment_id，参数类型int

formData类型参数text_content，参数类型string

formData类型参数receiver_id，参数类型int

formData类型参数belonging_id，参数类型int

formData类型参数file（传递评论时附加的图片）

**返回参数**：success字段代表是否评论成功，若success为false，则message中为错误原因。content始终为空字符串



### SquareList

列出广场的动态列表，暂定每页10条（可修改）

**调用地址：**http://106.14.78.79:40010/moment/squareList

**接口请求类型**：GET

**传入参数类型**： 

query类型参数page，参数类型int

**返回参数**：

```json
"content": [
	{
	  "avatar": "string",
      "nickname": "string",
      "text_content": "string",
      "image": "string",
      "like_num": "int",
      "is_liked": "bool",
      "comment_num": "int",
      "view_num": "int",
      "created_time": "time",
      "moment_id": "int",
      "sender_id": "int",
      "is_follower": "bool"
	}
],
  "message": "search successfully",
  "success": true
```



## FollowedList

列出被关注人的动态列表，暂定每页10条（可修改）

**调用地址：**http://106.14.78.79:40010/moment/followedList

**接口请求类型**：GET

**传入参数类型**： 

query类型参数page，参数类型int

**返回参数**：

```json
"content": [
	{
	  "avatar": "string",
      "nickname": "string",
      "text_content": "string",
      "image": "string",
      "like_num": "int",
      "is_liked": "bool",
      "comment_num": "int",
      "view_num": "int",
      "created_time": "time",
      "moment_id": "int",
      "sender_id": "int",
      "is_follower": "bool"
	}
],
  "message": "search successfully",
  "success": true
```



### CommentList

列出点击进入动态详情后的评论内容

该部分参考soul的实现，进入动态详情后显示出所有对动态的评论内容，每条评论最多只显示两条子评论("sub_comment"数组里显示)，后续可进入“更多回复”的界面，再调用SubCommentList接口即可.

**调用地址：**http://106.14.78.79:40010/moment/CommentList

**接口请求类型**：GET

query类型参数page，参数类型int

query类型参数moment_id，参数类型int

**返回参数**：

```json
"content": [
	{
	  "user_id": 	"int",
      "comment_id": "int",
      "nickname": "string",
      "avatar": "string",
      "text_content": "string",
      "image": "string",
      "created_time": "time",
      "sub_comment": [
      	{
      	  "user_id": "int",
          "comment_id": "int",
          "nickname": "string",
          "avatar": "string",
          "text_content": "string",
          "image": "string",
          "created_time": "time",
          "receiver_id": "int",
          "receiver_name": "string"
      	},
        {
            ...
        }
      ]
	},
    {
        ...
    }
],
"message": "search successfully",
"success": true
      
```



### SubCommentList

查看某条评论的子评论

传递的参数belonging_id上面comment接口中已经介绍过了，代表评论B的id

**调用地址：**http://106.14.78.79:40010/moment/SubCommentList

**接口请求类型**：GET

query类型参数page，参数类型int

query类型参数belonging_id，参数类型int

**返回参数**：

```json
"sub_comment": 
[
  {
	"user_id": "int",
    "comment_id": "int",
    "nickname": "string",
    "avatar": "string",
    "text_content": "string",
    "image": "string",
    "created_time": "time",
    "receiver_id": "int",
    "receiver_name": "string"
   },
   {
   	...
   }
 ]，
  "message": "search successfully",
  "success": true
```

