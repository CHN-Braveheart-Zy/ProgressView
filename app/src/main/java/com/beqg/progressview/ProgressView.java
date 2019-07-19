package com.beqg.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressView extends View {
    private static final String TAG = "ProgressView";

    //默认宽高,背景色,进度颜色
    private int mWidth = 600;
    private int mHeight = 46;
    private int mBgColor = 0;
    private int mProgressColor = 0;
    private int mProgressTextColor;
    private int mMaxProgress;
    private Paint mPaint;
    private int mCurrentProgres = 0;
    private int mTextSpace = 15;//进度条与文字间距
    private int mTextSize;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mProgressTextColor = typedArray.getColor(R.styleable.ProgressView_progress_text_color, Color.BLACK);
        mBgColor = typedArray.getColor(R.styleable.ProgressView_background_color,
                Color.parseColor("#F2F2F2"));
        mProgressColor = typedArray.getColor(R.styleable.ProgressView_progress_color,
                Color.parseColor("#F5D356"));
        mMaxProgress = typedArray.getInteger(R.styleable.ProgressView_max_value, 100);
        mTextSize = typedArray.getInteger(R.styleable.ProgressView_text_size, 45);
        if (typedArray != null) {
            typedArray.recycle();
        }
        Log.d(TAG, "ProgressView: constructor");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.AT_MOST) {
            width = mWidth;
        } else {
            width = widthSize;
        }
        int height = 0;
        if (heightMode == MeasureSpec.AT_MOST) {
            height = mHeight;
        } else {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //当前进度
        float percentage = mCurrentProgres * 100 / mMaxProgress;
        Rect percentageRect = new Rect();
        mPaint.setTextSize(mTextSize);
        String rate = (int) percentage + "%";
        mPaint.getTextBounds(rate, 0, rate.length(), percentageRect);
        mPaint.setColor(mProgressTextColor);
        //让文字位于进度条垂直居中
        int percentageHeight = percentageRect.bottom - percentageRect.top;
        int startY = (getMeasuredHeight() - percentageHeight) / 2 + percentageHeight;
        //画进度文字
        canvas.drawText(rate, getMeasuredWidth() - (percentageRect.right - percentageRect.left) - mTextSpace, startY, mPaint);

        //画背景
        mPaint.reset();
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(getHeight());
        int startX = getHeight() / 2;
        int progressBarBgWidth = getMeasuredWidth() - 80 - mTextSpace - getMeasuredHeight();
        canvas.drawLine(startX, startX, progressBarBgWidth, startX, mPaint);
        mPaint.setColor(mProgressColor);
        int progress = (int) (progressBarBgWidth * (percentage / 100.0f));
        Log.d(TAG, "onDraw: progress=" + progress + ", progressBarBgWidth=" + progressBarBgWidth);
        if (progress > 0) {
            canvas.drawLine(startX, startX, progress, startX, mPaint);
        }
    }


    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public int getCurrentProgres() {
        return mCurrentProgres;
    }

    public void setCurrentProgres(int currentProgres) {
        if (currentProgres > mMaxProgress) {
            throw new IllegalArgumentException("currentProgres cannot be greater than mMaxProgress");
        }
        mCurrentProgres = currentProgres;
        postInvalidate();
    }

    public int getTextSpace() {
        return mTextSpace;
    }

    public void setTextSpace(int textSpace) {
        mTextSpace = textSpace;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }
}
