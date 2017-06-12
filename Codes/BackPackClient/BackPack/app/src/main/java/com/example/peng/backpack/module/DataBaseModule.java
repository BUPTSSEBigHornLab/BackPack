package com.example.peng.backpack.module;

/**
 *Create by:Zhang Yunpeng
 *Date:2017/06/12
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 *describe:数据库操作模块
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseModule {

    private static final String TAG = "DataBaseModule";
    private DataBaseHelper dbHelper;
    private Context mContext;

    private DataBaseModule(Context context) {
        mContext = context;
    }
    /** 初始化创建数据库和表格 */
    public void init() {
        dbHelper = new DataBaseHelper(mContext, "BACKPACK.db", null, 1);
        Log.i(TAG, "init: " + "数据库已创建");
        dbHelper.getWritableDatabase();
        Log.i(TAG, "init: " + "判断性创建表格");
    }

    /** 在PACK表中添加数据 */
    public void addPack(String PackName, String MACAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PackName", PackName);
        values.put("MACAddress", MACAddress);
        db.insert("PACK", null, values);
    }

    /** 在PACK表中删除数据 */
    public void deletePack(String PackName, String MACAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    /** 在PACK表中更新数据 */
    public void updatePack(String PackName, String MACAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    /** 在PACK表中查询数据 */
    public void queryPack(String PackName, String MACAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    /** 在EVENT表中添加数据 */
    public void addEvent(String PackName, String MACAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PackName", PackName);
        values.put("MACAddress", MACAddress);
        db.insert("PACK", null, values);
    }
}
