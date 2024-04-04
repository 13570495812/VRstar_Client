package com.example.androidclientpico.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidclientpico.R;

import java.io.File;
import java.io.IOException;

public class video extends AppCompatActivity {

    private VideoView videoView;
    private Button bplay;
    private Button bstop;
    private MediaPlayer mediaPlayer;
    MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mediaPlayer = new MediaPlayer();
        init_vieofile();
    }
    private void init_vieofile() {
        mediaPlayer.reset();
        System.out.printf("11111111111");
        videoView = findViewById(R.id.videoView);
//        bplay = findViewById(R.id.view_ad);
//        bstop = findViewById(R.id.view_stop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("name");
            mediaPlayer = new MediaPlayer();
            String videoPath = value;

            try {
                File file = new File(videoPath);
                if (!file.exists()) {
                    // 文件不存在
                    Log.e("7777777777","klkjljlj");
                }else {
                    videoView.setVideoPath(videoPath);
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
//                mediaPlayer.reset();

//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setDataSource(videoPath);
//                File file = new File(Environment.getExternalStorageDirectory(), "1.mp4");
//                mediaPlayer.setDataSource(file.getAbsolutePath());
//                mediaPlayer.setDataSource(videoPath);
                // 缓冲
//                mediaPlayer.prepare();
                // 开始播放
//                mediaPlayer.start();
                // 具体位置
//                Log.i("position", position + "------");
//                mediaPlayer.seekTo(position);
//                mediaPlayer.setDataSource(videoPath);
//                mediaPlayer.prepare();
//                mediaPlayer.start();

//                if (v == playBtn) {
//                    mVideoView.start();
//                } else if (v == stopBtn) {
//                    mVideoView.stopPlayback();
//                }
//                String uri = videoPath + R.raw.as;
//                videoView.setVideoURI(Uri.parse(uri));
//                videoView.start();
            } catch (Exception e) {

            }
            Log.e("jkkjlljljljljlj","44444444444444444"+value);
        }
//        bplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.start();
//            }
//        });

//        vd.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/rew/"+R.raw.as));
//
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
