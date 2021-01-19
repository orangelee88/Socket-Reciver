package com.example.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.net.wifi.WifiInfo;

public class Reciver extends Activity {

    TextView test;
    TextView test2;

    private Handler handler = new Handler();
    private ServerSocket serverSocket;
    private String line ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test = (TextView) findViewById( R.id.test );
        test2 = (TextView) findViewById( R.id.test2 );

        //建立Thread
        Thread fst = new Thread(socket_server);
        //啟動Thread
        fst.start();

    }

    //取得IP
    private String getMyIp(){
        //新增一個WifiManager物件並取得WIFI_SERVICE
        WifiManager wifi_service = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        //取得wifi資訊
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        //取得IP，但這會是一個詭異的數字，還要再自己換算才行
        int ipAddress = wifiInfo.getIpAddress();
        //利用位移運算和AND運算計算IP
        String ip = String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        return ip;
    }


    private Runnable socket_server = new Runnable(){
        public void run(){
            handler.post(new Runnable() {
                public void run() {
                    test.setText("Listening...." + getMyIp());
                }
            });

            try{
                //建立serverSocket
                serverSocket = new ServerSocket(1234);

                //等待連線
                while (true) {
                    //接收連線
                    Socket client = serverSocket.accept();

                    handler.post(new Runnable() {
                        public void run() {
                            test.setText("Connected.");
                        }
                    });
                    try {
                        //接收資料
                        DataInputStream in = new DataInputStream(client.getInputStream());
                        line = in.readUTF();
                        while (line != null) {
                            handler.post(new Runnable() {
                                public void run() {
                                    test2.setText(line);
                                }
                            });
                        }
                        break;
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            public void run() {
                                test.setText("傳送失敗");
                            }
                        });
                    }
                }
            }catch(IOException e){
                handler.post(new Runnable() {
                    public void run() {
                        test.setText("建立socket失敗");
                    }
                });
            }
        }
    };

}