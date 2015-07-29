package com.jp.administrator.myloadingtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import android.view.View;

/**
 * Created by Administrator on 2015/7/14.
 */
public class ClearAnimationView extends View {
    public ClearAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClearAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearAnimationView(Context context) {
        super(context);
    }

    MyHandler handler;
    private Paint bluePaint;
    private Paint greenPaint;
    private Paint wordPaint;
    int j;
    Thread thread;
    Canvas mCanvas;
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas=canvas;
        handler=new MyHandler();
        init();
        canvas.drawCircle(300, 82, 80, bluePaint);//画圆
        j=MyWindowManager.percent;//内存占比数量
        if (thread==null)
        {
        thread=new Thread()
         {
           @Override
           public void run() {
               int i=0;
              while (i<j)
              {
                  i++;
                  try {
                      Thread.sleep(50);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  Message msg=new Message();
                  Bundle b=new Bundle();
                  b.putInt("count", i);
                  msg.setData(b);
                  msg.what=1;
                  handler.sendMessage(msg);
              }
               if (i>=j)
               {
                   Message msg=new Message();
                   msg.what=2;
                   handler.sendMessage(msg);
               }

           }
       };
        thread.start();
        }else
        {
            return;
        }

    }

    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count;
            if (msg.what==1)
            {
                System.out.println("msg.what==1------------------->");
                count=msg.getData().getInt("count");
                drawNunber(mCanvas, count);
                postInvalidate();
                System.out.println("msg.what==1------------------->"+":"+"msg.what==1------------------->");
            }
            if (msg.what==2)
            {
                System.out.println("msg.what==2------------------->");
//                thread.interrupt();
                thread=null;
                drawWord(mCanvas);
//                invalidate();
                System.out.println("msg.what==2------------------->"+":"+"msg.what==2------------------->");

            }

        }
    }
    private void drawWord(Canvas canvas)
    {
        canvas.drawText("深度清理",250, 120, wordPaint);
        System.out.println("2222222222222222222222222");
    }
    private void drawNunber(Canvas canvas,int count) {
        canvas.drawText(count+""+"%",250, 85, greenPaint);
        System.out.println("11111111111111111111111111");
    }


    private void init() {
        /*画文字
        * */
        if (wordPaint==null)
        {
            wordPaint=new Paint();
            wordPaint.setColor(Color.GREEN);
            wordPaint.setTextSize(25);
            wordPaint.setAntiAlias(true);//抗锯齿
        }


            /*画数字
        * */
        if (greenPaint==null)
        {
            greenPaint=new Paint();
            greenPaint.setColor(Color.GREEN);
            greenPaint.setTextSize(50);
            greenPaint.setAntiAlias(true);//抗锯齿
        }

           /*
        画圆
        * */
        if (bluePaint==null)
        {
            bluePaint = new Paint();
            bluePaint.setColor(Color.BLUE);
            bluePaint.setTextSize(10);
            bluePaint.setAntiAlias(true);
            bluePaint.setStyle(Paint.Style.STROKE);//画空心圆
            bluePaint.setStrokeWidth(5);
        }

    }

}
