package com.example.android.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.android.mediarecorder.R
import com.lib.videoplayer.bean.VideoInfo
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        initListener()
    }

    private fun initListener() {
        btn_play.setOnClickListener(this)
        btn_play_in_recyclerView.setOnClickListener(this)
        btn_play_in_fragment.setOnClickListener(this)
        btn_play_in_fragment_compat_home.setOnClickListener(this)
        btn_play_in_activity_compat_home.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
       when(v){
           btn_play->{
               //竖屏播放
               val videoThumbnail = ""
               // val videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4"
               val videoUrl = "https://vod.300hu.com/4c1f7a6atransbjngwcloud1oss/53e716ab164406729495875585/v.f30.mp4?dockingId=67bd36d0-2e04-4ee4-a4d5-dfb9f534ea7b&storageSource=3"
               val videoTitle = ""
               val videoInfo = VideoInfo(videoTitle, 0L, videoThumbnail, videoUrl)
               VideoActivity.intentTo(this,videoInfo)
           }
           btn_play_in_recyclerView->{
                  var intent=Intent(this,RecyclerViewActivity::class.java)
               startActivity(intent)
           }
           btn_play_in_fragment->{
               var intent=Intent(this,UseInFragmentActivity::class.java)
               startActivity(intent)
           }
           btn_play_in_fragment_compat_home->{
               var intent=Intent(this,VideoPressHomeActivity2::class.java)
               startActivity(intent)
           }
           btn_play_in_activity_compat_home->{
               val videoThumbnail = ""
               // val videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4"
               val videoUrl = "https://vod.300hu.com/4c1f7a6atransbjngwcloud1oss/53e716ab164406729495875585/v.f30.mp4?dockingId=67bd36d0-2e04-4ee4-a4d5-dfb9f534ea7b&storageSource=3"
               val videoTitle = "测试1"
               val videoInfo = VideoInfo(videoTitle, 0L, videoThumbnail, videoUrl)
               VideoPressHomeActivity.intentTo(this,videoInfo)
           }
       }
    }
}
