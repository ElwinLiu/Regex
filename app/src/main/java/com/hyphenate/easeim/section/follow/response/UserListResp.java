package com.hyphenate.easeim.section.follow.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class UserListResp {


    @JsonProperty("content")
    public List<User> content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


    public static class User {
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("momentNum")
        public Integer momentNum;
        @JsonProperty("subscribe_status")
        public Boolean subscribe_status;
        @JsonProperty("created_time")
        public String created_time;
    }
}
