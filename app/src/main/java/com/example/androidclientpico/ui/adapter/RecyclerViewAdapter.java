package com.example.androidclientpico.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclientpico.R;
import com.example.androidclientpico.ui.model.RecommandBodyValue;

import java.util.List;

import kotlin.collections.LongIterator;


public class RecyclerViewAdapter extends BaseAdapter {
//    adapter里面的时间都list保存
    private final List<String> mList;
    private final Context mContext;
    private final String TAG="HOEM FRAGMENT";
    public RecyclerViewAdapter(List<String>mockList, Context context){
        Log.d(TAG,"======================================mockList"+mockList);
        this.mList = mockList;
        this.mContext = context;

    }
//数据总数
    @Override
    public int getCount() {
        // 空数据判断
        return mList ==null ? 0: mList.size();
    }

    /**
     * 获取每一条的item
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        return null;
    }
    /**
     * 获取某一个的id
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * 数据和布局是如何对应的
     * item---》回调
     * @param position 当前item的位置
     * @param convertView 服用的view布局
     * @param container  容器
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Log.d(TAG,"convertView"+convertView);
        String itemData= mList.get(position);
        // 拿到一条数
        // 每一条item如果显示的
        // 加载每一条布局
        // 当第一次进去 没有初始化过的时候才加载
        // 引入控件换性能 找到第三方那view
        ViewHolder viewHolder =null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_layout,container,false);
            TextView itemtext = convertView.findViewById(R.id.ll_home_name);
            itemtext.setText(itemData);
            convertView.setTag(viewHolder);

        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
//        LinearLayout itemLayout = view.findViewById(R.id.item_home);

//        itemtext.setText(itemData);
        //
        return convertView;
    }
    class ViewHolder{
        TextView textView;
    }
}
