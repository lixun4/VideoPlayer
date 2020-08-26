package com.lib.videoplayer.base;

import android.os.Bundle;
import android.widget.Toast;

import com.lib.videoplayer.video.VideoPlayerManager;

import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class BaseVideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
        VideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给VideoPlayer先处理。
        if (VideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }
    private void initSo() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Throwable e) {
            Toast.makeText(this,"播放器不支持此设备",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }
}
