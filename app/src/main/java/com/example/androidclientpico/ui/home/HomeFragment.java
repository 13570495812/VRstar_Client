package com.example.androidclientpico.ui.home;
import static com.example.androidclientpico.ui.MainActivity.isTime;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclientpico.R;

//import com.example.androidclientpico.ui.adapter.MyRecyclerBoxAdapter;

import com.example.androidclientpico.ui.JsonUtils;
import com.example.androidclientpico.ui.Read_coin_file;
import com.example.androidclientpico.ui.adapter.MyRecyclerViewAdapter;
import com.example.androidclientpico.ui.adapter.OnAdapterClickListener;
import com.example.androidclientpico.ui.model.BaseCommandModel;
import com.example.androidclientpico.ui.model.RecommandBodyValue;
import com.example.androidclientpico.ui.video;
import com.example.androidclientpico.util.ResponseEntityToModule;
import com.google.android.flexbox.FlexboxLayout;
import com.pvr.tobservice.ToBServiceHelper;

import org.intellij.lang.annotations.Language;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private final String TAG="HOEM FRAGMENT";
    private RadioButton chooseAll;
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 1;
    private EditText searchView;
    private IFragmentHomeCallback fragmentCallback;
    private OnAdapterClickListener RecyclerViewCallback;
    /**  sn序列号 */
    public static String SN_Number = "";
    public static int millisInFuture=0;
    public static int radioGroupsCheck=0;
    public static boolean radioGroupsCheckClick=false;
    /**  sn序列号 */

    public static String Quantity_Number;
    public static String code="";
    private TextView llquantity;

    private TextView refreshs;

    private RecyclerView mRecyclerView;
    private RadioButton Hotlist;

    private Request CrRequst;
    private View view;
    private TextView timeUpdate;
    private List<RecommandBodyValue> data;
    private RecyclerView mItemList;
    private MyRecyclerViewAdapter adapter;
    private TextView online;
    private RadioButton movement;
    private RadioButton shoot;
    private RadioButton strategy;
    private RadioButton adventure;
    private RadioButton leisure;
    private RadioButton plot;
    private RadioButton puzzle;
    private RadioButton other;
//    private JSONObject item;
//    private JSONArray jsonArray;
//    private TextView llhomename;
    private Context context;
    /*刷新时间*/
    private int TIME = 1000;
    MyCountDownTimer myCountDownTimer;
    private RadioGroup radioGroups;
    private String langa;
    private LinearLayout lferr_mode;
    private TextView ferr_text;
    private Read_coin_file free_mode_functuon;
    private LinearLayout lcharge_mode;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//       eventBus,LiveDate

        Log.d(TAG,"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Bundle bundle = this.getArguments();
//        String sd= bundle.getString("message");
//        Log.d(TAG,"DSFL"+sd);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_home,container,false);
        }
//        ParentActivity parentActivity = (ParentActivity ) getActivity();
        view = inflater.inflate(R.layout.fragment_home,container,false);
        radioGroups = view.findViewById(R.id.ll_home_radioGroup);


        chooseAll = view.findViewById(R.id.ll_home_choose_all); // 全部

        Hotlist = view.findViewById(R.id.ll_home_choose_Hotlist);
        movement = view.findViewById(R.id.ll_home_choose_Movement);
        shoot = view.findViewById(R.id.ll_home_choose_Shoot);
        strategy = view.findViewById(R.id.ll_home_choose_Strategy);
        adventure = view.findViewById(R.id.ll_home_choose_Adventure);
        leisure = view.findViewById(R.id.ll_home_choose_Leisure);
        plot = view.findViewById(R.id.ll_home_choose_Plot);
        puzzle = view.findViewById(R.id.ll_home_choose_Puzzle);
        other = view.findViewById(R.id.ll_home_choose_Other);

        timeUpdate = view.findViewById(R.id.time_update); //
        //是否在线
        online = view.findViewById(R.id.l_top_Online);

        llquantity = view.findViewById(R.id.ll_home_quantity); //电量

        refreshs = view.findViewById(R.id.home_refresh); // 刷新
        // 免费模式显示的布局
        lferr_mode = view.findViewById(R.id.Free_mode);
        ferr_text = view.findViewById(R.id.text_mode_home);

        lcharge_mode = view.findViewById(R.id.charge_mode);
        mRecyclerView = view.findViewById(R.id.recyclerView);  // recyclerView布局
        free_mode_functuon = new Read_coin_file();
//        llhomename = view.findViewById(R.id.ll_home_namell);
//        RecyclerView.Adapter my = MyRecyclerViewAdapter();
//        MyRecyclerViewAdapter = MyRecyclerViewAdapter(this,listView)
    
        setViewChoos();
        OkHttpClientRequstC(0); //  默认获取全部数据
        initData();// 初始化
//        getJsonList();
        context = getContext();

        return view;
    }

    /**
     * 获取电量
     */
    public void HomeQuantity(){
        String ms= fragmentCallback.getMsgHomeQrFromActivity("mag");
        llquantity.setText("Quantity:"+ms);

    }

    /**
     * 服务器重新启动 MediaPlayer
     */
    public void updateUi() {
        new Thread(){
            @Override
            public void run(){
//                Log.e(TAG,"djfdljfld+"+llhomename);
            }
        }.start();
    }
    /**
     * 获取时间
     */
    public void HomeGetTime(){
        timeUpdate.setText(fragmentCallback.getMsgHomeTimeFromActivity("item"));
//         millisInFuture=fragmentCallback.getMsgHomeTimeFromActivity("item");
//        countdown(); //时间倒计时
    }
    public void HomeLanga(){
        langa = fragmentCallback.getMsgHomeLanguageFromActivity("mag");
        getNavList();
    }
    /**
     * 倒计时
     */
//    private void countdown(){
//        PeterTimeCountRefresh timer= new PeterTimeCountRefresh(millisInFuture,1000);
//        timer.setOnTimerProgressListener(new PeterTimeCountRefresh.OnTimerProgressListener() {
//            @Override
//            public void onTimerProgress(long timeLong) {
////             String text = TimeUtils.parseDuration(timeLong);
//                long totalSeconds = timeLong/1000;
//                long seconds = totalSeconds % 60;
//                long minutes = (totalSeconds/60)%60;
//                long hours = totalSeconds/3600;
//                String a = new Formatter().format("%02d:%02d:%02d",hours,minutes,seconds).toString();
//                timeUpdate.setText(a);
////             tv.setText(text);
//            }
//        });
//
//        timer.setOnTimerFinishListener(new PeterTimeCountRefresh.OnTimerFinishListener() {
//            @Override
//            public void onTimerFinish() {
//                Log.d(TAG, "onTimerFinish enter");
//                fragmentCallback.sendMsgToActivity("时间结束");
//            }
//        });
//        timer.start();
//    }

    /**
     * 请求
     * allCreateRequst 全部
     * 0
     */
    private Request allCreateRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/DataList.json").get().build();
    }
    /**
     * 热销榜 1
     */
    private Request HotlistRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Hotlist.json").get().build();
    }
    /**
     * 运动 2
     */
    private Request MovementRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Movement.json").get().build();
    }

    /**
     * 射击 3
     */
    private Request ShootRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Shoot.json").get().build();
    }

    /**
     * 策略 4
     */
    private Request StrategyRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Strategy.json").get().build();
    }
    /**
     * 冒险 5
     */
    private Request AdventureRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Adventure.json").get().build();
    }
    /**
     * 休闲 6
     */
    private Request LeisureRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Leisure.json").get().build();
    }
    /**
     * 休闲 7
     */
    private Request PlotRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Plot.json").get().build();
    }
    /**
     * 益智 8
     */
    private Request PuzzleRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Puzzle.json").get().build();
    }
    /**
     * 其他 9
     */
    private Request OtherRequst(){
        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/Other.json").get().build();
    }

//    /**
//     * 5 动作
//     * @return
//     */
//    private Request actionHomeCreateRequst(){
//        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/actionHome.json").get().build();
//    }
//
//    /**
//     * 3飞行
//     * @return
//     */
//    private Request flightCreateRequst(){
//        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/flight.json").get().build();
//    }
//
//    /**
//     * 2情节
//     * @return
//     */
//    private Request plotCreateRequst(){
//        return  new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/plot.json").get().build();
//    }
//    /**
//     *4射击
//     * @return
//     */
//    private Request shootingCreateRequst() {
//        return new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/shooting.json").get().build();
//    }
//    /**
//     * 1策略
//     * @return
//     */
//    private Request strategyCreateRequst() {
//        return new Request.Builder().url("https://jamma.globalvrpark.com/img_tmp/jsonData/strategy.json").get().build();
//    }

    /**
     * 初始化
     */
    private void initData() {
        String ms= fragmentCallback.getMsgHomeQrFromActivity("mag");
        llquantity.setText("Quantity:"+ms);

        /*语音*/
        langa = fragmentCallback.getMsgHomeLanguageFromActivity("mag");
        if(langa.equals("1")){
            ferr_text.setText("免费模式");
        }else {
            ferr_text.setText("Free mode");
        }
        if(free_mode_functuon.ferr().equals("0")){
            lferr_mode.setVisibility(View.VISIBLE);
            lcharge_mode.setVisibility(View.GONE);
        }
        if(free_mode_functuon.ferr().equals("")){
            lferr_mode.setVisibility(View.VISIBLE);
            lcharge_mode.setVisibility(View.GONE);
        }
        if(free_mode_functuon.ferr().equals("1")){
            lferr_mode.setVisibility(View.GONE);
            lcharge_mode.setVisibility(View.VISIBLE);
        }
//        getJsonList();
        myCountDownTimer = new MyCountDownTimer(TIME, 1000);
        myCountDownTimer.start();
    }

////    @Override
//    public void doSomeThing(String string) {
//        Log.d(TAG,"-------------"+string)
////        mText.setText(string);
//    }

    /**
     * FRAGEMTN 回调函数
     * @param callback
     */
    public void setFragmentCallback(IFragmentHomeCallback callback){
        fragmentCallback = callback;
    }

    /**
     * 电量
     */
    public void quantityHome(){
        String ms= fragmentCallback.getMsgHomeQrFromActivity("mag");
        llquantity.setText("Quantity:"+ms);
//        Log.d(TAG,"电量"+ms);
        Quantity_Number = fragmentCallback.getMsgHomeQrFromActivity("mag");
    }
    /**
     * 状态
     */
    public void OnlineHome(){
        String msg= fragmentCallback.getMsgHomeOnlineFromActivity("msg");
        online.setText(":"+msg);
    }
    // 获取本地图片

    // 获取assets文件夹中的图像并转换为Bitmap对象
    private Bitmap getLocalBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream);
    }
    /**
     * 倒计时
     */
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            int totalSeconds = (int) (millisUntilFinished/1000);
        }
        @Override
        public void onFinish() {
            getNavList();
//            getJsonList();
            myCountDownTimer.cancel();
//            finish();
        }
    }
    /*获取本地头部列表JSON文件件*/
    public JSONArray getFileNavigation  (){
        JSONArray jsonArray = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
        }
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/jamma/";
        File file = new File(folderPath, "navigation.json");
//        String json = JsonUtils.loadJSONFromAsset(getActivity(), String.valueOf(folder));  //
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);    }
            inputStream.close();
            String jsonString = stringBuilder.toString();
            jsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }
    /*获取本地JSON文件件*/
     public JSONArray getFilePermission  (){
         JSONArray jsonArrayList = null;
         JSONArray jsonArray = new JSONArray();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
        }
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/jamma/";
        File file = new File(folderPath, "newdata.json");
//        String json = JsonUtils.loadJSONFromAsset(getActivity(), String.valueOf(folder));  //
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            String jsonString = stringBuilder.toString();
            jsonArrayList = new JSONArray(jsonString);
            for (int i = 0; i < jsonArrayList.length(); i++) {
                JSONObject item = jsonArrayList.getJSONObject(i);
                int type = item.getInt("type");

                    if(radioGroupsCheck==0){
                        jsonArray.put(item);
                    }else if(type==radioGroupsCheck){
                        jsonArray.put(item);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    /*JSON文件读取游戏列表数据*/
    public void getNavList() {
        RadioGroup radioGroup = view.findViewById(R.id.ll_home_radioGroup);
        radioGroup.removeAllViews(); // 清空 RadioGroup 中的所有子 View
        JSONArray jsonArray =  getFileNavigation();
        if (jsonArray != null) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String Ename = item.getString("Ename");
                    String CHname = item.getString("CHname");
                    int PackageName = item.getInt("PackageName");
//                            int age = item.getInt("age");
                    Log.d(TAG, "Ename: ");
                    Log.d(TAG, "CHname: " + Ename + ", PackageName: " +PackageName);
// 创建 RadioButton 对象
                    RadioButton radioButton = new RadioButton(getActivity());
//                   radioButton.setButtonDrawable(null);
                    radioButton.setButtonDrawable(android.R.color.transparent);
//                    radioButton.setBackgroundResource(R.drawable.selector_main_rb_text);
                    radioButton.setTextColor(getResources().getColorStateList(R.drawable.button_txt_color));
//                    radioButton.setTextColor(getResources().getColorStateList(R.color.black));
                    if(langa.equals("1")){
                        radioButton.setText(CHname);
                    } else {
                        radioButton.setText(Ename);
                    }

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,0,10,0);
                    radioButton.setLayoutParams(params);
                    //设置不要单选按钮前面的小圆圈
                    Bitmap a=null;
                    radioButton.setButtonDrawable(new BitmapDrawable(a));

                    radioButton.setPadding(20,0,20,0);
                    radioButton.setTextSize(20);

//                    radioButton.setButtonDrawable(R.drawable.selector_main_rb_text);
                    radioButton.setId(PackageName);
                    radioGroups.addView(radioButton);


                    /* 名字*/
//                    View itemLis = flexboxLayout.getChildAt(i);
//                    TextView textView = itemLis.findViewById(R.id.ll_home_name);
//                    textView.setText(name);
                    /*图片*/

                    /*点击事件*/
//                    TextView openStart = itemLis.findViewById(R.id.openStart);
//                    openStart.setTag(PackageName); // 设置 tag 属性为 item 的 index
                    radioGroups.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                            if(radioGroupsCheckClick){
                                radioGroupsCheck=checkedId;
                                getJsonList();
                            }
                            // 处理 RadioButton 的选中状态
//                            switch (checkedId) {
//                                case 0:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//                                    // 选中了 Option 1
//                                    break;
//                                case 1:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//
//                                    Log.e(TAG,"setOnCheckedChangeListenersetOnCheckedChangeListener5555555555555555555");
//                                    // 选中了 Option 1
//                                    break;
//                                case 2:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//
//                                    // 选中了 Option 2
//                                    break;
//                                case 3:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//                                    Log.e(TAG,"setOnCheckedChangeListenersetOnCheckedChangeListener"+checkedId);
////                                    radioGroupsCheck=3;
//                                    // 选中了 Option 3
//                                    break;
//                            }
                        }
                    });
                }
                radioGroups.check(radioGroupsCheck);
                radioGroupsCheckClick=true;
                getJsonList();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
    /*JSON文件读取游戏列表数据*/
    public void getJsonList() {
        FlexboxLayout flexboxLayout = (FlexboxLayout) getActivity().findViewById(R.id.flexbox_layout); // 大布局
        flexboxLayout.removeAllViews(); // 清空flexbox
        JSONArray jsonArray =  getFilePermission();
        if (jsonArray != null) {
            try {
                int a = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String CHname = item.getString("CHname");
                    String name = item.getString("name");
                    int type = item.getInt("type");
                    String img = item.getString("image");
                    String PackageName = item.getString("PackageName");
                        View newItemView =  LayoutInflater.from(HomeFragment.this.getContext()).inflate(R.layout.item_layout, null);
                        /** 获取item.xml */
                        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT
                        );
//                        if((3 * a) - 1!=a){
//                            lp.setFlexGrow(1.0f); // 设置item的flex grow属性，控制item在空间不足时的扩展比例
//                        }
                        lp.setMargins(20, 10, 20, 10); // 设置item的margin

                        flexboxLayout.addView(newItemView, lp);

                        flexboxLayout.requestLayout();
                        /* 名字*/
                        View itemLis = flexboxLayout.getChildAt(i);
                        TextView textView = itemLis.findViewById(R.id.ll_home_name);
                        if(langa.equals("1")){
                            textView.setText(CHname);
                        }else {
                            textView.setText(name);
                        }
                        /*图片*/
                        ImageView imageView = itemLis.findViewById(R.id.ll_home_imageView);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(img, options);
                        imageView.setImageBitmap(bitmap);
                        /*点击事件*/
                        TextView openStart = itemLis.findViewById(R.id.openStart);
                        openStart.setTag(PackageName); // 设置 tag 属性为 item 的 index
                        openStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Log.e(TAG,"688688688688688");
                                String Packag = (String) v.getTag();
                                if(Packag.endsWith("mp4")){
                                    //启动视频播放器播放视频
                                    fragmentCallback.onVideoClicksendMsgToActivity(Packag);
                                }else {
//                                    Log.e(TAG,"688688688688688"+Packag);
                                    fragmentCallback.senMsgPackageName(Packag);
//                                    Intent launchIntentForPackage=getActivity().getPackageManager().getLaunchIntentForPackage(Packag); // 启动第三方APP
//                                    getActivity().startActivity(launchIntentForPackage);
                                }
                            }
                        });
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d(TAG,"onActivityCreated");
        /**
         * 电量
        * */
        llquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms= fragmentCallback.getMsgHomeQrFromActivity("mag");
                llquantity.setText("Quantity:"+ms);
//                Log.d(TAG,"电量"+ms);
                Quantity_Number = fragmentCallback.getMsgHomeQrFromActivity("mag");
//                Toast.makeText(HomeFragment.this.getContext(),Quantity_Number,Toast.LENGTH_SHORT).show();
            }
        });
        /** 刷新*/
        refreshs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray =  getFilePermission();
                FlexboxLayout flexboxLayout = (FlexboxLayout) getActivity().findViewById(R.id.flexbox_layout); // 大布局
                flexboxLayout.removeAllViews(); // 清空flexbox
                if (jsonArray != null) {
                    try {
                        int a = jsonArray.length();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String CHname = item.getString("CHname");
                            String name = item.getString("name");
                            int type = item.getInt("type");
                            String img = item.getString("image");
                            String PackageName = item.getString("PackageName");
                            View newItemView =  LayoutInflater.from(HomeFragment.this.getContext()).inflate(R.layout.item_layout, null);
                            /** 获取item.xml */
                            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                            );
//                        if((3 * a) - 1!=a){
//                            lp.setFlexGrow(1.0f); // 设置item的flex grow属性，控制item在空间不足时的扩展比例
//                        }

                            lp.setMargins(20, 10, 20, 10); // 设置item的margin

                            flexboxLayout.addView(newItemView, lp);

                            flexboxLayout.requestLayout();
                            /* 名字*/
                            View itemLis = flexboxLayout.getChildAt(i);
                            TextView textView = itemLis.findViewById(R.id.ll_home_name);
                            if(langa.equals("1")){
                                textView.setText(CHname);
                            }else {
                                textView.setText(name);
                            }
                            /*图片*/
                            ImageView imageView = itemLis.findViewById(R.id.ll_home_imageView);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap bitmap = BitmapFactory.decodeFile(img, options);
                            imageView.setImageBitmap(bitmap);
                            /*点击事件*/
                            TextView openStart = itemLis.findViewById(R.id.openStart);
                            openStart.setTag(PackageName); // 设置 tag 属性为 item 的 index
                            openStart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Log.e(TAG,"757757757757757757757");
                                    String Packag = (String) v.getTag();
                                    if(Packag.endsWith("mp4")){
                                        //启动视频播放器播放视频
                                        fragmentCallback.onVideoClicksendMsgToActivity(Packag);
                                    }else {
                                        fragmentCallback.senMsgPackageName(Packag);

//                                        Intent launchIntentForPackage=getActivity().getPackageManager().getLaunchIntentForPackage(Packag); // 启动第三方APP
//                                            getActivity().startActivity(launchIntentForPackage);


                                    }
                                }
                            });
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                /**
                 * manactivity传数据
                 */
                fragmentCallback.sendMsgToActivity("刷新");

            }
        });
        /** 全部*/
        chooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(0);
            }
        });
        /**
         *
         *  排行 1
         */
        Hotlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(1);
            }
        });
        /**
         *
         *   排名2
         */
        movement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(2);
            }
        });
        /**
         *
         *  排序3
         *  */
        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(3);
            }
        });
        /**
         *
         * 排序4
         */
        strategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(4);
            }
        });
        /**
         *
         * 排序5
         */
        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(5);
            }
        });
        /**
         *
         * 排序6
         */
        leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(6);
            }
        });
        /**
         *
         * 排序7
         */
        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(7);
            }
        });
        /**
         *
         * 排序8
         */
        puzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(8);
            }
        });
        /**
         *
         * 排序9
         */
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClientRequstC(9);
            }
        });
    }

    /*网络请求*/
    public void OkHttpClientRequstC(int a){

        HomeQuantity();
        // 1客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5,TimeUnit.SECONDS); // 链接编辑
        builder.writeTimeout(5,TimeUnit.SECONDS);
        builder.readTimeout(5,TimeUnit.SECONDS);
        // 2请求
        OkHttpClient client =builder.build();
        if(a==0){
            CrRequst =allCreateRequst();
        }
        if(a==1){
            CrRequst =HotlistRequst();
        }
        if(a==2){
            CrRequst =MovementRequst();
        }
        if(a==3){
            CrRequst =ShootRequst();
        }
        if(a==4){
            CrRequst =StrategyRequst();
        }
        if(a==5){
            CrRequst =AdventureRequst();
        }
        if(a==6){
            CrRequst =LeisureRequst();
        }
        if(a==7){
            CrRequst =PlotRequst();
        }
        if(a==8){
            CrRequst =PuzzleRequst();
        }
        if(a==9){
            CrRequst =OtherRequst();
        }
        Call call =client.newCall(CrRequst);
        // 3 返回数据的处理
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.d(TAG,"ml"+call.request());
                System.out.println("-------------------------请求失败");
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("-------------------------请求成功");

                BaseCommandModel model =(BaseCommandModel) ResponseEntityToModule.parseJsonToModule(response.body().string(), BaseCommandModel.class);
                data = model.data;
                /**
                 * RecyclerView
                 */
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollToPosition(0);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayout.VERTICAL));

                        adapter = new MyRecyclerViewAdapter(getActivity(),data);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        adapter.setOnAdapterClickListener(new OnAdapterClickListener() {
                            @Override
                            public void onCityClick(String name) {
//                                Log.d(TAG,"----------我是fragemnt接收的包名--------------"+name);
                                code=name;
                                fragmentCallback.senMsgPackageName(code);
                            }
                            @Override
                            public void onLocateClick() {

                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 时间倒计时
     */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }

    //    默认选择
    private void setViewChoos() {
        chooseAll.setChecked(true);

        if(searchView==null){return;

        }else{//获取ImageView的id
//            searchView.setHintTextColor(TypedValue.COMPLEX_UNIT_SP, 14);//14sp//设置字体颜色
//            int imgId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon",null,null);//获取ImageView

//            ImageView searchButton =(ImageView)searchView.findViewById(imgId);//设置图片

//            searchButton.setImageResource(R.drawable.search);//不使用默认

//            searchView.setIconifiedByDefault(false);

        }
    }
}
