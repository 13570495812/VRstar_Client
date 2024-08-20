package com.example.androidclientpico.ui;

//import static com.example.androidclientpico.ui.MainActivity.DATA_HOT;
//import static com.example.androidclientpico.ui.MainActivity.DATA_TIME;
//import static com.example.androidclientpico.ui.MainActivity.END_CODE;
//import static com.example.androidclientpico.ui.MainActivity.Free_Not;
//import static com.example.androidclientpico.ui.MainActivity.In_Game;
//import static com.example.androidclientpico.ui.MainActivity.SK_CONNECTED;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class TcpManager {

    private static final String TAG = TcpManager.class.getSimpleName();
    /*socket*/
    private Socket socket;
    /*连接线程*/
    private Thread connectThread;
    /* 发送输出流*/
    private OutputStream outputStream;
    /* 读写输入流*/
    private InputStream inputStream;
    private DataInputStream dis;
    /* 线程状态，安全结束线程*/
    private  boolean threadStatus =false;
    /* 读取保存进数组*/
    byte buff[]  = new byte[1024*1024*2];
    private String ip;
    private String port;
    private Handler handler = new Handler(Looper.getMainLooper());
    /*默认重连*/
    private boolean isReConnect = true;
    /*倒计时Timer发送心跳包*/
    private Timer timer;
    private TimerTask task;

    /* 心跳周期(s)*/
    private int heartCycle = 6;
    /*接收数据长度*/
    private int rcvLength;
    /*接收数据*/
    private String rcvMsg;
    private byte[] data;
    MainActivity mainActivity = new MainActivity();
    PersonM_T personM_t = new PersonM_T();

    public TcpManager(){}
    private static TcpManager instance;
    public static synchronized TcpManager getInstance(){
        if(instance==null){
            synchronized (TcpManager.class){
                instance=new TcpManager();
            }
        }
        return instance;
    }

    public TcpManager initSocket(final String ip, final String port) {
        this.ip=ip;
        this.port=port;
        /* 开启读写线程*/
        threadStatus =true;
        new ReadThread().start();

        if (socket == null && connectThread == null) {
            connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    socket = new Socket();
                    try {
                        /*超时时间为2秒*/
                        socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 2000);
                        /*连接成功的话  发送心跳包*/
                        if (socket.isConnected()) {
                            inputStream = socket.getInputStream();
                            dis = new DataInputStream(inputStream);
                            /*因为Toast是要运行在主线程的  这里是子线程  所以需要到主线程哪里去显示toast*/
                            Log.e(TAG, "服务连接成功");
                            /*发送连接成功的消息*/

                            if(onSocketStatusListener!=null)
                                onSocketStatusListener.onConnectSuccess();
                            /*发送心跳数据*/
                            sendBeatData();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            Log.e(TAG,"连接超时，正在重连");
                            releaseSocket();
                        } else if (e instanceof NoRouteToHostException) {
                            Log.e(TAG,"该地址不存在，请检查");
                        } else if (e instanceof ConnectException) {
                            Log.e(TAG,"连接异常或被拒绝，请检查");
                            /*重连*/
//                            releaseSocket();

                        }
                    }
                }
            });
            /*启动连接线程*/
            connectThread.start();
        }
        return this;
    }

    /*发送数据*/
    public void sendData(final String data) {
        if (socket != null && socket.isConnected()) {
            /*发送指令*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();
                        if (outputStream != null) {
//                            outputStream.write(data);
                            outputStream.write((data).getBytes("UTF-8"));
                            outputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } else {
            Log.e(TAG,"socket连接错误,请重试");
        }
    }
    public static byte[] intToBytes2(int n){
        byte[] b = new byte[4];
        for(int i = 0;i < 4;i++){
            b[i] = (byte)(n >> (24 - i * 8));
        }
        return b;
    }
    /*定时发送数据*/
    private void sendBeatData() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
//                        getInstance();
                        outputStream = socket.getOutputStream();
//                        Log.e(TAG,""+instance);
                        Log.e(TAG,"发送心跳包");
//                        Log.e("发送心跳包9999", personM_t.getSK_CONNECTED());

                        String dataw= mainActivity.DATA_HOT+";"+mainActivity.Free_Not+";"+mainActivity.In_Game+";"+
                                mainActivity.DATA_TIME+";"+mainActivity.SK_CONNECTED+";"+mainActivity.PackName+";"+mainActivity.END_CODE;
//                        data = new byte[]{0x01,0x01,0x};
//                        byte[] bytes = ByteBuffer.allocate(4).putInt(number).array();
//
//                        byte[] byteArray = new byte[bytes.length];
//                        int i = 0;
//
//                        for (byte b : bytes) {
//
//                            System.out.print(Integer.toHexString(b & 0xFF) + " ");
//                            byteArray[i]=b;
//                            i++;
//                        }
//                      int a = 352445;
//                        byte bytew = intToByte(a);
//                        System.out.println("bytewbytewbytew "+bytew);
//                       int w =   byteToInt(bytew);
//                        System.out.println("wwwwwwwwwwwwwwwwwww "+w);
//                        String number = "23587";

//                        System.out.println("十进制 "+ (6+number.getBytes().length));



//                        System.out.println("十进制gg "+ srtbyte.length);
//                        byte[] a = Integer.toString(srtbyte.length).getBytes("UTF-8");
//                        int f = 9;
//                        byte byte4 = intToByte(srtbyte.length);
//                        System.out.println("十进制b.length "+byte0);
//                        System.out.println("十进制b.length "+byte0);
//                        System.out.println("十进制a.length "+a.length);
//                        System.out.println("十进制a.length "+a[0]);

//                        String resa = new String(a,"UTF-8");

//                        System.out.println("十进制resa "+resa);
//                        srtbyte = number.getBytes("UTF-8");
//                        srtbyte[0]="0x01";
//                        srtbyte[1]="0x00";
//                        srtbyte[2]="0x01";
//                        srtbyte[3]="6";
//                        srtbyte[4]="323135";
//                        srtbyte[5]="0X99";

//                        for (byte b : srtbyte) {
//
////                            Log.e("eoru",""+index);
//
//                            Log.e("我是99",""+b);
//                        }
//                        String hexData = "01 00 03 09";  // 示例的十六进制数据字符串
//                        byte[] data = hexStringToByteArray(hexData);

//                        data = new byte[]{0x00,0x00,0x5c,srtbyte,0x23};


                        /*这里的编码方式根据你的需求去改*/
//                        byte[] a = intToBytes2(33664);0
//                        for (int i = 0; i < a.length; i++) {
//                            Log.d("Fruit", ""+a[i]);
//                        }


//                        StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < srtbyte.length; i++) {
//                            if (i > 0) {
//                                sb.append(" ");
//                            }
//                            sb.append(srtbyte[i]);
//                        }
//                        String joinedString = sb.toString();
//                        Log.e("srtbyte[srtbyte.length-1]=0x11;",joinedString);
//                        byte[] bytes = hexStringToByteArray(joinedString);
//                        byte[] bytes = srtbyte.getBytes();
                        outputStream.write(dataw.getBytes());
//                        outputStream.write(("test\n").getBytes("UTF-8"));
                        outputStream.flush();
                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        Log.e(TAG,"连接断开，正在重连");

                        /*重连*/
                        releaseSocket();
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 1000*heartCycle);
    }

    public void a(String a){
        System.out.println( "qqqqqqqqq"+a );
//        Log.e(TAG,a);
    }
    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }
    //测试 byte 转 int
    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
//        public static byte[] hexStringToByteArray(String hexString) {
//        hexString = hexString.replaceAll(" ", "");  // 去除空格
//        int len = hexString.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
//                    + Character.digit(hexString.charAt(i + 1), 16));
//        }
//        return data;
//    }
    /*释放资源*/
    private  void releaseSocket(){
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream=null;
        }
        if(dis!=null){
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dis=null;
        }
        if (socket != null) {
            try {
                socket.close();

            } catch (IOException e) {
            }
            socket = null;
        }
        if (connectThread != null) {
            connectThread = null;
        }
        /*重新初始化socket*/
        if (isReConnect) {
            initSocket(ip,port);
        }
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (threadStatus) {
                if (inputStream != null) {
                    try {
                        rcvLength = dis.read(buff);
                        if (rcvLength > 0) {
                            rcvMsg = new String(buff, 0, rcvLength, "GBK");
//                            rcvMsg = rcvMsg.substring(0,rcvMsg.length()-1);
//                            System.out.print("s =" + rcvMsg);
                            //接收到数据，切换主线程，显示数据
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("112",rcvMsg);
//                                    String[] msgArray0 = rcvMsg.split("|");
//                                    Log.e("rrrrr",""+msgArray0.length);

//                                    Log.e("msgArray",""+msgArray.length);

                                    mainActivity.socket_data_processing(rcvMsg);
//
//                                    if(onReceiveDataListener!=null){
//                                        onReceiveDataListener.onReceiveData(rcvMsg);
//                                    }

                                }
                            });

                        }
                    } catch (Exception e) {
                        Log.e(TAG,"接收总控数据异常");
                    }
                }

            }
        }
    }

    public interface OnSocketStatusListener{
        void onConnectSuccess();
    }

    public OnSocketStatusListener onSocketStatusListener;

    public void setOnSocketStatusListener(OnSocketStatusListener onSocketStatusListener) {
        this.onSocketStatusListener = onSocketStatusListener;
    }

    public interface OnReceiveDataListener{
        void onReceiveData(String str);
    }

    public OnReceiveDataListener onReceiveDataListener;

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }
}