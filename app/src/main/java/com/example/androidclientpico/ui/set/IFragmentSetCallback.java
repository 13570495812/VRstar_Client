package com.example.androidclientpico.ui.set;

public interface IFragmentSetCallback {
    /**
     * fragment回调activity
     * @param string
     */
    void sendMsgToActivity(String string);

    /**
     * 设置界面语音
     * @param string
     * @return
     */
    void getMsgSetLanguageFromActivity(String string);
    /**
     * 密码输入错提醒框
     * @param
     */
    void sendMsgSetBackPas();

    /**
     * 密码输入错提醒框
     * @param
     */
    void sendMsgSetBackIP(String string);
    /**
     *
     * @param string
     */
    void sendMsgSetBack(String string);
    /**
     * 设置界面IP
     * @param Ip
     * @return
     */
    String getMsgSetIpFromActivity(String Ip);

    /**
     *
     * @param sn
     * @return
     */
    String getMsgSetSnFromActivity(String sn);

    /**
     * 设置界面二维码链接
     * @param QrCode
     * @return wifiConnect
     */

    String getMsgSetQrCodeFromActivity(String QrCode);
    /**
     * 切换语音默认
     * @param Language
     * @return wifiConnect
     */

    String getMsgLanguageFromActivity(String Language);
    /**
     * 地址
     * @param wifiConnect
     * @return
     */
    String getMsgSetwifiConnectFromActivity(String wifiConnect);

}
