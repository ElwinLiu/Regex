package define

import "github.com/dgrijalva/jwt-go"

const Port = ""******************""

const Username = "******"
const Password = "******"

//const Username = "debian-sys-maint"
//const Password = ""******************""

const Dbname = "****"
const JwtSecret = "****"

//const BucketPath = "********************"
//const SecretID = "********************"
//const SecretKey = ""******************""

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
