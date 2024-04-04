package com.example.androidclientpico.ui.set;

import static android.provider.Telephony.Mms.Part.FILENAME;

//import static com.example.androidclientpico.ui.MainActivity.SERVER_IP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidclientpico.R;
import com.example.androidclientpico.ui.AppContext;
import com.example.androidclientpico.ui.Read_coin_file;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class SetFragmint extends Fragment {
    private final String TAG="HOEMset";
    /** 二级组件id */
    private LinearLayout setOne;
    /** 二级组件id */
    private LinearLayout setTow;
    /** 获取密码框 */
    private EditText setPsw;
    private TextView setDetermine;
    private TextView seteTxtIP;
//    private Button shareScreen;
    private TextView txtPicoSN;
    private Button setSave;
    private EditText set_eTxtIP;
    private IFragmentSetCallback fragmentCallback;
    private TextView setOpen;
    private TextView setback;
    private TextView showOpen;
    private TextView read;
    private Button ll_s_wifi;
    private Button ll_s_restart;
    public EditText et_salary;
    private RadioButton english;
    private RadioButton china;
    private Button btn_send;
    private String language;
    private Read_coin_file file_mode;
    private Switch switchrad;
    private TextView mode_text_;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
//        获取控件
        setOne = view.findViewById(R.id.ll_set_big_one);
        setTow = view.findViewById(R.id.ll_set_big_tow);
//        shareScreen = view.findViewById(R.id.share_Screen);
        showOpen = view.findViewById(R.id.ll_show_open);
        seteTxtIP = view.findViewById(R.id.ll_set_txtIP); // mac ip
        txtPicoSN = view.findViewById(R.id.ll_set_txtPicoSN);
        setSave = view.findViewById(R.id.ll_set_save);
        ll_s_wifi = (Button) view.findViewById(R.id.ll_set_wifi);
        ll_s_restart = (Button) view.findViewById(R.id.ll_set_restart);

        english = (RadioButton) view.findViewById(R.id.English);
        china = (RadioButton) view.findViewById(R.id.china);

//        read = view.findViewById(R.id.ll_set_read);
        set_eTxtIP =(EditText) view.findViewById(R.id.ll_set_eTxtIP);  // 服务器
        // 打开home设置
        setOpen = view.findViewById(R.id.ll_set_open);
        setback = view.findViewById(R.id.ll_set_back);
        switchrad = view.findViewById(R.id.sw_status);
        mode_text_ = view.findViewById(R.id.mode_text_change);
//       初始化
        file_mode = new Read_coin_file();
        init_vieofile();
        return view;
    }
    private void init_vieofile() {

        /** 二维码 */
//        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap("https://jamma.globalvrpark.com", 650, 650);
//        shareScreen.setImageBitmap(mBitmap);
        /** 获取IP */
        String ip= fragmentCallback.getMsgSetIpFromActivity("ip");
//        Log.d(TAG,"-----------------------------------------ip"+ip);
        set_eTxtIP.setText(ip);
        /** SN */

        String sn= fragmentCallback.getMsgSetSnFromActivity("sn");
        txtPicoSN.setText("SN:"+sn);
        /** 二维码 */
        String QrCode= fragmentCallback.getMsgSetQrCodeFromActivity("QrCode");
//        Log.d(TAG,"-----------------------------------------QrCode"+QrCode);
        /**
         * 获取IP
         */
       String wifiConnect = fragmentCallback.getMsgSetwifiConnectFromActivity("wifiConnect");





        seteTxtIP.setText("IP：" +wifiConnect );

        /*mainActivity 传过来的的语音类型*/
        language = fragmentCallback.getMsgLanguageFromActivity("Lg");

//        Log.d(TAG,"-----------------------------------------language"+language);
        /** 默认显示 */
        setOne.setVisibility(View.VISIBLE);
        setTow.setVisibility(View.GONE);

//        setOne.setVisibility(View.GONE);
//        setTow.setVisibility(View.VISIBLE);


        /*是否免费版 1收费 0是免费*/
//        Log.e(TAG,"77777777777"+coinipSave.ferr());
        if(file_mode.ferr().equals("")){
            switchrad.setChecked(false);
            file_mode.changeFerr("0");
            if(language.equals("1")){
                mode_text_.setText("免费模式");
            }else {
                mode_text_.setText("Charge mode");
            }
        }
        if(file_mode.ferr().equals("0")){
            switchrad.setChecked(false);
            if(language.equals("1")){
                mode_text_.setText("免费模式");
            }else {
                mode_text_.setText("Free mode");
            }
        }
        if(file_mode.ferr().equals("1")){
            switchrad.setChecked(true);
//            mode_text_.setText("收费模式");
            if(language.equals("1")){
                mode_text_.setText("收费模式");
            }else {
                mode_text_.setText("Charge mode");
            }
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Bundle bundle = this.getArguments();
//        String sd= bundle.getString("message");
//        Log.d(TAG,"DSFL"+sd);
        super.onCreate(savedInstanceState);
    }
    /**
     * FRAGEMTN 回调函数
     * @param callback
     */
    public void setFragmentCallback(IFragmentSetCallback callback){
        fragmentCallback = callback;
    }

    /** 获取格式化时间 */
    private String getTime(String s) {
        return new SimpleDateFormat(s).format(new Date(System.currentTimeMillis()));
    }
    /*语音切换*/
    public void LanguageChange(){
        /**
         * 语言
         */
        if(language.equals("1")){
            china.setChecked(true);
            ll_s_wifi.setText("查看WiFi");
            ll_s_restart.setText("重新启动");
//            shareScreen.setText("投屏");
            setSave.setText("保存");
//            mode_text_.setText("收费模式");
//            ll_set_restart
        }else  {
            english.setChecked(true);
            ll_s_wifi.setText("CHECK WIFI");
            ll_s_restart.setText("RESTART");
//            shareScreen.setText("Share screen");
            setSave.setText("Save");
            btn_send.setText("send");
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_salary = (EditText) getActivity().findViewById(R.id.ll_set_Password);
        btn_send = (Button) getActivity().findViewById(R.id.ll_set_determine);

        LanguageChange();

//        /** 密码判断 */
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, et_salary.getText()+"");
//                Log.i(TAG, getTime("MMddHH"));
                //String username=user.getUsername().toString();
                String salary = et_salary.getText().toString();
                if (et_salary.getText().toString().equals(getTime("MMddHH"))) {
//                    Log.d(TAG,"对"+salary);
                    setOne.setVisibility(View.GONE);
                    setTow.setVisibility(View.VISIBLE);

                } else {
                    fragmentCallback.sendMsgSetBackPas();
//                    Log.d(TAG,"密码错误"+salary);
//                    Toast.makeText(SetFragmint.this.getContext(), "密码错误！", Toast.LENGTH_SHORT).show();
                }
//                if(salary==null){
//                    Log.d(TAG,"请输入密码"+salary);
//                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_LONG).show();
//                } else if (salary == "123456") {
//                    Log.d(TAG,"密码正确"+salary);
//                    Toast.makeText(getActivity(), "密码正确", Toast.LENGTH_LONG).show();
//                }else {
//                    Log.d(TAG,"密码错误"+salary);
//                    Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_LONG).show();
//                    setOne.setVisibility(View.GONE);
//                    setTow.setVisibility(View.VISIBLE);
//                }
            }
        });
        /*选择语言*/
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.getMsgSetLanguageFromActivity("0");
                ll_s_wifi.setText("CHECK WIFI");
                ll_s_restart.setText("RESTART");
//                shareScreen.setText("Share screen");
                setSave.setText("Save");
                btn_send.setText("send");
            }
        });
        /*中午*/
        china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.getMsgSetLanguageFromActivity("1");
                ll_s_wifi.setText("查看WiFi");
                ll_s_restart.setText("重新启动");
//                shareScreen.setText("投屏");
                setSave.setText("保存");
                btn_send.setText("确定");
//            ll_set_restart

            }
        });
    /** 保存IP */
        setSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), set_eTxtIP.getText().toString(), Toast.LENGTH_LONG).show();
                if(set_eTxtIP.getText()==null){
//                Log.e(TAG,"ip不能为空请输入ip");
//                    Toast.makeText(getActivity(), "请输入服务器IP地址", Toast.LENGTH_LONG).show();
                }else {
                    String a  = String.valueOf(set_eTxtIP.getText().toString().getBytes());
                    fragmentCallback.sendMsgSetBackIP(String.valueOf(set_eTxtIP.getText()));
//                    Log.e(TAG,"ip不能为空请输入ip"+set_eTxtIP.getText());
//                    Toast.makeText(getActivity(), getSerialNumber(), Toast.LENGTH_LONG).show();
//                    try {
//                        FileOutputStream fos =view.getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                        fos.write(seteTxtIP.getText().toString().getBytes());
//                        fos.close();
//                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
//                        AppContext.restartApp();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
     /*   read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgToActivity("read");
            }
        });*/
        switchrad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
//                    Log.e(TAG,"111111111111111111111");
                    file_mode.changeFerr("1");
                    if(language.equals("1")){
                        mode_text_.setText("收费模式");
                    }else {
                        mode_text_.setText("Charge mode");
                    }
//                    Toast.makeText(getBaseContext(), "TRUE", Toast.LENGTH_SHORT).show();
                } else {
                    file_mode.changeFerr("0");
                    if(language.equals("1")){
                        mode_text_.setText("免费模式");
                    }else {
                        mode_text_.setText("Free mode");
                    }
//                    Log.e(TAG,"00000000000000000000000000000");
//                    Toast.makeText(getBaseContext(), "FALSE", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /**
         * 打开home设置界面
         */
        setOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgToActivity("open");
            }
        });

//        shareScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentCallback.sendMsgToActivity("shareScreen");
//            }
//        });
        showOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgToActivity("store");
            }
        });

        ll_s_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgToActivity("wifi");
            }
        });

        ll_s_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgToActivity("restart");
            }
        });


        /**
         * 返回
         */
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.sendMsgSetBack("back");
            }
        });
    }
    public void statusPaas(){
        /** 默认显示 */
        setOne.setVisibility(View.VISIBLE);
        setTow.setVisibility(View.GONE);
    }
    /** 保存服务器IP */
    public void SaveServerIP(View view) {
        Toast.makeText(getActivity(), getSerialNumber(), Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(set_eTxtIP.getText().toString().getBytes());
            fos.close();
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
            AppContext.restartApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 获取安卓系统序列号 */
    public static String getSerialNumber() {
        String serial = null;
        try {
            serial = android.os.Build.SERIAL;
        } catch (Exception e) { }
        return serial;
    }

//


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//
//        setDetermine = getActivity().findViewById(R.id.ll_set_determine);
//        String str1="";
//        setPsw = getActivity().findViewById(R.id.ll_set_Password);
//        str1 = setPsw.getText().toString();
//
//        System.out.printf(str1);
//
//        setDetermine.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // TODO Auto-generated method stub
//                Toast.makeText(getActivity(), "success2", 0).show();
//            }
//        });
//    }
}
