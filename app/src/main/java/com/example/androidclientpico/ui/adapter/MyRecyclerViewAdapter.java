package com.example.androidclientpico.ui.adapter;

import static com.example.androidclientpico.ui.MainActivity.isTime;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidclientpico.R;
import com.example.androidclientpico.ui.model.RecommandBodyValue;
import com.example.androidclientpico.util.ACache;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyRecyAdapterHolder> {
    private final Context mContext;
    private final List<RecommandBodyValue> mList;
    private static final String TAG="TOOOOOOOO";
    private ACache mCache; // 缓存
    private View view;
    private OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener listener){
        onAdapterClickListener = listener;
    }
    public MyRecyclerViewAdapter(Context context, List<RecommandBodyValue> list){
//        Log.d(TAG,"onBindViewHolder"+list);
        this.mContext = context;
        this.mList = list;
    }
    @NonNull
    @Override
    public MyRecyAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent,false);

//        Log.d(TAG,"on"+getItemCount());
        return new MyRecyAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyAdapterHolder holder, int position) {
//        Log.d(TAG,"onBindViewHolder"+position);
        holder.tv.setText(mList.get(position).lbl);
        Glide.with(mContext).load(mList.get(position).img).into(holder.imageView);
        /**
         * 设置搞定
         */
//        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
//        layoutParams.height=420;
        /**
         * 点击事件
         */
        final String code =mList.get(position).code;
        holder.openStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"----------我是item返回的包名--------------"+code);

                /** 获取 */
//                mCache = ACache.get(mContext);
//                String  GetTime=  mCache.getAsString("TimeCache");
                Log.d(TAG,"----------isTimeisTimeisTime--------------"+isTime);
//                if(!isTime){
//                    Context context =mContext.getApplicationContext();
//                    CharSequence text = "Hello toast!";
//                    int duration = Toast.LENGTH_SHORT;
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
                    onAdapterClickListener.onLocateClick();
//                    Intent launchIntentForPackage=mContext.getPackageManager().getLaunchIntentForPackage(code); // 启动第三方APP
//                    mContext.startActivity(launchIntentForPackage);
//                }else {

//                    Intent launchIntentForPackage=mContext.getPackageManager().getLaunchIntentForPackage(code); // 启动第三方APP
//                    mContext.startActivity(launchIntentForPackage);
//                }

//                onAdapterClickListener.onCityClick(code);


//                Log.d(TAG,"我是id"+code);
            }
        });
//        holder.tv.setText(position);
        Log.d(TAG,"onBindViewHolder"+getItemCount());

    }
    // 定义接口

//    private void markBitmap(String img, ImageView imageView) {
//        new Throwable(String.valueOf(new Runnable(){
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                InputStream inputStream = null;
//                try {
//                    URL url = new URL(img);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setReadTimeout(8000);
//                    connection.setConnectTimeout(8000);
//                    connection.setRequestMethod("GET");
//
//                    inputStream = connection.getInputStream();
//                    //获取图片信息输入流
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    imageView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//
//                }catch (MalformedURLException e){
//                    e.printStackTrace();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }));
//    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    // 第一个参数为数组中的图片url，第二个参数为需要被传递图片信息的imageView实例


    public class MyRecyAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private TextView openStart;
        private ImageView imageView;
        public MyRecyAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.ll_home_name);
            openStart = itemView.findViewById(R.id.openStart);
            imageView = itemView.findViewById(R.id.ll_home_imageView);


        }
    }
}
