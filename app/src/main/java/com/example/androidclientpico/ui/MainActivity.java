package com.example.androidclientpico.ui;

import static android.provider.Telephony.Mms.Part.FILENAME;

import static com.pvr.tobservice.enums.PBS_HomeEventEnum.SINGLE_CLICK_HMD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidclientpico.R;
import com.example.androidclientpico.ui.about.AboutFragment;
import com.example.androidclientpico.ui.about.IFragmentAboutCallback;
import com.example.androidclientpico.ui.action.AcitonFragment;
import com.example.androidclientpico.ui.home.HomeFragment;
import com.example.androidclientpico.ui.home.IFragmentHomeCallback;
import com.example.androidclientpico.ui.set.IFragmentSetCallback;
import com.example.androidclientpico.ui.set.QRCodeUtil;
import com.example.androidclientpico.ui.set.SetFragmint;
import com.example.androidclientpico.util.ACache;
import com.pvr.tobservice.ToBServiceHelper;
import com.pvr.tobservice.enums.PBS_DeviceControlEnum;
import com.pvr.tobservice.enums.PBS_HomeEventEnum;
import com.pvr.tobservice.enums.PBS_HomeFunctionEnum;
import com.pvr.tobservice.enums.PBS_PICOCastUrlTypeEnum;
import com.pvr.tobservice.enums.PBS_SystemInfoEnum;
import com.pvr.tobservice.enums.PBS_VideoPlayMode;
import com.pvr.tobservice.interfaces.IBoolCallback;
import com.pvr.tobservice.interfaces.IIntCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    /*handler*/
    private Handler mHandler;

    public static boolean isTime = false;
    public static final int MSG_ONE = 0;

    public static final int MSG_TOW = MSG_ONE + 1;

    private Timer timer = null;//计时器
    private TimerTask task = null;

    private int i = 0;

    private int TIME = 1000;

    public static MainActivity mainActivity = null;
    private MainActivity myApplicaton;
    /**
     * 连接服务器
     */
    private static int pid = -1;

    /**  连接服务器 */

    /**
     * sn序列号
     */
    public static String SN_Number = "";

    public static String videos = "/storage/emulated/0/Android/jamma/vide/1.mp4";
    /**
     * sn序列号
     */
    public static String Quantity_Number;
    public static String textBol;
    /**
     * 服务器IP
     */
    public static String SERVER_IP = "";
    //    public static String SERVER_IP = "120.77.214.227";
//    1;PA7L10MGG8080274W;false|
    public static String MY_IP = null;
    public static String txtConnectInfo = "Offline";
    /**
     * 端口
     */
    public static int SERVER_PORT = 3217;

    /**
     * 二维码字符内容
     */
    private String QRcontent;
    /**
     * 时间
     */
    public static int PayTime = 20000;
    /**
     * 音量
     */
    AudioUtil audioUtil;
    private Activity mActivityContant;
    /**
     * ip地址文件名
     */
    String FILENAME = "JAMMA.txt";
    /**
     * 语音文件名
     */
    String LanguageFILENAME = "Language.txt";
    public static String Language = "";
    /**
     * 是否免费模式文件名
     */


    /**
     * 是否游戏中
     */
    public static boolean isGameing = false;
    public static boolean one = false;
    /**
     * 是否倒计时
     */
    public static boolean countdown = false;
    /**
     * 是否是游戏
     */
    public static boolean isGame = false;
    /**
     * 收到游戏时间
     */
    public static String StringTimes = "";
    /**
     * 前台服务
     */
    Intent intentService;
    /**
     * MAC序列号
     */
    public static String MAC_Address = "";
    public static MainActivity M_this;
    /**
     * 游戏倒计时时间
     */
    public static int CountDownGameTimes = 0;
    public static String DownGameTimes = "00:00:00";
    private IFragmentHomeCallback fragmentCallback;
    /**
     * APP包名
     */
    public static String PackName = "";
    /**
     * 连接服务器
     */
    public static String wifiConnectAdrss = "";

    private String wifiConfig;
    SocketConnect socketConnect = new SocketConnect();
    WifiConnect wifiConnect;
    /**
     * fragments
     */
    Fragment[] fragments = new Fragment[]{null, null, null, null}; //存放4个控件
    private LinearLayout llConent;
    private RadioButton rbHome;
    private RadioButton rbAbout;
    private RadioButton rbAction;
    private RadioButton rbSet;
    private HomeFragment bf;
    private SetFragmint sf;
    private ACache mCache; // 缓存
    MyCountDownTimer myCountDownTimer;
    private AboutFragment af;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
    private String wifi_name;
    private Read_coin_file file_mode;

    // 检查权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d(TAG, "onCreate");
//        获取控件
        llConent = findViewById(R.id.ll_main_content);
        rbHome = findViewById(R.id.rb_main_home);
        rbAbout = findViewById(R.id.rb_main_about);
        rbAction = findViewById(R.id.rb_main_action);
        rbSet = findViewById(R.id.rb_main_set);

        /**
         * frament 全参数
         */


        /**
         * 导入是否免费模式
         */
        file_mode = new Read_coin_file();

        /**
         * 初始化
         */
        Init();
        M_this = this;
        /**
         * 默认选择
         */
        setViewChoos();
        /**
         * 懒加载
         */
        setViewEventListener();
        /**
         * 获取电量信心
         */
        getSN_Quantity();

        //Density.setDensity(getApplication(),this);
    }

    private void Init() {
        mainActivity = this;
        /**
         *   绑定ToBService
         */
        ToBServiceHelper.getInstance().bindTobService(mainActivity);
        /**
         * 权限动态获取
         */
        checkPermission();
        /**
         * 获取服务器IP
         */
        SERVER_IP = GetServerIP();
//        SERVER_IP = "192.168.188.186";
        /*语言选择*/
        Language = GetLanguage();
        if (Language.equals("1")) {
            rbHome.setText("首页");
            rbAbout.setText("关于");
            rbAction.setText("活动");
            rbSet.setText("设置");

        } else {
            rbHome.setText("Home");
            rbAbout.setText("About");
            rbAction.setText("Activities");
            rbSet.setText("Setting");
        }
        /** 音量设置 */
        audioUtil = new AudioUtil(getApplicationContext());
        audioUtil.setMediaVolume(+100);
        /**
         * 获取MAC_Address
         */
        MAC_Address = getmacAddress();
        /**
         * socketConnect
         */
        socketConnect.conn();
        intentService = new Intent(MainActivity.this, MyService.class);//前台服务
        startService(intentService);
        /**
         * handlerThread
         */

        new Thread(new ThreadShow()).start();

        /**
         * 获取IP
         */
        wifiConnect = new WifiConnect(this);
        wifiConnectAdrss = wifiConnect.getLocalIpAddress(this);

        /** 获取缓存时间 */
//        mCache = ACache.get(this);
//        mCache.put("TimeCache", Integer.toString(CountDownGameTimes));
//        String GetTime = mCache.getAsString("TimeCache");
//        if (GetTime.equals("0") || GetTime == null) {
////            myCountDownTimer.cancel();
//        } else {
////            myCountDownTimer = new MyCountDownTimer(Integer.parseInt(GetTime), 1000);
////            myCountDownTimer.start();
//        }
    }

    /**
     * 主线程
     */
    public Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String[] strArray = (String[]) msg.obj;
            Log.e(TAG,"==="+msg.what);
            switch (msg.what) {
                case 0:
                    if (!isGameing) {
                        txtConnectInfo = "Offline";
                        bf.OnlineHome();
                        mainActivity.socketConnect.isRegister = false;
                        mainActivity.ShakeHands();
                    }
                    break;
                case 1:  //** 上线 *//*
                    Log.d(TAG,"--------------------------------1 我在线------------------"+isGameing);
                    Log.d(TAG,"--------------------------------1 我在线------------------"+mainActivity.socketConnect.isRegister);
//                    if (!mainActivity.socketConnect.isRegister) {
                        try {
                            mainActivity.QRcontent = strArray[1];
                            mainActivity.socketConnect.isRegister = true;
                            txtConnectInfo = "Online";
                            bf.OnlineHome();
//                            mainActivity.SendMessage("1", true);
                            bf.quantityHome(); //电量
                            Log.d(TAG,"--------------------------------我改咕咕咕咕------------------"+mainActivity.socketConnect.isRegister);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    }
                    break;
                case 2: //** 服务器发生投币器信号发生*//*
//                    Log.d(TAG,"--------------------------------2 收到投币信息------------------"+isGameing);
                    PackName = strArray[1];
                    StringTimes = strArray[2];
//                    }
                    break;
                case 3:
                    //** 发送时间过来APK //mainActivity.PlayGame("com.example.xch.generateqrcode"); *//*
                    mainActivity.ScreenOn();
                        try {
//                            Log.d(TAG,"--------------------------------3 上线------------------"+isGameing);
//                            PackName = strArray[1];
                            StringTimes = strArray[3];
                            if (!isTime) {
                                myCountDownTimer = new MyCountDownTimer( Integer.parseInt(StringTimes), 1000);
                                myCountDownTimer.start();
                                isTime=true;
                            } else {
//                                Log.d("TAG","===========第二次次========="+isTime);
                                myCountDownTimer.cancel();
                                myCountDownTimer = new MyCountDownTimer(Integer.parseInt(StringTimes), 1000);
                                myCountDownTimer.start();
                            }
                        } catch (Exception e) {
                        }
                    break;
                case 4: //** apk杀进程 *//*
                    try {
                        PackName = strArray[1];
                        mainActivity.Neo3KillApps(null, new String[]{PackName}, 0);

                        isGame = false;
                        isGameing = false;
                        mainActivity.socketConnect.isRegister = false;
                        mainActivity.SendMessage("4", true);
                        one = true;
                    } catch (Exception e) {
                        AppContext.restartApp();
                    }
                    break;
                case 5://点击游戏打开
                    mainActivity.ScreenOn();  // 点击打开游戏屏幕自动亮

//                    if (strArray[1].equals("Status"))
                        Log.d(TAG,"--------------------------------5 点击打开游戏 ------------------"+isGameing);
//                        if(!PackName.equals("")){
//                            mainActivity.Neo3KillApps(null, new String[]{PackName}, 0);
//                        }
//                    mainActivity.CurrentStatus();
                    PackName = strArray[2];
                    StringTimes = strArray[3];
//                    Log.e(TAG,"我是时间"+ StringTimes);
//                    Log.e(TAG,"我是apk报名"+ PackName);
                    PackageManager packageManager = getPackageManager();
                    Intent intent=new Intent();
                    intent =packageManager.getLaunchIntentForPackage(PackName);
                    if(intent==null){
                        Log.e(TAG,"我是没有安装"+ PackName);
                    }else{
                        startActivity(intent);
                    }
                    break;
                case 6://重啓
                    if (strArray[1].equals("Restart"))
//                        Log.d(TAG,"--------------------------------6 重啓 ------------------"+isGameing);
                        SharedPreferencesUtil.getInstance(getApplicationContext()).setInt(SharedName.game_times.getValue(), CountDownGameTimes);
                    mainActivity.SendMessage("6", true);
                    AppContext.restartApp();
                    break;
                case 7://關機
                    if (strArray[1].equals("Shutdown")) {
//                        Log.d(TAG,"--------------------------------7 重啓 ------------------"+isGameing);
                        mainActivity.SendMessage("7", true);
                        mainActivity.androidShutDown();
                    }
                    break;
                case 96: //** apk杀进程 *//*
                    try {
//                        Log.d(TAG,"--------------------------------96 apk杀进程 ------------------"+isGameing);
//                        mainActivity.Neo3KillApps(null, new String[]{PackName}, 0);
//                        SharedPreferencesUtil.getInstance(getApplicationContext()).setInt(SharedName.game_times.getValue(), CountDownGameTimes);
//                        Thread.sleep(1000);
//                        AppContext.restartApp();
                    } catch (Exception e) {
                    }
                    break;
                case 97://亮屏
                    mainActivity.ScreenOn();
                    break;
                case 98://熄屏
                    mainActivity.ScreenOff();
                    break;
                case 99:
//                    Log.d(TAG,"--------------------------------0 999------------------"+msg.what);
                    break;
                case 100:
                    mainActivity.getSN_Quantity();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 线程类
     */
    class ThreadShow implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    //开启倒计时
                    if (countdown) {
//                        mainActivity.TimeOut();
                    }
                    if (isGameing) { //保持游戏中亮屏
                        Message msg = new Message();
                        msg.what = 97;
                        MainActivity.mainActivity.handler.sendMessage(msg);
                    }
                    if (!isGameing && one) {
                        one = false;
                        Message msg = new Message();
                        msg.what = 0; //握手
                        MainActivity.mainActivity.handler.sendMessage(msg);
                    }
                    Message msg = new Message();
                    if (!socketConnect.isRegister) { //如果没有注册，一直发握手注册，
                        msg.what = 0; //握手
                        MainActivity.mainActivity.handler.sendMessage(msg);
                    } else {
                        msg.what = 1; //已经注册
                        MainActivity.mainActivity.handler.sendMessage(msg);
                        mainActivity.socketConnect.SendToServer(" 1;" + SN_Number + ";true");
                        Thread.sleep(1000 * 5);
                    }
                    Thread.sleep(3000);
                    mainActivity.getSN_Quantity();
                } catch (Exception e) {
                }
            }
        }
    }
    /*字线程*/
    /**
     * 发送
     */
    /**
     * 打开播放器
     */
    private void openVideos(String urls) {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsPlayerOpenPlayer(urls);
            ToBServiceHelper.getInstance().getServiceBinder().pbsPlayerSetPlayerMode(PBS_VideoPlayMode.VideoType_360_2D);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 刷新
     */

    private void serv() {
        initPicoCast();
        try {

            String RtmpURL = ToBServiceHelper.getInstance().getServiceBinder().pbsPicoCastGetUrl(PBS_PICOCastUrlTypeEnum.RTMP_URL,1);
            Log.d(TAG,RtmpURL);
//            normal_url.setText("normal_url : " + url);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "================我已经执行了");
//        videos

        //启动视频播放器播放视频

        //        String json = JsonUtils.loadJSONFromAsset(this, "json/newdata.json");
//        if (json != null) {
//            JsonUtils.parseJSON(json);
//        }
//        Init();
//        CurrentStatus();
//        getSN_Quantity();

//        Log.d(TAG,"我是刷新Intel");

//        Message msg = new Message();
//        msg.what = 0; //握手
//        MainActivity.mainActivity.handler.sendMessage(msg);

//        AppContext.restartApp();
//        ShakeHands();

//        Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();

//        Log.d(TAG,"----------------wos----------------");
//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setTitle("请稍后");
//        dialog.setMessage("正在加载中");
//        dialog.show();
    }

    public void initPicoCast() {
        try {
            int result = ToBServiceHelper.getInstance().getServiceBinder().pbsPicoCastInit(callback, 0);
            Log.i("PicoVRIoTService -1", "init_pico_cast : " + result);
//            init_pico_cast.setText("init_pico_cast : " + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IIntCallback callback = new IIntCallback.Stub() {
        @Override
        public void callback(int result) throws RemoteException {
//            runOnUiThread(() -> init_pico_cast.setText("init_pico_cast connect : " + result));
            Log.i("PicoVRIoTService -1", "init_pico_cast connect callback : " + result);
        }
    };


    /**
     * 获取sh
     *
     * @param
     */
    private void getSN_Quantity() {
        ToBServiceHelper.getInstance().bindTobService(getApplicationContext(), new ToBServiceHelper.BindCallBack() {
            @Override
            public void bindCallBack(Boolean status) {
                try {
                    SN_Number = ToBServiceHelper.getInstance().getServiceBinder().pbsStateGetDeviceInfo(PBS_SystemInfoEnum.EQUIPMENT_SN, 0);//getPUI_EQUIPMENT_SN();
                    Quantity_Number = ToBServiceHelper.getInstance().getServiceBinder().pbsStateGetDeviceInfo(PBS_SystemInfoEnum.ELECTRIC_QUANTITY, 0);
//                    wifiConfig = ToBServiceHelper.getInstance().getServiceBinder().pbsGetAutoConnectWiFiConfig(0);
//                    Log.e(TAG,"wifi==========="+wifiConfig);
                } catch (RemoteException e) {
                }
//                txtPicoSN.setText("SN:" + SN_Number);
//                txtQUANTITYInfo.setText("Quantity:" + Quantity_Number);
//                mainActivity.socketConnect.SendToServer(" 33;" + SN_Number + ";Online|");
                bf.HomeQuantity(); // 电量传给homefragemnt

                /**
                 *  初始化
                 */
//                setViewChoos();
                /**
                 *  懒加载
                 */
//                setViewEventListener();
            }
        });
    }

    /**
     * 默认选择首页
     */
    private void setViewChoos() {
        //        默认选择
        rbHome.setChecked(true);
        switchFragment(0);
    }

    private void a() {

        af.videoStup();
    }

    /**
     * 加载fragem
     *
     * @param fragmentIndex
     */
    private void switchFragment(int fragmentIndex) {
//        在activity 中现在fragment
//        1 获取fragment管理器
        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        2 开启fragemnt 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

//        懒加载-如果需要显示的fragemnt为null，或者new 并添加到fragment事务中
        if (fragments[fragmentIndex] == null) {
//            af.onStart();

            switch (fragmentIndex) {
                case 0: //home;
                    Bundle bundle = new Bundle();
                    bf = new HomeFragment();
                    bf.setArguments(bundle);
                    fragments[fragmentIndex] = bf;
                    HomeFragmentPass();
                    break;
                case 1:
                    fragments[fragmentIndex] = new AcitonFragment();
                    break;
                case 2:
                    Bundle abundle = new Bundle();
                    af = new AboutFragment();
                    af.setArguments(abundle);

                    fragments[fragmentIndex] = af;
                    AboutFragmintPass();
                    break;
                case 3:
                    Bundle sbundle = new Bundle();
                    sf = new SetFragmint();
                    sf.setArguments(sbundle);
                    fragments[fragmentIndex] = sf;
                    SetFragmintPass();
                    break;
            }
            //==添Fragment对象列可Fragment非务中
            ///参数：是示Fragment的容器的ID,Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }
//        隐私其他fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                transaction.hide(fragments[i]);
                if (fragmentIndex != 2) {
                    try {
                        af.videoStup();
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        af.videoViewStart();
                    } catch (Exception e) {

                    }
//
                }
                if (fragmentIndex == 3) {
                    try {
                        sf.statusPaas();
                        sf.et_salary.setText("");

                    } catch (Exception e) {

                    }

                }
            }
        }

//        3 把显示的 Fragment 的对象添加 FragmentTransaction
//        HomeFragment homeFragment = new HomeFragment();
//        transaction.add(R.id.ll_main_content,homeFragment);
//        4 显示
        transaction.show(fragments[fragmentIndex]);
//        5  提交事务
        transaction.commit();
    }

    /**
     * home界面传数据
     */
    private void HomeFragmentPass() {
        bf.setFragmentCallback(new IFragmentHomeCallback() {
            @Override
            public void sendMsgToActivity(String string) {  // 回答按时
                serv();
            }

            /*接收packname*/
            @Override
            public void senMsgPackageName(String string) {
                PackName = string;
//                String GetTime = mCache.getAsString("TimeCache");
                if(file_mode.ferr().equals("")){
                    starapps();
                }
                if(file_mode.ferr().equals("0")){
                    starapps();
                }
                if(file_mode.ferr().equals("1")){
                    if (!isTime) {
                        showNormalDialog();
                    }else {
                        starapps();

                    }
                }

            }

            @Override
            public String getMsgHomeQrFromActivity(String mag) {
                return Quantity_Number;
            }

            @Override
            public String getMsgHomeLanguageFromActivity(String mag) {
                return Language;
            }

            @Override
            public String getMsgHomeTimeFromActivity(String time) {
                return DownGameTimes;
            }

            @Override
            public String getMsgHomeOnlineFromActivity(String Online) {
                return txtConnectInfo;
            }

            @Override
            public void onVideoClicksendMsgToActivity(String urls) {
                openVideos(urls);
            }
        });
    }
private void starapps(){
    PackageManager packageManager = getPackageManager();
    Intent intent=new Intent();
    intent =packageManager.getLaunchIntentForPackage(PackName);
    startActivity(intent);
}
    /**
     * set界面传数据
     */
    private void SetFragmintPass() {
        sf.setFragmentCallback(new IFragmentSetCallback() {
            @Override
            public void sendMsgToActivity(String string) {
//                Log.e(TAG,string);
                if (string.equals("open")) {
                    opensSet();
                }
                if (string.equals("shareScreen")) {
                    share_Screen();
                }
                if (string.equals("store")) {
                    App_store();
                }
                if (string.equals("wifi")) {
                    get_wifi();
                }
                if (string.equals("restart")) {
                    showNormalDialogRestart();
                }
                if (string.equals("read")) {
                    SERVER_IP = GetServerIP();
//                    Log.e(TAG,SERVER_IP);
//                    String filePath = "/sdcard/Gyt/";
//                    String fileName = "data.txt";
//                    File file = new File("/jamma.txt");
//                    String fileContent =read_store(file);
//                    Log.e(TAG,"-------------"+fileContent);

                }
            }

            public void getMsgSetLanguageFromActivity(String string) {
                Language = string;
                 bf.HomeLanga();
                if (Language.equals("1")) {
                    rbHome.setText("首页");
                    rbAbout.setText("关于");
                    rbAction.setText("活动");
                    rbSet.setText("设置");
                } else {
                    rbHome.setText("Home");
                    rbAbout.setText("About");
                    rbAction.setText("Activities");
                    rbSet.setText("Setting");
                }
                SaveLanguage(Language);
            }

            @Override
            public void sendMsgSetBackIP(String string) {
//                Log.e(TAG,string);
                SaveServerIP(string);
                /*IP 传过来的*/
//                showNormalDialogPass();
            }

            @Override
            public void sendMsgSetBackPas() {
                /*密码错误弹出框*/
                showNormalDialogPass();
            }

            @Override
            public void sendMsgSetBack(String string) {
                backs();
            }

            ;

            @Override
            public String getMsgSetIpFromActivity(String Ip) {
                return SERVER_IP;
            }

            @Override
            public String getMsgSetSnFromActivity(String sn) {
                return SN_Number;
            }

            @Override
            public String getMsgSetQrCodeFromActivity(String QrCode) {
                return null;
            }

            public String getMsgLanguageFromActivity(String Lg) {
                return Language;
            }

            @Override
            public String getMsgSetwifiConnectFromActivity(String QrCode) {
                return wifiConnectAdrss;
            }
        });
    }


    private String getConnectWifiSsid() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

//        Log.d("wifiInfo------", wifiInfo.toString());
//
//        Log.d("SSID------", wifiInfo.getSSID());

        return wifiInfo.getSSID();

    }

    private void get_wifi() {
        wifi_name = getConnectWifiSsid();
        showNormalDialogCheck_WiFi();
//            Log.e(TAG,wifi_name);
    }

    private void App_store() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        // 这里面的值是你要跳转app的包名，你跳转的清单文件里的package名
        intent = packageManager.getLaunchIntentForPackage("com.picovr.store");
        startActivity(intent);
    }

    private void share_Screen() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        // 这里面的值是你要跳转app的包名，你跳转的清单文件里的package名
        intent = packageManager.getLaunchIntentForPackage("com.pvr.picocast");
        startActivity(intent);
    }

    /*关于我们界面传信息*/
    private void AboutFragmintPass() {
        af.setFragmentCallback(new IFragmentAboutCallback() {
            @Override
            public void onLocateClicksendAboutActivity() {

            }

            @Override
            public void sendAboutActivity(String string) {

            }
        });
    }

    private void setViewEventListener() {
        /*home*/
        rbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(0);
            }
        });
        /*活动*/
        rbAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(1);
            }
        });
        /*关于*/
        rbAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(2);
            }
        });
        /*设置*/
        rbSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(3);
            }
        });
    }

    /**
     * 打开设置 open
     */
    public void opensSet() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsPropertySetHomeKey(PBS_HomeEventEnum.SINGLE_CLICK_HMD, PBS_HomeFunctionEnum.VALUE_HOME_QUICK_SETTING, new IBoolCallback.Stub() {
                @Override
                public void callBack(boolean b) throws RemoteException {
                    showNormalDialogOpenHome();
                }
            });
        } catch (RemoteException e) {
        }
    }

    //    private void showWaitingDialog() {
//        /* 等待Dialog具有屏蔽其他控件的交互能力
//         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
//         * 下载等事件完成后，主动调用函数关闭该Dialog
//         */
//        ProgressDialog waitingDialog= new ProgressDialog(MainActivity.this);
//        waitingDialog.setTitle("我是一个等待Dialog");
//        waitingDialog.setMessage("等待中...");
//        waitingDialog.setIndeterminate(true);
//        waitingDialog.setCancelable(false);
//        waitingDialog.show();
//    }
    /*保存IP提醒框*/
    private void showNormalDialogRestart() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("PICO glasses restart ");
        normalDialog.setMessage("YWhether Pico glasses restart");
        normalDialog.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
                /**
                 * 眼镜重新启动
                 */
                Glasses_restart();
            }
        });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    ;

    /*保存IP提醒框*/
    private void showNormalDialogSaveIP() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("save ip ");
        normalDialog.setMessage("You have successfully saved the server IP address, please restart the glasses");
        normalDialog.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
                /**
                 * 眼镜重新启动
                 */
                Glasses_restart();
            }
        });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    ;

    /*关闭home提醒框*/
    private void showNormalDialogCheck_WiFi() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("View the linked WiFi name");
        normalDialog.setMessage("Now the name of the linked WiFi is" + wifi_name);
        normalDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
//    normalDialog.setNegativeButton("close",
//            new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //...To-do
//                }
//            });
        // 显示
        normalDialog.show();
    }

    ;

    /*关闭home提醒框*/
    private void showNormalDialogOffHome() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("You have closed Pico Home");
        normalDialog.setMessage("Successfully closed Pico Home");
        normalDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
//    normalDialog.setNegativeButton("close",
//            new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //...To-do
//                }
//            });
        // 显示
        normalDialog.show();
    }

    ;

    /*打开home提醒框*/
    private void showNormalDialogOpenHome() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("You have entered Pico Home");
        normalDialog.setMessage("Entered Pico home to set up the basic functions of Pico glasses");
        normalDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
//    normalDialog.setNegativeButton("close",
//            new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //...To-do
//                }
//            });
        // 显示
        normalDialog.show();
    }

    ;

    /*密码提醒框*/
    private void showNormalDialogPass() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("wrong password ！！！");
        normalDialog.setMessage("wrong password, Please contact the JAMMA administrator");
//        normalDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    ;

    /*时间到期提醒框*/
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("Kindly reminding, please insert coins to play the game");
        normalDialog.setMessage("You can close the reminder box by clicking CLOSE ");
//        normalDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    /**
     * 关闭home
     */
    public void backs() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsPropertySetHomeKey(PBS_HomeEventEnum.SINGLE_CLICK_HMD, PBS_HomeFunctionEnum.VALUE_HOME_DISABLE, new IBoolCallback.Stub() {
                @Override
                public void callBack(boolean b) throws RemoteException {
                    showNormalDialogOffHome();
                }
            });
        } catch (RemoteException e) {
//            Log.d(TAG,"-------------------------cuows"+e);
        }
    }

    /**
     * 眼镜重新启动
     */
    public void Glasses_restart() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsControlSetDeviceAction(PBS_DeviceControlEnum.DEVICE_CONTROL_REBOOT, new IIntCallback.Stub() {
                @Override
                public void callback(int i) throws RemoteException {
                    Log.e(TAG, "=========关机==========" + i);
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });
        } catch (RemoteException e) {
//            Log.d(TAG,"-------------------------cuows"+e);
        }
    }

    /**
     * 保存服务器IP
     */
    public void SaveServerIP(String view) {
        Toast.makeText(this, getSerialNumber(), Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(view.toString().getBytes());
            fos.close();
            showNormalDialogSaveIP();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存语言 LanguageFILENAME
     */
    public void SaveLanguage(String view) {
//        Toast.makeText(this, getSerialNumber(), Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fos = openFileOutput(LanguageFILENAME, Context.MODE_PRIVATE);
            fos.write(view.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * IP获取
     *
     * @return
     */
    private String GetServerIP() {
        String IP = SERVER_IP;
        try {
            FileInputStream inStream = openFileInput(FILENAME);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            IP = StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IP;
    }
    /**
     * 语言获取
     *
     * @return
     */
    private String GetLanguage() {
        String Lg = Language;
        try {
            FileInputStream inStream = openFileInput(LanguageFILENAME);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
                Log.e(TAG,"我是与一月又一月一月又一月呀呀呀"+len);
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            Lg = StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lg;
    }
    /**
     * 是否免费模式
     *
     * @return
     */
//    private String Get_Is_mode() {
//        String MD = IS_mode;
//        try {
//            FileInputStream inStream = openFileInput(ISFreeMode);
//            int len = 0;
//            byte[] buf = new byte[1024];
//            StringBuilder StringBuilder = new StringBuilder();
//            while ((len = inStream.read(buf)) != -1) {
//                StringBuilder.append(new String(buf, 0, len));
//            }
//            inStream.close();
//            MD = StringBuilder.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return MD;
//    }
    /**
     * 获取安卓系统MAC
     */
    public static String getmacAddress() {
        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 生成二维码并显示
     */
    private void generateQrcodeAndDisplay() {
//        QRcontent = "https://jamma.globalvrpark.com"; //用於顯示二維碼字符

    }
    /***
     @param ：重启或关机
     PBS_DeviceControlEnum. DEVICE_CONTROL_REBOOT：重启
     PBS_DeviceControlEnum. DEVICE_CONTROL_SHUTDOWN：关机
     @param ：回调接口，0-成功，1-失败
     */
    /**
     * 倒计时
     */
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
//            int totalSeconds = (int) (millisUntilFinished/1000);
            long totalSeconds = millisUntilFinished / 1000;
//            Log.d("tog","=================="+totalSeconds);
//            mCache.put("TimeCache", Integer.toString((int) totalSeconds));
            long seconds = totalSeconds % 60;
            long minutes = (totalSeconds / 60) % 60;
            long hours = totalSeconds / 3600;
            String a = new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
//            Log.d(TAG,"---------------时间倒计时---------"+a);
            DownGameTimes = new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
            bf.HomeGetTime();


        }

        @Override
        public void onFinish() {
            isTime=false;
            if(!PackName.equals("")){
                Neo3KillApps(null, new String[]{PackName}, 0);
            }

//            finish();
        }
    }

    /**
     * 获取PICO 关机
     */
    public void androidShutDown() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsControlSetDeviceAction(PBS_DeviceControlEnum.DEVICE_CONTROL_SHUTDOWN, new IIntCallback.Stub() {
                @Override
                public void callback(int result) throws RemoteException {
//                    Log.d(TAG, "callback: " + result);
                }
            });
        } catch (RemoteException e) {
        }
    }

    /**
     * 亮屏
     */
    public void ScreenOn() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsScreenOn();
        } catch (RemoteException e) {
        }
    }

    /**
     * pico专用杀死应用
     */
    public void Neo3KillApps(int[] pids, String[] packageNames, int ext) {
//        Log.d(TAG,"-------------------------------关闭游戏的pids"+pids);
//        Log.d(TAG,"-------------------------------关闭游戏的packageNames"+packageNames);
//        Log.d(TAG,"-------------------------------关闭游戏的ext"+ext);
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsKillAppsByPidOrPackageName(pids, packageNames, 0);
        } catch (RemoteException e) {
//            Log.d(TAG, "-------------------------------关闭游戏的" + e);
        }
    }

    /**
     * 熄屏
     */
    public void ScreenOff() {
        try {
            ToBServiceHelper.getInstance().getServiceBinder().pbsScreenOff();
        } catch (RemoteException e) {
        }
    }

    /**
     * 获取安卓系统序列号
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            serial = android.os.Build.SERIAL;
        } catch (Exception e) {
        }
        return serial;
    }

    /**
     * 握手
     */
    public void ShakeHands() {
        if (!socketConnect.isRegister) {
            SendMessage(String.valueOf(ServerMSGType.Register.value()), false);
        }
    }
    /** 当前状态 */
//    private void CurrentStatus() {
//        if (!isGameing) {
////            txtConnectInfo.setText("Online");
////            txtConnectInfo.setTextColor(Color.GREEN);
//            txtConnectInfo="Online";
//            bf.OnlineHome();
//            mainActivity.SendMessage("5",false);
//        }else {
//            mainActivity.SendMessage("5",true);
//            txtConnectInfo="Offline";
//        }
//    }
    /** 启动第三方APK */
//    public void PlayGame(String pacName){
//        Util.startApp(getApplicationContext(), pacName);
//    }

    /**
     * 统一发送消息
     */
    public void SendMessage(String Number, boolean Status) {
        socketConnect.SendToServer(Number + ";" + SN_Number + ";" + Status + "|");
    }


    /**
     * ================================================================ip 写文件和读取
     */


    private void checkPermission() {
        mPermissionList.clear();

        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }


    /**
     * 定义方法
     */
    @Subscribe
    public void event(String msg) {
        Log.d(TAG, "jkjk" + msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }


}