package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends AppCompatActivity {

    TextView test;
    EditText editText;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_client );

        test = (TextView) findViewById( R.id.test );
        editText = (EditText) findViewById( R.id.et1 );

    }
    public void onClick(View V) {
        data = editText.getText().toString();//轉型別
        new Thread( new Runnable() {
            @Override
            public void run() {
                Socket();
            }

        } ).start();
    }

    public void Socket() {
        InetAddress serverAddr = null;
        SocketAddress sc_add = null;
        Socket socket = null;
        //要傳送的字串
        //String data = "Hello Socket";
        try {
            //設定Server IP位置
            serverAddr = InetAddress.getByName( "192.168.0.181" );
            //設定port:1234
            sc_add = new InetSocketAddress( serverAddr, 1234 );

            socket = new Socket();
            //與Server連線，timeout時間2秒
            socket.connect( sc_add, 2000 );

            //傳送資料
            DataOutputStream out = new DataOutputStream( socket.getOutputStream() );
            out.writeUTF( data );

            //關閉socket
            socket.close();

        } catch (UnknownHostException e) {
            test.setText( "InetAddress物件建立失敗" );
        } catch (SocketException e) {
            test.setText( "socket建立失敗" );
        } catch (IOException e) {
            test.setText( "傳送失敗" );
        }
    }

}