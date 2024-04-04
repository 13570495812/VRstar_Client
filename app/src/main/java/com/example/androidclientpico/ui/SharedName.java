package com.example.androidclientpico.ui;

public enum SharedName {
    game_times("game_times");

    private final String value;

    //构造方法必须是private或者默认
    private SharedName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
