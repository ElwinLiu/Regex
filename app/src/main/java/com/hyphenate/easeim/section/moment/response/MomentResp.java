package com.hyphenate.easeim.section.moment.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MomentResp {


    @JsonProperty("content")
    public MomentListResp.Moment content;
    @JsonProperty("message")
    public String message;
    @JsonProperty("success")
    public Boolean success;


}
