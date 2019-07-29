package com.beqg.progressview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyPopupView extends FrameLayout implements View.OnTouchListener {

    private WindowManager.LayoutParams mDecorLayoutParams;
    private View mDecor;
    private Object mInstance;
    private Context mContext;
    private View mRoot;
    private WindowManager mWm;
    private boolean mShowing;

    public MyPopupView(@NonNull Context context) {
        this(context,null);
    }

    public MyPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayoutparames();
        mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        try {
            Class clazz = Class.forName("com.android.internal.policy.PhoneWindow");
            Constructor constructor = clazz.getConstructor(Context.class);
            mInstance = constructor.newInstance(context);
            Method method = clazz.getMethod("setWindowManager", WindowManager.class, IBinder.class, String.class);
            method.invoke(mInstance, mWm,null,null);
            Method method1 = clazz.getMethod("setContentView", View.class);
            method1.invoke(mInstance,this);
            Method method2 = clazz.getMethod("getDecorView");
            mDecor = (View) method2.invoke(mInstance);
            mDecor.setOnTouchListener(this);
            mDecor.setBackgroundColor(0x00FFFFFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLayoutparames() {
        mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = Gravity.BOTTOM | Gravity.CENTER;
        p.height = LayoutParams.WRAP_CONTENT;
        p.x = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        p.token = null;
    }

    public void setAnchorView() {

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.module_popup_view, null);
        return mRoot;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mShowing) {
            mWm.removeView(mDecor);
            mShowing = false;
        }
        return false;
    }

    public void hide() {
        mShowing = false;
        mWm.removeView(mDecor);
    }

    public void show() {
        mWm.addView(mDecor, mDecorLayoutParams);
        mShowing = true;
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void setShowing(boolean showing) {
        mShowing = showing;
    }
}
