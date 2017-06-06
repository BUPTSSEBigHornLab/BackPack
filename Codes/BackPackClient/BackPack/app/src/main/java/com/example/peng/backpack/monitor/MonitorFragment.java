package com.example.peng.backpack.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.peng.backpack.R;
import com.example.peng.backpack.main.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class MonitorFragment extends Fragment {

    private static final String TAG = "MonitorFragment";

    /**
     * 用于存储各个通道数据的列表，MonitorItem中包含了三列数据信息 ，通道号、测量值、状态
     */
    private List<MonitorItem> list;
    private MonitorAdapter mAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        list = new ArrayList<MonitorItem>();
        mAdapter = new MonitorAdapter(this.getActivity(), list);
        final Activity activity = this.getActivity();
        listView = (ListView) view.findViewById(R.id.datalist);

        listView.setAdapter(mAdapter);

        Button srart = (Button) view.findViewById(R.id.start);
        srart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timer.schedule(task, 0, 2000); // 1s后执行task,经过1s再次执行
            }
        });

        MainActivity.bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                String res = new String(data);
                UpdateUI(res);
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String to_send = "GMA";
                MainActivity.bt.send(to_send.getBytes(), true);
            }
            super.handleMessage(msg);
        }

        ;
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

        Log.i(TAG, "UpdateUI: " + data);
        list.clear();

        for (int i = 3; i <= 21; i += 6) {
            String channel = data.substring(i, i + 1);
            String status = data.substring(i + 1, i + 2);
            String val = data.substring(i + 2, i + 6);

            MonitorItem item = new MonitorItem();
            item.setChannel(channel);
            item.setMesure_val(val);
            item.setStatus(status);
            list.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

}
