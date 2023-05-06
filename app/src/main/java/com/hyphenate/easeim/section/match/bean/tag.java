package com.hyphenate.easeim.section.match.bean;

public class tag {
    private Integer id;
    private String tag_content;


    public tag(Integer id, String tag_content) {
        this.id = id;
        this.tag_content = tag_content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag_content() {
        return tag_content;
    }

    public void setTag_content(String tag_content) {
        this.tag_content = tag_content;
    }

    @Override
    public String toString() {
        return "tag{" +
                "id=" + id +
                ", tag_content='" + tag_content + '\'' +
                '}';
    }
}
