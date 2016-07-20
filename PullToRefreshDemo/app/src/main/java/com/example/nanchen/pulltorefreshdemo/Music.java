package com.example.nanchen.pulltorefreshdemo;

/**
 * Created by 南尘 on 16-7-20.
 */
public class Music {

    private String title;
    private String singer;

    public Music() {
    }

    public Music(String title, String singer) {
        this.title = title;
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
