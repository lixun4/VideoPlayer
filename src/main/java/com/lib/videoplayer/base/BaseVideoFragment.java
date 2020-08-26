package com.lib.videoplayer.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lib.videoplayer.util.LogUtil;
import com.lib.videoplayer.video.VideoPlayerManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class BaseVideoFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSo();
    }


    @Override
    public void onStop() {
        LogUtil.i("BaseVideoFragment onStop");
        super.onStop();
        VideoPlayerManager.instance().releaseNiceVideoPlayer();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        IjkMediaPlayer.native_profileEnd();
    }
}
