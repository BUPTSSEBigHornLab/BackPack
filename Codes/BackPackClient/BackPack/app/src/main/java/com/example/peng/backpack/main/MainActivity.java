package com.example.peng.backpack.main;

/**
 *Create by:Zhang Yunpeng
 *Date:2017/03/29
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 *describe:主页面，用于向各个页面产生跳转，并创建需要全局使用的蓝牙连接
 */

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.peng.backpack.module.DataBaseModule;
import com.example.peng.backpack.monitor.MonitorActivity;
import com.example.peng.backpack.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    public static BluetoothSPP bt;
    public static DataBaseModule dataBaseModule = new DataBaseModule();
    public static int PackID;
    public static int EventID;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bt = new BluetoothSPP(this);
        dataBaseModule.init(this);

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {

            }
        });
        /**************************************蓝牙配置页面*************************************/
        //监听蓝牙状态
        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {

            public void onDeviceConnected(String name, String address) {
                try {
                    dataBaseModule.addPack(name, address);
                    PackID = dataBaseModule.queryPackID(address);
                }catch (Exception e) {
                    Log.i(TAG, "onDeviceConnected: " + e.toString());
                }
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
        Button btbluetooth = (Button) findViewById(R.id.bt_bluetooth);
        btbluetooth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    showbluetoothDialog();
                    //bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        /**************************************智能监测页面*************************************/
        Button btmonitor = (Button) findViewById(R.id.bt_monitor);
        btmonitor.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonitorActivity.class);
                startActivity(intent);
            }
        });
        /**************************************数据管理页面*************************************/
        Button btdata = (Button) findViewById(R.id.bt_data);

        /**************************************信息上传页面*************************************/
        Button btinfo = (Button) findViewById(R.id.bt_information);

        /**************************************IO测试页面**************************************/
        Button bttest = (Button) findViewById(R.id.bt_test);

        /**************************************设置页面****************************************/
        Button btset = (Button) findViewById(R.id.bt_set);
    }

    public void onStart() {
        super.onStart();
        client.connect();
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
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
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
    //处理已有蓝牙连接，用户强行点击蓝牙配置
    private void showbluetoothDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("已有蓝牙连接");
        normalDialog.setMessage("确定要断开当前连接?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bt.disconnect();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
        // 显示
        normalDialog.show();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //处理用户强行退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitDialog();
        }
        return false;
    }
    private void exitDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("确定要退出当前程序?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
        // 显示
        normalDialog.show();
    }
}
