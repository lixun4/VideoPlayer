package com.example.android.play

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.mediarecorder.R
import com.lib.videoplayer.video.VideoPlayerManager

class VideoPressHomeActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_in_fragment)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, DemoCompatHomeKeyFragment())
                .commit()
    }

    override fun onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressd()) {
            return
        }
        super.onBackPressed()
    }
}
