package com.beqg.progressview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CountdownView extends LinearLayout {

    private TextView mTvHour;
    private TextView mTvMinute;
    private TextView mTvSecond;
    private long mCountdownTime;
    private OnCountdownCompleteListener mListener;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.module_countdown_view, this, true);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvHour = findViewById(R.id.tv_countdown_hour);
        mTvMinute = findViewById(R.id.tv_countdown_minute);
        mTvSecond = findViewById(R.id.tv_countdown_second);
    }

    /**
     * 设置倒计时毫秒
     *
     * @param countdownMillisecond
     */
    public void setCountdownMS(long countdownMillisecond) {
        setCountdownSecond(countdownMillisecond / 1000);
    }

    /**
     * 设置倒计时秒
     *
     * @param seconds
     */
    public void setCountdownSecond(long seconds) {
        mCountdownTime = seconds * 1000;
        formatDate(seconds);
    }

    private void formatDate(final long seconds) {
        int hour = (int) (seconds / 3600);
        int minute = (int) (seconds % 3600 / 60);
        int second = (int) (seconds % 60);
        mTvHour.setText(hour < 10 ? "0" + hour : hour + "");    //小于10前面补0
        mTvMinute.setText(minute < 10 ? "0" + minute : minute + "");
        mTvSecond.setText(second < 10 ? "0" + second : second + "");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (seconds < 1) {
                    if (mListener != null) {
                        mListener.onComplete(mCountdownTime);
                    }
                } else {
                    formatDate(seconds - 1);
                }
            }
        }, 1000);
    }

    public interface OnCountdownCompleteListener {
        void onComplete(long countdownTime);
    }

    public void setOnCountdownCompleteListener(OnCountdownCompleteListener listener) {
        mListener = listener;
    }
}
