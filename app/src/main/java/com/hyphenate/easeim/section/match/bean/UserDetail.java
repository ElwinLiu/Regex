package com.hyphenate.easeim.section.match.bean;



//用户的详细信息
public class UserDetail {


    private Integer id;
    private String avatar;
    private String nickname;
    private tag tags;
    private Integer follower_num;
    private Integer subscribed_num;
    private Boolean subscribe_status;
    private String created_time;

    public UserDetail(Integer id, String avatar, String nickname, tag tags, Integer follower_num, Integer subscribed_num, Boolean subscribe_status, String created_time) {
        this.id = id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.tags = tags;
        this.follower_num = follower_num;
        this.subscribed_num = subscribed_num;
        this.subscribe_status = subscribe_status;
        this.created_time = created_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public tag getTags() {
        return tags;
    }

    public void setTags(tag tags) {
        this.tags = tags;
    }

    public Integer getFollower_num() {
        return follower_num;
    }

    public void setFollower_num(Integer follower_num) {
        this.follower_num = follower_num;
    }

    public Integer getSubscribed_num() {
        return subscribed_num;
    }

    public void setSubscribed_num(Integer subscribed_num) {
        this.subscribed_num = subscribed_num;
    }

    public Boolean getSubscribe_status() {
        return subscribe_status;
    }

    public void setSubscribe_status(Boolean subscribe_status) {
        this.subscribe_status = subscribe_status;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }


    @Override
    public String toString() {
        return "UserDetail{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", tags=" + tags +
                ", follower_num=" + follower_num +
                ", subscribed_num=" + subscribed_num +
                ", subscribe_status=" + subscribe_status +
                ", created_time='" + created_time + '\'' +
                '}';
    }
}
