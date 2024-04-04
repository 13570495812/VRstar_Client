package com.example.androidclientpico.ui.home;

public interface IFragmentHomeCallback {
//    void listener(int position, String content);
    void onVideoClicksendMsgToActivity(String string);
    /**
     * fragment回调activity时间到
     * @param string
     */
    void sendMsgToActivity(String string);

    /**
     * 点击回调包名
     * @param string
     */
    void senMsgPackageName(String string);

    /**
     * home界面电量
     * @param mag
     * @return
     */
    String getMsgHomeQrFromActivity(String mag);  // home

    String getMsgHomeLanguageFromActivity(String mag);  // home

    /**
     * home 界面时间
     * @param time
     * @return
     */
    String getMsgHomeTimeFromActivity(String time);
    /**
     * home 是否在线
     * @param Online
     * @return
     */
    String getMsgHomeOnlineFromActivity(String Online);
}
