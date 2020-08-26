package com.lib.videoplayer.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lib.videoplayer.util.LogUtil;
import com.lib.videoplayer.video.VideoPlayerManager;

import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 在此Activity种，如果视频正在播放或缓冲，按下除Back键（比如Home键、最近任务列表键、锁屏键等），暂停视频播放，回到此Activity后继续播放视频；
 * 如果离开次Activity（跳转到其他Activity或按下Back键），则释放视频播放器
 */
public class CompatHomeKeyActivity extends AppCompatActivity {

    private boolean pressedBack=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pressedBack = false;
        VideoPlayerManager.instance().resumeNiceVideoPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了back键
        if (pressedBack) {
            VideoPlayerManager.instance().releaseNiceVideoPlayer();
        } else {
            VideoPlayerManager.instance().suspendNiceVideoPlayer();
        }
    }

    @Override
    public void onBackPressed() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //返回键
            pressedBack = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }
}
