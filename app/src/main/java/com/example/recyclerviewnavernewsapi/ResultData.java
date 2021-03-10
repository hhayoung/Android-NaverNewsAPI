package com.example.recyclerviewnavernewsapi;

public class ResultData {

    private String tv_rank;
    private String tv_title;
    private String tv_content;
    private String tv_link;

    public ResultData(String tv_rank, String tv_title, String tv_content, String tv_link) {
        this.tv_rank = tv_rank;
        this.tv_title = tv_title;
        this.tv_content = tv_content;
        this.tv_link = tv_link;
    }

    public String getTv_rank() {
        return tv_rank;
    }

    public void setTv_rank(String tv_rank) {
        this.tv_rank = tv_rank;
    }

    public String getTv_title() {
        return tv_title;
    }

    public void setTv_title(String tv_title) {
        this.tv_title = tv_title;
    }

    public String getTv_content() {
        return tv_content;
    }

    public void setTv_content(String tv_content) {
        this.tv_content = tv_content;
    }

    public String getTv_link() {
        return tv_link;
    }

    public void setTv_link(String tv_link) {
        this.tv_link = tv_link;
    }
}
