package com.example.androidclientpico.ui;

public enum ServerMSGType {
    //正常
    Normal(0),
    //握手
    Register(1),
    //播放
    Player_Action(2),
    //暂停或继续
    Player_PlayOrPause(3),
    //退出
    Player_Exit(4),
    //状态
    Player_Status(5),
    //发送动作
    SendingNow(6),
    // 客户端下线
    ClientNot(7),
    // 数据库信息
    SQLData(8);

    private int value = 0;

    private ServerMSGType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}

enum PlayerStatus {
    //未准备
    NOT_READY(0),
    //准备
    READY(1),
    //结束
    END(2),
    //播放中
    PLAYING(3),
    //暂停
    PAUSED(4),
    //停止
    STOPPED(5),
    //错误
    ERROR(6);

    private int value = 0;

    private PlayerStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}


