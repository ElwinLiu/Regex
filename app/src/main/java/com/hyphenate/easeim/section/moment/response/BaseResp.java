package com.hyphenate.easeim.section.moment.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResp {
    @JsonProperty("content")
    public String content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;
}
