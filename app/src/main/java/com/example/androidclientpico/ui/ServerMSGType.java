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
    Player_Status(5);


    private int value = 0;

    private ServerMSGType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}


