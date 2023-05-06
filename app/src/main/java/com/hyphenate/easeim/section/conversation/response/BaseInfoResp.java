package com.hyphenate.easeim.section.conversation.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class BaseInfoResp {


    @JsonProperty("content")
    public BaseInfo content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


    public static class BaseInfo {
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("nickname")
        public String nickname;
    }
}
