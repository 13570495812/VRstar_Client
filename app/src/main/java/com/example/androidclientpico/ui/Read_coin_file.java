package com.example.androidclientpico.ui;

import static com.example.androidclientpico.ui.MainActivity.M_this;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Read_coin_file {
    String IsItFree = "Free.txt";  // 是否免费
    /*是否免费模式*/
    String is_ferr = "";
    String minutes_number= "";

    public String ferr() {
        return GetFerr();
    }
    /**
     * 修改时间
     */
    public void changeTime(String time){
        minutes_number=time;
    }

    /**
     * 修改时间
     */
    public void changeFerr(String ferr){
        is_ferr=ferr;
        Save_Free();
    }
/*返回是否免费状态*/
    public String Return_to_statu_ferr(){
        return GetFerr();
    }
    /*保存免费模式*/
    public void Save_Free() {
//        Toast.makeText(M_this, getSerialNumber(), Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fileCoun = M_this.openFileOutput(IsItFree, Context.MODE_PRIVATE);
            fileCoun.write(is_ferr.getBytes());
            fileCoun.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        /*保存IP提醒框*/

    /**
     * IP获取
     *
     * @return
     */

    public String GetFerr() {
        try {
            FileInputStream inStream = M_this.openFileInput(IsItFree);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            is_ferr = StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is_ferr;
    }

}
