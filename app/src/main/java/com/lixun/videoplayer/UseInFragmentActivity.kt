package com.lixun.videoplayer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lib.videoplayer.video.VideoPlayerManager

class UseInFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_in_fragment)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, DemoFragment())
                .commit()
    }

    override fun onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressd()) {
            return
        }
        super.onBackPressed()
    }
}
