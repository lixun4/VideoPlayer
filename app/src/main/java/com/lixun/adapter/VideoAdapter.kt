package com.example.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.adapter.ViewHolder.VideoViewHolder
import com.lib.videoplayer.bean.VideoInfo
import com.lib.videoplayer.video.TxVideoPlayerController
import com.lixun.videoplayer.R

class VideoAdapter(context: Context, var data: MutableList<VideoInfo>): RecyclerView.Adapter<VideoViewHolder>() {
     var videoInfoList:MutableList<VideoInfo>?=null
     var context:Context?=null
    init{
        this.videoInfoList=data
        this.context=context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        var itemView=LayoutInflater.from(context).inflate(R.layout.layout_item_video,parent,false)
        var viewHolder=VideoViewHolder(context!!,itemView)
        var controller=TxVideoPlayerController(context)
        viewHolder.setController(controller)
        return  viewHolder
    }

    override fun getItemCount(): Int {
       return if(videoInfoList==null) 0 else videoInfoList!!.size

    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        var item=videoInfoList!![position]
         holder.bindData(item)
    }
}