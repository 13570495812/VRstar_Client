package com.example.androidclientpico.ui.about;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidclientpico.R;
import com.example.androidclientpico.ui.home.IFragmentHomeCallback;
import com.example.androidclientpico.ui.set.IFragmentSetCallback;

public class AboutFragment extends Fragment {
    private final String TAG="AboutFragment";
    private VideoView vd;
    private Button payButton;
    private MediaController controller;
    private Uri uri;
    VideoView video_view;
    MediaController mc;
    private VideoView videoView;
    private IFragmentAboutCallback fragmentCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragmint_about,container,false);
//        payButton.setOnClickListener(this);
        init_vieofile();
        return view;
    }

    private void init_vieofile() {
        System.out.printf("11111111111");
//        vd.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/rew/"+R.raw.as));
//
    }
    /**
     * FRAGEMTN 回调函数
     * @param callback
     */
    public void setFragmentCallback(IFragmentAboutCallback callback){
        fragmentCallback = callback;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    /** 播放视频 */
        super.onActivityCreated(savedInstanceState);
       Button btnAd = getActivity().findViewById(R.id.btn_ad);
        Button btnStup = getActivity().findViewById(R.id.btn_stop);
        videoView = (VideoView)getActivity().findViewById(R.id.ll_about_videoView);
        btnAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "android.resource://" +getActivity().getPackageName() + "/" + R.raw.as;
               videoView.setVideoURI(Uri.parse(uri));
                videoView.start();
            }
        });
        /** 暂停视频 8/
         *
         */
        btnStup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoStup();
            }


        });
    }
    /*暂停视频*/
    public void videoStup() {
        videoView.pause();
//        Log.d(TAG,"1212  暂停暂停");
    }
    /*开始播放视频*/
    public void videoViewStart(){
        String uri = "android.resource://" +getActivity().getPackageName() + "/" + R.raw.as;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/rew/"+R.raw.as);

//        getActivity().setContentView(R.layout.fragmint_about);
//        VideoView videoView = (VideoView)getActivity().findViewById(R.id.ll_about_videoView);

        //加载指定的视频文件
//        String path = "android.resource://" +getActivity().getPackageName() + "/" + R.raw.as;
//        videoView.setVideoPath(path);
        //创建MediaController对象
//        MediaController mediaController = new MediaController(getActivity());
        //VideoView与MediaController建立关联
//        videoView.setMediaController(mediaController);

        //让VideoView获取焦点
//        videoView.requestFocus();

    }

    @Override
    public void onStart() {
        super.onStart();
//        videoStup();
        Log.d(TAG,"about ==================onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        videoStup();
        Log.d(TAG,"about ==================onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        videoStup();
        Log.d(TAG,"about ==================onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        videoStup();
        Log.d(TAG,"about ==================onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        videoStup();
        Log.d(TAG,"about ==================onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        videoStup();
        Log.d(TAG,"about =====================onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        videoStup();
        Log.d(TAG,"about ==================onDetach");
    }
}
