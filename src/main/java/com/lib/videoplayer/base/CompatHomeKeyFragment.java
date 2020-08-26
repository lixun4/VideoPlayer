package com.lib.videoplayer.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.lib.videoplayer.util.LogUtil;
import com.lib.videoplayer.video.VideoPlayerManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 *
 * 在此Fragment中，如果视频正在播放或缓冲，按下按下除Back键（比如Home键、最近任务列表键、锁屏键等），暂停视频播放，回到此Fragment后继续播放视频；
 * 如果离开次Fragment（跳转到其他Activity或按下Back键），则释放视频播放器
 */
public class CompatHomeKeyFragment extends Fragment {

    private boolean pressedBack=false;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSo();
    }

    @Override
    public void onStart() {
        super.onStart();
        pressedBack=false;
        VideoPlayerManager.instance().resumeNiceVideoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了back键
        if (pressedBack) {
            VideoPlayerManager.instance().releaseNiceVideoPlayer();
        } else {
            VideoPlayerManager.instance().suspendNiceVideoPlayer();
        }
       IjkMediaPlayer.native_profileEnd();
    }
    private void initSo() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Throwable e) {
            Toast.makeText(getActivity(),"播放器不支持此设备",Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

}
