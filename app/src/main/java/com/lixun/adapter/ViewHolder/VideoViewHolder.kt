package com.example.android.adapter.ViewHolder

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lib.videoplayer.bean.VideoInfo
import com.lib.videoplayer.video.TxVideoPlayerController
import com.lib.videoplayer.video.VideoPlayer
import com.lixun.videoplayer.R

class VideoViewHolder: RecyclerView.ViewHolder{
    var playerController:TxVideoPlayerController?=null
    var videoPlayer:VideoPlayer?=null
    var context:Context?=null
    constructor(context:Context, itemView: View) : super(itemView) {
        this.context=context
        videoPlayer=itemView.findViewById(R.id.video_player)
        if(videoPlayer!=null){
            // 将列表中的每个视频设置为默认16:9的比例
            val params = videoPlayer!!.getLayoutParams()
            params.width = itemView.resources.displayMetrics.widthPixels // 宽度为屏幕宽度
            params.height = (params.width * 9f / 16f).toInt()    // 高度为宽度的9/16
            videoPlayer!!.setLayoutParams(params)
        }
    }
    fun setController(playerController:TxVideoPlayerController){
        this.playerController=playerController
        videoPlayer!!.setController(playerController)
    }
    fun bindData(data:VideoInfo){
        var videoTitle=data.title
        var videoThumbnail=data.imageUrl
        var videoUrl=data.videoUrl

        videoPlayer!!.setPlayerType(VideoPlayer.TYPE_IJK) // IjkPlayer or MediaPlayer
        videoPlayer!!.setUp(videoUrl, null)

        if (!TextUtils.isEmpty(videoTitle))
            playerController!!.setTitle(videoTitle)
        Glide.with(context!!)
                .load(videoThumbnail)
               // .placeholder(com.lib.videoplayer.R.drawable.img_default)
                .centerCrop()
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        playerController!!.imageView().setImageDrawable(resource)
                        return false
                    }

                })
                .into(playerController!!.imageView())
        playerController!!.setLenght(data.length)

    }

}