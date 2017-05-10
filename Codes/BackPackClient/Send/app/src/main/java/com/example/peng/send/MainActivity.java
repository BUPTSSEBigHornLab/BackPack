package com.example.peng.send;

/*
 *Create by:ZhangYunpeng
 *Date:2017/03/29
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.app.AlertDialog.Builder;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.peng.send.R.id.edit;

public class MainActivity extends Activity {


    public static BluetoothSPP bt;
    private  EditText edittext;
    private  TextView textview;
    private Timer mtimer;
    private int TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this);

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        /**************************************蓝牙配置页面*************************************/
        //监听蓝牙状态
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {

            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "连接到" + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "连接断开", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "连接不可用", Toast.LENGTH_SHORT).show();
            }
        });
        //检查当前蓝牙状态，并连接蓝牙
        Button btbluetooth = (Button) findViewById(R.id.bluetooth);
        btbluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        Button send = (Button)findViewById(R.id.send);
        edittext = (EditText)findViewById(edit);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String to_send = edittext.getText().toString();
                timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
                //String to_send = "GMA";
                //bt.send(to_send.getBytes(),true);
            }
        });
        textview = (TextView) findViewById(R.id.text);
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //String res = new String(data);
                textview.setText(message);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                // Do something when data incoming
            }
        });
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String to_send = "GMA";
                bt.send(to_send.getBytes(),true);
            }
            super.handleMessage(msg);
        };
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public void onStart() {
        super.onStart();
        //检查蓝牙是否可用
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "蓝牙不可用"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

