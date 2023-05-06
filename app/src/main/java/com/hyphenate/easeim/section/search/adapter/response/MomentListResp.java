package com.hyphenate.easeim.section.search.adapter.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class MomentListResp {


    @JsonProperty("content")
    public List<Moment> content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


    public static class Moment implements Serializable {
        private static final long serialVersionUID = 1L;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("text_content")
        public String text_content;
        @JsonProperty("image")
        public String image;
        @JsonProperty("like_num")
        public Integer like_num;
        @JsonProperty("is_liked")
        public Boolean is_liked;
        @JsonProperty("comment_num")
        public Integer comment_num;
        @JsonProperty("view_num")
        public Integer view_num;
        @JsonProperty("created_time")
        public String created_time;
        @JsonProperty("moment_id")
        public Integer moment_id;
        @JsonProperty("sender_id")
        public Integer sender_id;
        @JsonProperty("is_follower")
        @JsonAlias(value = "is_followed")
        public Boolean is_follower;
    }
}
