package com.car;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.common.Constant;
import com.db.model.Command;
import com.device.BlueToothCar;
import com.task.TaskManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CommunicationService extends Service {
    public static String TAG = CommunicationService.class.getSimpleName();
    public static final  String OPERATION = "OPERATION";
    public static final  String ADDRESS = "ADDRESS";
    private static BlueToothCar mCar = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		/* TODO Auto-generated method stub */
        Log.d(TAG, "onStartCommand");
        if (intent != null) {
            switch (intent.getIntExtra("OPERATION", -1)) {
                case 0:
                    mCar.stop();
                    Log.d(TAG, "stop bluetooth connection");
                    break;
                case 1:
                    Log.d(TAG, "start bluetooth connection");
                    String address = intent.getStringExtra(ADDRESS);
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                    mCar.connect(device, false);
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        EventBus.getDefault().register(CommunicationService.this);
        mCar = new BlueToothCar(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCar.stop();
        TaskManager.stop();
        Log.d(TAG, "onDestroy");
    }

    public static void sendCommand(Command command){
        Log.d(TAG, "sendCommand");
        EventBus.getDefault().post(command);
    }

    public static void sendBlueState(BlueState state){
        Log.d(TAG, "sendBlueState");
        EventBus.getDefault().post(state);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommand(Command event) {
        Constant.Direction direction = Constant.getDirection(event.getDirect());
        Log.d(TAG, "onCommand:" + direction.getName());
        mCar.write(direction.getName().getBytes());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBlueState(BlueState state) {
        Log.d(TAG, "onBlueState");
        int index = state.getState();
        String content = null;
        switch(index){
            case BlueToothCar.STATE_NONE:
                content = "STATE_NONE";
                break;
            case BlueToothCar.STATE_CONNECTING:
                content = "STATE_CONNECTING";
                break;
            case BlueToothCar.STATE_CONNECTED:
                content = "STATE_CONNECTED";
                break;
            case BlueToothCar.STATE_FAILED:
                content = "STATE_FAILED";
                break;
            case BlueToothCar.STATE_LOST:
                content = "STATE_LOST";
                break;
        }
        if (content != null) {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }
}
