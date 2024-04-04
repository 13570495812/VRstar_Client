package com.example.androidclientpico.ui;

import static android.content.ContentValues.TAG;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketConnect {
    private Socket socket;
    private String sendMsg = "";
    boolean isConnected = false;
    boolean isConnect = false;
    boolean isRegister = false;
    boolean isRegisterOK = false;
    Handler handler = new Handler();
    //利用HandlerThread处理异步消息，Activity销毁的时候回收
    private Handler mHandler;
    private String HeadMsg;

    public SocketConnect() {
        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    /**
     * 建立服务端连接
     */
    public void conn() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        socket = new Socket(MainActivity.SERVER_IP, MainActivity.SERVER_PORT);
                        InputStream inputStream = socket.getInputStream();
                        DataInputStream input = new DataInputStream(inputStream);
//                        String msga = input.readUTF();
                        isConnect = true;
                        byte[] b = new byte[1024];
                        while (isConnect) {
                            try {
                                int length = input.read(b);
                                String Msg = new String(b, 0, length, "UTF8");
                                if (!Msg.contains("{") && !Msg.contains("}")) {
                                    String[] msgArray = Msg.split(";");
                                    String c= msgArray[0];

                                    String d= msgArray[1];
//                                    String f= msgArray[2];
//                                    String s= msgArray[3];
//                                    Log.e(TAG,"我是00000"+c);
//                                    Log.e(TAG,"我是11111"+d);
//                                    Log.e(TAG,"我是2222"+f);
//                                    Log.e(TAG,"我是333"+s);

                                    Pattern p = Pattern.compile("[0-9]*");
                                    Matcher m = p.matcher(c);

                                    try {
                                        Message message = new Message();
                                        message.what = Integer.valueOf(msgArray[1]);
                                        message.obj = msgArray;
                                        if (message.what == 2) {
                                            MainActivity.mainActivity.handler.sendMessageDelayed(message, Long.valueOf(msgArray[6]));
                                        } else {
                                            MainActivity.mainActivity.handler.sendMessage(message);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            } catch (IOException e) {
                                isConnect = false;
                            }
                        }
                    } catch (IOException e) {
                        isConnect = false;
                    }
                }
            }
        }.start();

    }
    /**
     * 发送消息
     */
    public void SendToServer(final String MSG) {
        if (isConnect) {
            this.HeadMsg = MSG;
            new Thread() {
                @Override
                public void run() {
                    try {
                        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                        writer.writeBytes(" "+ HeadMsg);

                       /* Log.e("writeUTF"," --------------------------------writeUTF------------------0"+HeadMsg);
                        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                        writer.writeUTF(MSG); // 写一个UTF-8的信息
                        System.out.println("发送消息");*/
                    } catch (IOException e) {
                        isConnect = false;
                    }
                }
            }.start();
        }
    }
    public void onDestroy(){
//        Log.d(MainActivity.mainActivity.TAG, "SocketClient onDestroy: ");
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages("");
        }
    }

}
