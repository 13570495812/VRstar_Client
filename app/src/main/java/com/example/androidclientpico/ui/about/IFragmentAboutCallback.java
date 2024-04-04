package com.example.androidclientpico.ui.about;

public interface IFragmentAboutCallback {

    void onLocateClicksendAboutActivity();
    /**
     * fragment回调activity时间到
     * @param string
     */
    void sendAboutActivity(String string);
}
