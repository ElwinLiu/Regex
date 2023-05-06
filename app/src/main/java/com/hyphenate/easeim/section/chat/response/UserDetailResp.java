package com.hyphenate.easeim.section.chat.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class UserDetailResp {
    @JsonProperty("content")
    public UserDetail content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


    public static class UserDetail {
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("tags")
        public List<Tag> tags;
        @JsonProperty("follower_num")
        public Integer follower_num;
        @JsonProperty("subscribed_num")
        public Integer subscribed_num;
        @JsonProperty("is_followed")
        public Boolean is_followed;
        @JsonProperty("match_rate")
        public Integer match_rate;
        @JsonProperty("created_time")
        public String created_time;


        public static class Tag {
            @JsonProperty("id")
            public Integer id;
            @JsonProperty("tag_content")
            public String tag_content;
        }
    }
}
