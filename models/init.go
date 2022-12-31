package models

import (
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
	"terminal/define"
)

var DB = Init()

func Init() *gorm.DB {
	dsn := define.Username + ":" + define.Password + "@tcp(localhost:3306)/" + define.Dbname + "?charset=utf8mb4&parseTime=True&loc=Local"
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})
	if err != nil {
		log.Printf("gorm New DB Error: %v", err)
		return nil
	}
	return db
}
