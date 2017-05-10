/*
 *Create by:ZhangYunpeng
 *Date:2017/03/30
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 */
package com.example.peng.backpack;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class MonitorActivity extends AppCompatActivity {
    /** 用于存储各个通道数据的列表，MainItem中包含了三列数据信息 ，通道号、测量值、状态*/
    private List<String> list;
    private ArrayAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        list = new ArrayList<String>();
        mAdapter = new ArrayAdapter(MonitorActivity.this,android.R.layout.simple_list_item_1, list);
        listView = (ListView) findViewById(R.id.test_list);
        listView.setAdapter(mAdapter);
        Button srart = (Button)findViewById(R.id.start);
        srart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timer.schedule(task, 0, 2000); // 1s后执行task,经过1s再次执行
            }
        });
        MainActivity.bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                String res = new String(data);
                UpdateUI(res);
                //textview.setText(message);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                // Do something when data incoming
            }
        });
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String to_send = "GMA";
                MainActivity.bt.send(to_send.getBytes(),true);
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
    private void UpdateUI(String data) {
        list.clear();
        for(int i = 3; i <= 21; i += 6) {
            String channel = data.substring(i, i + 1);
            String status = "";
            String temp = data.substring(i + 1, i + 2);
            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            if(temp == "F") {
                status = "故障";
            }else if(temp == "N") {
                status = "正常";
            }else if(temp == "A") {
                status = "报警";
            }else if(temp == "S") {
                status = "严重报警";
            }else {

            }
            String val = data.substring(i + 2, i + 6);
            String res = channel + "\t" + val + "\t" + status;
            list.add(res);
            //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
        }
    }
}
