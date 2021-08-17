package com.example.smartgate.dataObject;


public class ModelVideo {

    String id,timestamp,videoUrl;

    public ModelVideo()
    {

    }

    public ModelVideo(String id, String timestamp, String videoUrl)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
