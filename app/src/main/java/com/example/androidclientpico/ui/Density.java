package com.example.androidclientpico.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

public class Density {

    private static float appDensity;//表示屏幕密度
    private static float appScaleDensity; //字体缩放比例，默认appDensity

    public static void setDensity(final Application application, Activity activity){
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        appDensity = 0;
        appScaleDensity = 0;
        Log.i("TAG", "setDensity: "+displayMetrics.widthPixels + " "+ displayMetrics.heightPixels + " "+ displayMetrics.densityDpi+ " " + displayMetrics.density);
        if (appDensity == 0){
            //初始化赋值操作
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig != null && newConfig.fontScale > 0){
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        //计算目标值density, scaleDensity, densityDpi
        //Neo3设置1 G2 4K设置3.5f
        float targetDensity = 1;
        if (isG24K()) {
            targetDensity = 3.5f;
        }else {
            targetDensity = 1.25f;
        }
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换Activity的density, scaleDensity, densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;
    }

    public static boolean isG24K() {
        return Build.MODEL.equals("Pico G2 4K");
    }

}
