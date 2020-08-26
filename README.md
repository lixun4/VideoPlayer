# VieoPlayer

### Features

 * 用IjkPlayer/MediaPlayer + TextureView封装，可切换IjkPlayer、MediaPlayer.

 * 支持本地和网络视频播放（支持https）

 * 支持全屏播放

 * 手势滑动调节播放进度、亮度、声音.

 * 可自定义控制界面

 * 可引入外部的.so文件，覆盖本库中的.so文件

   

### Usage
**在对应视频界面所在的Activity的Manifest.xml中需要添加如下配置：**
```
android:configChanges="orientation|keyboardHidden|screenSize"
```

#### 1.在Activity中使用
在Activity中使用时，该Activity需要继承自BaseVideoActivity或参考BaseVideoActivity

（onCreate中初始化so文件

onStop中需要释放播放器

onBackPress中处理按下返回键的逻辑

onDestroy中关闭Native库）

中的代码重新编写

```java
public class BaseVideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
        VideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给VideoPlayer先处理。
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
    protected void onDestroy() {
        super.onDestroy();
        //关闭Native库
        IjkMediaPlayer.native_profileEnd();
    }
}
```
初始化VideoPlayer

```java
videoPlayer = (NiceVideoPlayer) findViewById(R.id.video_player); videoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
videoPlayer.setUp(mVideoUrl, null);
  
TxVideoPlayerController controller = new TxVideoPlayerController(this);
videoPlayer.setTitle(mTitle);
controller.setImage(mImageUrl);
videoPlayer.setController(controller);
```

详细可参考demo中的VideoActivity、RecyclerViewActivity

#### 2.在Fragment中使用
在Fragment中使用时，该Fragment外层的Activity需要继承自AppCompatActivity，并且也要处理返回键按下逻辑：
```java
public class XXXActivity extends AppCompatActivity {
    ...
    @Override
    public void onBackPressed() {
    // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
    // 所以在Activity中onBackPress要交给VideoPlayer先处理。
    if (NiceVideoPlayerManager.instance().onBackPressd()) return;
    super.onBackPressed();
}
    ...
}
```
同时在Fragment中继承BaseVideoFragment或参考BaseVideoFragment重新编写：
```java
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
  
    @Override
    public void onDestroyView() {
        super.onDestroyView();
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


```
详细可参考demo中的UseInFragmentActivity`和`DemoFragment

#### 3.在RecyclerView列表中使用
在ReclerView列表中使用时需要监听itemView回收，以此释放掉对应的播放器
```java
 recyclerview.setRecyclerListener(object:RecyclerView.RecyclerListener{
            override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
                val videoPlayer = (holder as VideoViewHolder).videoPlayer
                if (videoPlayer === VideoPlayerManager.instance().currentNiceVideoPlayer) {
                    VideoPlayerManager.instance().releaseNiceVideoPlayer()
                }
            }

        })
```
详细参考demo中的RecyclerViewActivity
#### 4.播放时Home键按下以及回到播放界面的处理
按照上面的做法，在onStop直接释放掉播放器，那么在播放时按下Home键播放器也会被释放掉，如果在此回到播放界面，播放器回到最初始的状态。如果需要在播放的时候按下Home键只是暂停播放器，重新回到播放界面时又继续播放，那么可以参考demo中的CompatHomeKeyActivity，或者对应的Activity集成自CompatHomeKeyActivity，详细参考demo中的VideoPressHomeActivity。当然，如果是在Fragment中，参考CompatHomeKeyFragment，或者继承自CompatHomeKeyFragment（外层的Activity还是继承自AppCompat，并处理onBackPress)，详细参考demo中的VideoPressHomeActivity2.

```java
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
   private void initSo() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Throwable e) {
            Toast.makeText(this,"播放器不支持此设备",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
```

```java
/**
 *
 * 在此Fragment中，如果视频正在播放或缓冲，按下按下除Back键（比如Home键、最近任务列表键、锁屏键等），暂停视    频播放，回到此Fragment后继续播放视频；
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
```



#### 5.自定义控制界面
```java
public class CustomController extends VideoPlayerController {
    // 实现自己的控制界面
    ...
}
```