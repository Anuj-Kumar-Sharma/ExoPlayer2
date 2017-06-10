package com.example.anujsharma.yoblunt_exoplayer2.dataStructures;

import android.graphics.Bitmap;

public class VideoData {

    private String url, date, duration, caption;
    private Bitmap videoFrame;

    public VideoData(String url, String date, String duration, Bitmap videoFrame, String caption) {
        this.url = url;
        this.date = date;
        this.duration = duration;
        this.caption = caption;
        this.videoFrame = videoFrame;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String time) {
        this.duration = time;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Bitmap getVideoFrame() {
        return videoFrame;
    }

    public void setVideoFrame(Bitmap videoFrame) {
        this.videoFrame = videoFrame;
    }
}
