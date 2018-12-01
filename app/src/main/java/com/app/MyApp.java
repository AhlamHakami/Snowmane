package com.app;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.car.CommunicationService;
import com.db.dao.DaoMaster;
import com.db.dao.DaoSession;

//
public class MyApp extends Application {
    private static DaoMaster.DevOpenHelper mHelper;
    private static SQLiteDatabase mDB;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    public static MyApp instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
        Intent i = new Intent(MyApp.this, CommunicationService.class);
        startService(i);
    }

    public static MyApp getInstances(){
        return instances;
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        Intent i = new Intent(MyApp.this, CommunicationService.class);
        stopService(i);
    }

    private void setDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "commands-db", null);
        mDB = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDB);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return mDB;
    }
}