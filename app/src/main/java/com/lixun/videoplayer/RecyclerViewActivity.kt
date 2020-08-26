package com.example.android.play

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.adapter.VideoAdapter
import com.example.android.adapter.ViewHolder.VideoViewHolder
import com.example.android.mediarecorder.R
import com.lib.videoplayer.base.BaseVideoActivity
import com.lib.videoplayer.bean.VideoInfo
import com.lib.videoplayer.video.VideoPlayerManager
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : BaseVideoActivity() {
    var videoInfoList:MutableList<VideoInfo>?= mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        intData()
        initRecycllerView()
    }

    private fun intData() {
        videoInfoList!!.add(VideoInfo("测试1",58000,
                "http://img30.360buyimg.com/popWaterMark/jfs/t20104/338/514397684/416343/58fd5b7e/5afa8092N60e37774.jpg",
                "https://vod.300hu.com/4c1f7a6atransbjngwcloud1oss/53e716ab164406729495875585/v.f30.mp4?dockingId=67bd36d0-2e04-4ee4-a4d5-dfb9f534ea7b&storageSource=3"))
        videoInfoList!!.add(VideoInfo("测试2",108000,
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1035415831,1465727770&fm=26&gp=0.jpg",
                "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4"))
        videoInfoList!!.add(VideoInfo("测试3",58000,
                "",
                "https://vod.300hu.com/4c1f7a6atransbjngwcloud1oss/53e716ab164406729495875585/v.f30.mp4?dockingId=67bd36d0-2e04-4ee4-a4d5-dfb9f534ea7b&storageSource=3"))
        videoInfoList!!.add(VideoInfo("测试4",108000,
                "",
                "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4"))
    }

    private fun initRecycllerView() {
        recyclerview.setLayoutManager(LinearLayoutManager(this))
        recyclerview.setHasFixedSize(true)
        val adapter = VideoAdapter(this, videoInfoList!!)
        recyclerview.adapter = adapter
        recyclerview.setRecyclerListener(object:RecyclerView.RecyclerListener{
            override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
                val videoPlayer = (holder as VideoViewHolder).videoPlayer
                if (videoPlayer === VideoPlayerManager.instance().currentNiceVideoPlayer) {
                    VideoPlayerManager.instance().releaseNiceVideoPlayer()
                }
            }

        })


    }
}
