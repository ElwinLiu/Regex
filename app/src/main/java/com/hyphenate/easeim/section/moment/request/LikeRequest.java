package com.hyphenate.easeim.section.moment.request;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LikeRequest {
    @JsonProperty("like_status")
    public Boolean like_status;
    @JsonProperty("moment_id")
    public Integer moment_id;
}
