package com.jp.administrator.myloadingtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/7/15.
 */
public class FloatWindowBigView extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowBigView(final Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.bigwindowlayout, this);
        final View view = findViewById(R.id.big_window_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    MyWindowManager.removeBigWindow(context);
                    MyWindowManager.createSmallWindow(context);
                }
                return false;
            }
        });
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        setListener(context);

    }

    private void setListener(final Context context) {
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);
//        final ImageView img= (ImageView) findViewById(R.id.img);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.createSmallWindow(context);
            }
        });
        
//        img.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                MyWindowManager.getUsedPercentValue(context);
//                  int a=MyWindowManager.percent;
//                System.out.println("--------------->"+a);
//            }
//        });

    }
}
