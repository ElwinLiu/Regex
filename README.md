# Back-end interface documentation 1.4

Interface documentation address: http://106././.79:40010/swagger/index.html#/

Back-end uniform return format:

```json
{
  "content". 
  "message": "".
  "success": true
}
```

content is a template type that holds the return content of the interface

message is a string that returns the execution message, such as success or the error encountered by the execution

success is a bool type, indicating whether the interface was called successfully

## User

### Register

User registration interface

**Call address**: http://106././.79:40010/user/register

**Interface request type**: POST

**Input parameter type**: body, i.e. `@Body` note type of `retrofit2` in Android development

```json
{
  "account": "string".
  "nickname": "string".
  "password": "string".
  "tags": [
    0
  ]
}
```

**return parameters**: the success field represents whether the registration is successful or not, if success is false, the error reason is in message. content is always the empty string

### Login

User login interface

**Call address**: http://106././.79:40010/user/login

**Interface request type**: POST

**Input parameter type**: body

```json
{
  "account": "string".
  "password": "string"
}
```

**return parameters**:

If success is false, the message is the reason for the error. content is the empty string

If success is true, the content contains the ID and jwt token of the user.

The following is the return result of a successful call

```json
{
  "content": {
    "id": 1.
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZXhwIjoxNjcxMzU1MDIxfQ.kdJS1MTiYD6UCXFzTxfm43tMA2aaUxfrc-t40J81MX0"
  }.
  "message": "login successfully".
  "success": true
}
```



### AvatarUpload

User upload update avatar interface, called with the user's jwt token in the Authorization field of the header

**Call address**: http://106././.79:40010/user/avatarUpdate

**Interface request type**: POST

**Input parameter type**: formData

File parameter named file, for the new avatar uploaded by the user

**return parameter**: if the call is successful, the url address of the uploaded avatar will be in the content, if it fails, the content will be an empty string.

The following is the return result of a successful call

```json
{
  "content": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/c042703a-ff3e-41ab-a259-0c6b60a26bae.jpg".
  "message": "upload avatar successfully".
  "success": true
}
```

### Detail

Get the details of the target user, without token if not logged in, and with token if logged in

**Call address**: http://106././.79:40010/user/detail

**Interface request type**: GET

**Input parameter type**: query Parameter name: id Type: int

**return parameters**: if successfully called, the content contains the id of the user, avatar, nickname, list of user preferences tag, number of followers, number of followed, whether to follow the user, user registration date

The tag list is composed of the id of the tag and the content of the tag:

```
[
	{id: "".
	tag_content: ""}.
	{}
]
```



The following is the return result of a successful call

```json
{
  "content": {
    "id": 1.
    "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/c042703a-ff3e-41ab-a259-0c6b60a26bae.jpg".
    "nickname": "string".
    "tags": [
      {
        "id": 1.
        "tag_content": "music"
      }.
      {
        "id": 2.
        "tag_content": "sports"
      }
    ].
    "follower_num": 0.
    "subscribed_num": 0.
    "subscribe_status": false.
    "created_time": "2022-12-11T01:26:59+08:00"
  }.
  "message": "get user detail successfully".
  "success": true
}
```



### Modify

Modify the personal information interface, when called you need to carry the user's jwt token in the Authorization field of the header

**Call address**: http://106././.79:40010/user/modify

**Interface request type**: PUT

**Input parameter type**: body

```json
{
  "nickname": "string".
  "tags": []
}
```

**return parameter**: modification success status, content is always empty string



### PasswordChange

personal password change interface, call to carry the user's jwt token in the Authorization field of the header

**call address**: http://106././.79:40010/user/passwordChange

**Interface request type**: PUT

**Input parameter type**: body

```json
{
  "new_password": "string".
  "old_password": "string"
}
```

**return parameter**: modify success status, content is always empty string



### FollowerList

View the user's followers, when called you can choose to carry the user's jwt token in the header's Authorization field, carry it to show your own concern status with the list of users, do not carry it then all concern status is false. designed for paging requests, the size of each page is 15 pieces of data

**call address**: http://106././.79:40010/user/followerList

**Interface request type**: GET

**Input parameter type**:

query type parameter id, the parameter type is int

query type parameter page, the parameter type is int

**return parameter**:

If the interface is successfully called and the user has followers, the content is a list of followers, including the id, nickname, avatar, the number of dynamic release, the user's own attention status, the user's registration events; if the user does not have followers, the content is null

The following is the return result of the successful call

```json
{
  "content": [
    {
      "id": 2.
      "nickname": "test".
      "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/default.png".
      "momentNum": 0.
      "subscribe_status": true.
      "created_time": "2022-12-11T18:05:12+08:00"
    }
  ].
  "message": "get follower list successfully".
  "success": true
}
```



### SubscribedList

View the user's followed list, optionally carry the user's jwt token in the Authorization field of the header when called, carry it to show your followed status with the users in the list, don't carry it then the followed status is all false. designed as a paged request, each page size is 15 pieces of data

**call address**: http://106././.79:40010/user/subscribedList

**Interface request type**: GET

**Input parameter type**:

query type parameter id, the parameter type is int

query type parameter page, the parameter type is int

**return parameter**:

If the interface is successfully called and the user has followers, the content is a list of followers, including the id, nickname, avatar, the number of dynamic release, the user's own attention status, the user's registration events; if the user does not have followers, the content is null

The following is the return result of the successful call

```json
{
  "content": [
    {
      "id": 2.
      "nickname": "test".
      "avatar": "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/default.png".
      "momentNum": 0.
      "subscribe_status": true.
      "created_time": "2022-12-11T18:05:12+08:00"
    }
  ].
  "message": "get subscribed list successfully".
  "success": true
}
```



### TagList

Interface to get all tag ids and contents

**Call address**: http://106././.79:40010/user/tagList

**Interface request type**: GET

**Input parameter type**: None

**return parameters**:

content is the tag array, the following is the return result of a successful call

```json
{
  "content": [
    {
      "id": 1.
      "tag_content": "music"
    }.
    {
      "id": 2.
      "tag_content": "sports"
    }.
    {
      "id": 3.
      "tag_content": "fine_arts"
    }
  ].
  "message": "get tag list successfully".
  "success": true
}
```



### Follow

Interface for following or unfollowing. Called with the user's jwt token in the Authorization field of the header.

If the user with the corresponding id is not followed, the user will be followed after the call. If the user with the corresponding id is already followed, the user will be unfollowed after the call.

**Call address**: http://106././.79:40010/user/follow

**Interface request type**: GET

**Input parameter type**: query id int

**return parameters**: call success status, content is always the empty string

---



## Moment

### Publish

User publish dynamic interface

**Call address**: http://106././.79:40010/moment/publish

**Interface request type**: POST

**Input parameter type**:

query type parameter text_content, parameter type string

formData type parameter file

**return parameter**: success field represents whether the successful release, if success is false, then the error reason in message. content is always the empty string



### Modify

User modifies the dynamic interface

If the original dynamic without pictures, need to add pictures: image is the empty string, file pass pictures

If there is no image in the original dynamic, no need to add a new image: image is empty string, file is empty

If the original dynamic has images, need to delete the images: image is empty string, file is empty

If the original dynamic has images, need to modify the images: image is the original image address, file pass the image

If the original dynamic has a picture, do not want to change the picture: image for the original address, file empty

**call address:** http://106././.79:40010/moment/modify

**Interface request type**: POST

**Input parameter type**:

formData type parameter moment_id, the parameter type is int

formData type parameter text_content, the parameter type is string

formData type parameter image, parameter type string

formData type parameter file

**return parameter**: success field represents whether the edit is successful, if success is false, then the error reason in message. content is always empty string



### Delete

User delete dynamic interface

**Call address:** http://106././.79:40010/moment/delete

**Interface request type**: POST

**Input parameter type**: 

query type parameter moment_id, the parameter type is int

**return parameter**: success field represents whether the deletion is successful, if success is false, then the error reason in message. content is always the empty string



### Like

Like a dynamic

like_status is whether the current like has been liked, if passed in true, the backend will cancel the like operation, and vice versa

**Call address:** http://106././.79:40010/moment/like

**Interface request type**: POST

**Input parameter type**: 

body type parameter, json format

```
{
  "like_status": "bool".
  "moment_id": "int"
}
```

**return parameters**: the success field represents whether the like was successful or not, if success is false, then the error reason is in message. content is always the empty string



### Comment

Comment on a dynamic, or comment on the dynamic's comments

If the reply is a dynamic, the receiver_id and believing_id can be passed to 0

If the reply is a comment (for the sake of convenience, here specify that comment B is the commented party), then receiver_id should be the user id of comment B, and believing_id should be the id of comment B

**Call address:** http://106././.79:40010/moment/Comment

**Interface request type**: POST

**Input parameter type**: 

formData type parameter moment_id, parameter type int

formData type parameter text_content, parameter type string

formData type parameter receiver_id, parameter type int

formData type parameter belonging_id, parameter type int

formData type parameter file (the image attached when passing comments)

**return parameter**: the success field represents whether the comment is successful, if success is false, then the reason for the error in message. content is always the empty string



### SquareList

List the dynamic list of squares, tentatively 10 items per page (can be modified)

**Call address:** http://106././.79:40010/moment/squareList

**Interface request type**: GET

**Input parameter type**: 

query type parameter page, parameter type int

**return parameters**:

```json
"content": [
	{
	  "avatar": "string".
      "nickname": "string".
      "text_content": "string".
      "image": "string".
      "like_num": "int".
      "is_liked": "bool".
      "comment_num": "int".
      "view_num": "int".
      "created_time": "time".
      "moment_id": "int".
      "sender_id": "int".
      "is_follower": "bool"
	}
].
  "message": "search successfully".
  "success": true
```



## FollowedList

List the dynamic list of followed people, tentatively 10 items per page (can be modified)

**Call address:** http://106././.79:40010/moment/followedList

**Interface request type**: GET

**Input parameter type**: 

query type parameter page, parameter type int

**return parameters**:

```json
"content": [
	{
	  "avatar": "string".
      "nickname": "string".
      "text_content": "string".
      "image": "string".
      "like_num": "int".
      "is_liked": "bool".
      "comment_num": "int".
      "view_num": "int".
      "created_time": "time".
      "moment_id": "int".
      "sender_id": "int".
      "is_follower": "bool"
	}
].
  "message": "search successfully".
  "success": true
```



### CommentList

List the comments after clicking into the dynamic details

This part refers to soul's implementation, after entering the dynamic details, it shows all the dynamic comments, each comment only shows at most two subcomments ("sub_comment" array display), then you can enter the "more replies" interface, and then call the SubCommentList interface.

**Call address:** http://106././.79:40010/moment/CommentList

**Interface request type**: GET

query type parameter page, parameter type int

query type parameter moment_id, parameter type int

**return parameters**:

```json
"content": [
	{
	  "user_id": "int".
      "comment_id": "int".
      "nickname": "string".
      "avatar": "string".
      "text_content": "string".
      "image": "string".
      "created_time": "time".
      "sub_comment": [
      	{
      	  "user_id": "int".
          "comment_id": "int".
          "nickname": "string".
          "avatar": "string".
          "text_content": "string".
          "image": "string".
          "created_time": "time".
          "receiver_id": "int".
          "receiver_name": "string"
      	}.
        {
            ...
        }
      ]
	}.
    {
        ...
    }
].
"message": "search successfully".
"success": true
      
```



### SubCommentList

View the subcomments of a comment

The passed parameter believing_id is described in the comment interface above and represents the id of comment B

**Call address:** http://106././.79:40010/moment/SubCommentList

**interface request type**: GET

query type parameter page, parameter type int

query type parameter belonging_id, parameter type int

**return parameters**:

```json
"sub_comment". 
[
  {
	"user_id": "int".
    "comment_id": "int".
    "nickname": "string".
    "avatar": "string".
    "text_content": "string".
    "image": "string".
    "created_time": "time".
    "receiver_id": "int".
    "receiver_name": "string"
   }.
   {
   	...
   }
 ], the
  "message": "search successfully".
  "success": true
```
