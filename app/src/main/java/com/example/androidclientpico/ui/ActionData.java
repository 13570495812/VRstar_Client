package com.example.androidclientpico.ui;

public class ActionData {
    public ActionData() {

    }
    String uri;
    //视频地址
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    String title;
    //视频名称
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    String videotype;
    //视频类型
    public String getVideotype() {
        return videotype;
    }
    public void setVideotype(String videotype) {
        this.videotype = videotype;
    }

    String Videowidth;
    //视频宽度
    public String getVideowidth() {
        return Videowidth;
    }
    public void setVideowidth(String videowidth) {
        Videowidth = videowidth;
    }

    String VideoHeigth;
    //视频高度
    public String getVideoHeigth() {
        return VideoHeigth;
    }
    public void setVideoHeigth(String videoHeigth) {
        VideoHeigth = videoHeigth;
    }

    String playTime;
    //视频起点
    public String getPlayTime() {
        return playTime;
    }
    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    int autoPlay;
    //是否自动播放 1自动 0第一帧等待
    public int getAutoPlay() {
        return autoPlay;
    }
    public void setAutoPlay(int autoPlay) {
        this.autoPlay = autoPlay;
    }
}

