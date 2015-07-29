package com.jp.administrator.myloadingtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
   private Context mContext;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        tv = new TextView(mContext);
    }

    public void onLoadingDown(View v)
    {
        String url="http://gdown.baidu.com/data/wisegame/a825da243c39db06/shoujibaidu_16787468.apk";
        Download.addProgressBar(mContext,url);
//        addDownLoad();
    }

    public void ClearAnimation(View v)
    {
        Intent intent=new Intent(this,FloatWindowService.class);
        startService(intent);
    }





    String myString = null;

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv.setText(myString);
            setContentView(tv);
        }
    };

    private void addDownLoad() {


        new Thread(new Runnable() {
            @Override
            public void run() {
//                String myString = null;
                try {
                    URL uri = new URL("http://blog.csdn.net/yhb5566/article/details/6504563");//注意，这里的URL地址必须为网络地址，
                    //URL uri = new URL("http://localhost:8080/my/poem.txt");
                    //本地地址http://localhost:8080/my/poem.txt会报Connection Refused的异常
                    URLConnection ucon = uri.openConnection();

                    System.out.println("==========>返回数据大小为："+ucon.getContentLength());
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayBuffer baf = new ByteArrayBuffer(100);
                    int current = 0;
                    while((current = bis.read()) != -1) {
                        baf.append((byte)current);
                    }

                    myString = new String(baf.toByteArray(), "utf-8");
                    //myString = EncodingUtils.getString(baf.toByteArray(), "GBK");
                    //myString = new String(baf.toByteArray());这个出现乱码，要在txt文件保存时选中utf-8
                } catch(Exception e) {
//                    myString = e.getMessage();
                }
                Message msg=new Message();
                msg.what=1;
                handler.sendEmptyMessage(0);
            }


        }).start();



        tv.setText(myString);
        this.setContentView(tv);
    }
}
