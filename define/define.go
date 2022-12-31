package define

import "github.com/dgrijalva/jwt-go"

const Port = ""******************""

const Username = "root"
const Password = ""******************""

//const Username = "debian-sys-maint"
//const Password = ""******************""

const Dbname = "terminal"
const JwtSecret = "terminal-key"

//const BucketPath = "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com"
//const SecretID = "AKIDwjL5btxnPnwsb5MPUL0TkodEfsrb6Zpq"
//const SecretKey = ""******************""

const BucketPath = "https://terminal-bucket-1314182456.cos.ap-nanjing.myqcloud.com"
const DefaultAvatar = "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com/terminal/default.png"
const SecretID = "AKIDmQsEQahgXCev2va2whDYgbR6Zq590d8P"
const SecretKey = "******************"

const FollowerListPageSize = 15
const MomentPageSize = 10
const MomentCommentPageSize = 10
const MomentSubCommentPageSize = 15

type UserClaim struct {
	ID int `json:"id"`
	jwt.StandardClaims
}
