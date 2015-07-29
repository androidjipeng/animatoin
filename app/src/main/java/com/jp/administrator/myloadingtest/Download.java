package com.jp.administrator.myloadingtest;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2015/7/9.
 */
public class Download {
    public static Context mContext;
    static long count = 0;
    static int contentLength=0;
    static Handler handler;
    static Thread thread;
    static ProgressDialog dialog;
    public Download(Context context) {
        this.mContext = context;

    }
    static OutputStream os;
    static File file;
    static String newFileName;
    public static void addProgressBar(final Context context, final String url) {
        mContext=context;
/*
通过自定义加载布局
      LayoutInflater inflater=LayoutInflater.from(context);
      View view = inflater.inflate(R.layout.progresslayout,null);
      final ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.downLoadProgressBar);
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setMax(100);
      final TextView tvContent= (TextView) view.findViewById(R.id.tvContent);
* */
        ShowDialogProgressBar(context);

          handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    //打开安装界面，进行安装
                    Intent i=new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.parse("file://" + newFileName),"application/vnd.android.package-archive");
                    context.startActivity(i);
                    Toast.makeText(context, "contentLength:" + contentLength, Toast.LENGTH_SHORT).show();
                }
                thread.interrupt();
                Toast.makeText(context, "contentLength:" + contentLength, Toast.LENGTH_SHORT).show();


            }
        };

        thread=new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                downloadResource(context,url);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    public static void ShowDialogProgressBar(final Context context) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setIcon(R.drawable.ic_launcher_download);// 设置提示的title的图标，默认是没有的
        dialog.setTitle("正在下载...");
        dialog.setMessage("请稍候...");
        dialog.setMax(100);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "后台下载:", Toast.LENGTH_SHORT).show();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(R.drawable.ic_launcher_cell_broadcast, "正在后台下载", System.currentTimeMillis());
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                notification.flags = Notification.FLAG_AUTO_CANCEL;

                Intent intent = new Intent(context, NotifService.class);

//                Intent intent=new Intent();
//                intent.setAction("com.jp.administrator.myloadingtest.NotifService");
//                Bundle bundle=new Bundle();
//                bundle.putParcelable("MyContext",context);
//                intent.putExtra("MyContext",context);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //用于跳转activity    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.)
                PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                  /*
                  * 下拉菜单时所显示的内容
                  * */
                notification.setLatestEventInfo(context, "正在下载", "downloading plese waiting!", pendingIntent);
                notificationManager.notify(3, notification);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, "取消:", Toast.LENGTH_SHORT).show();
                if (thread!=null||os!=null)
                {
                    Toast.makeText(context, "取消:", Toast.LENGTH_SHORT).show();
//                    thread.interrupt();
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);

                }
                if (file!=null)
                {
                    deleteFile(file);
                }

            }
        });
        dialog.show();
    }


    private static void downloadResource(Context context,String path) {
        /*
        * 前期下载准备
        * */
        String sdkpath = Environment.getExternalStorageDirectory() + "/MyDownLoad/";
        File f = new File(sdkpath);
        if (!f.exists()) {
            f.mkdir();
        }
        String urlDownLoading = path;
       /*
       * 开始下载操作
       * */
        newFileName = urlDownLoading.substring(urlDownLoading.lastIndexOf("/") + 1);
        newFileName = sdkpath + newFileName;
        file = new File(newFileName);
        deleteFile(file);
        try {
            //构造URL
            URL url = new URL(urlDownLoading);
            //打开连接
            URLConnection urlConnection = url.openConnection();
            //获取文件长度
            contentLength = urlConnection.getContentLength();
            int avg=contentLength/100;
            System.out.println("平均长度==========>>>>>>>>>" + avg);
            System.out.println("文件的长度==========>>>>>>>>>" + contentLength);
            //读取输入流
            InputStream inputStream = urlConnection.getInputStream();
            //设置数据缓存1kb(每次读取的量)
            byte[] bytes = new byte[1024];
            //读取数据的长度
            int len;
            //输出的数据流
            int i=0;
            os = new FileOutputStream(newFileName);
            //开始读取数据
            while ((len = inputStream.read(bytes)) != -1) {
                count=count+len;
                System.out.println("count========>>>"+count);
                os.write(bytes, 0, len);
                if ((count-avg)>0)
                {
                    System.out.println("条件--->>>>>>>>>>"+(count-avg));
                    count=0;
                    System.out.println("count清零--->>>>>>>>>>"+count);
                    i++;
                    dialog.setProgress(i);
//                    dialog.incrementProgressBy(i);//有点问题，和实际下载量不同
                }
//                dialog.incrementProgressBy(count);
//                handler.sendEmptyMessage(0);
            }

            //关闭输出输入流
            os.close();
            inputStream.close();
            dialog.dismiss();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        String apkUrl="http://gdown.baidu.com/data/wisegame/c116604781efaae6/tuniulvyou_62.apk";
//        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(apkUrl));
//        request.setDestinationInExternalPublicDir("MyDownLoad", "tuniulvyou_62.apk");
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        long downloadId = downloadManager.enqueue(request);
//        request.allowScanningByMediaScanner();//表示允许MediaScanner扫描到这个文件，默认不允许。
//        request.setTitle("MeiLiShuo");//设置下载中通知栏提示的标题
//        request.setDescription("MeiLiShuo desc");//设置下载中通知栏提示的介绍
////        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

}
