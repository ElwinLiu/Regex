package com.hyphenate.easeim.section.moment.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class FollowResp {
    @JsonProperty("content")
    public String content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;

}
