package com.example.android.play

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lib.videoplayer.R
import com.lib.videoplayer.base.BaseVideoActivity
import com.lib.videoplayer.bean.VideoInfo
import com.lib.videoplayer.video.TxVideoPlayerController
import com.lib.videoplayer.video.VideoPlayer
import kotlinx.android.synthetic.main.activity_video_vertical.*

/**
 * onStop 释放播放器
 */
class VideoActivity : BaseVideoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_vertical)
        init()
        video_player.start()

    }

    private fun init() {
        val videoInfo = intent.getSerializableExtra("videoInfo") as VideoInfo
        if (videoInfo != null) {
            val videoUrl = videoInfo.videoUrl
            if (TextUtils.isEmpty(videoUrl)) {
                return
            }
            val videoTitle = videoInfo.title
            val videoThumbnail = videoInfo.imageUrl

            video_player.setPlayerType(VideoPlayer.TYPE_IJK) // IjkPlayer or MediaPlayer
            video_player.setUp(videoUrl, null)
            val controller = TxVideoPlayerController(this)
            controller.setLenght(videoInfo.length)
            if (!TextUtils.isEmpty(videoTitle))
                controller.setTitle(videoTitle)
            Glide.with(this)
                    .load(videoThumbnail)
                    .centerCrop()
                    .dontAnimate()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            controller.imageView().setImageDrawable(resource)
                            return false
                        }

                    })
                    .into(controller.imageView())
            controller.hideChangeBrightness()
            controller.hideChangeVolume()
            controller.hideFullScreen()
            video_player.setController(controller)
        }
    }

    companion object {

        fun intentTo(context: Context, videoInfoInfo: VideoInfo) {
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra("videoInfo", videoInfoInfo)
            context.startActivity(intent)
        }
    }
}
