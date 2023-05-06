package define

import "github.com/dgrijalva/jwt-go"

const Port = "40010"

const Username = "******"
const Password = "******"

//const Username = "debian-sys-maint"
//const Password = "xAftDU4Lgms6LLFJ"

const Dbname = "****"
const JwtSecret = "****"

//const BucketPath = "https://terminal-1304032890.cos.ap-nanjing.myqcloud.com"
//const SecretID = "AKIDwjL5btxnPnwsb5MPUL0TkodEfsrb6Zpq"
//const SecretKey = "sWw73lFVXO6apppcEeuynOlvsc8SYRTP"

const BucketPath = "****"
const DefaultAvatar = "****"
const SecretID = "****"
const SecretKey = "****"

const FollowerListPageSize = 15
const MomentPageSize = 10
const MomentCommentPageSize = 10
const MomentSubCommentPageSize = 15

type UserClaim struct {
	ID int `json:"id"`
	jwt.StandardClaims
}
