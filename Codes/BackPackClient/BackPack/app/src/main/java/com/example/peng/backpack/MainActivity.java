/*
 *Create by:ZhangYunpeng
 *Date:2017/3/29
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 */
package com.example.peng.backpack;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.main);
    }
}
