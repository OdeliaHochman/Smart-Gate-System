package com.example.smartgate.dataObject;


public class ModelVideo {

    String id,title,timestamp,videoUrl;

    public ModelVideo()
    {

    }

    public ModelVideo(String id, String title, String timestamp, String videoUrl)
    {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
