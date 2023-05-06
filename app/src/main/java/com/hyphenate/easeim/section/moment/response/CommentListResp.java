package com.hyphenate.easeim.section.moment.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class CommentListResp {


    @JsonProperty("content")
    public List<Comment> content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


    public static class Comment {
        @JsonProperty("user_id")
        public Integer user_id;
        @JsonProperty("comment_id")
        public Integer comment_id;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("text_content")
        public String text_content;
        @JsonProperty("image")
        public String image;
        @JsonProperty("created_time")
        public String created_time;
        @JsonProperty("sub_comment")
        public Object sub_comment;
    }
}
