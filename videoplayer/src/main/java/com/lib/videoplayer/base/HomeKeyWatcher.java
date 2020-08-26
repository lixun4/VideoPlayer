package com.lib.videoplayer.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 监听Home键按下的Wathcer
 */
public class HomeKeyWatcher {
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    public interface OnHomePressedListener {
        /**
         * 短按Home键
         */
        void onHomePressed();

        /**
         * 长按 Home 键或者 Recent 键
         */
         void onHomeLongPressed();
         void onLockPressed();
    }

    public HomeKeyWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mRecevier = new InnerRecevier();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mRecevier != null) {
            mContext.unregisterReceiver(mRecevier);
        }
    }

    class InnerRecevier extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (mListener != null) {
                        if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                            // 短按home键
                            mListener.onHomePressed();
                        } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                            // 长按 Home 键或者 Recent 键
                            mListener.onHomeLongPressed();
                        } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                            // 锁屏
                            mListener.onLockPressed();

                        } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                            // Samsung 长按 Home 键
                            mListener.onHomeLongPressed();
                        } else {
                        }
                    }
                }
            }
        }
    }
}  