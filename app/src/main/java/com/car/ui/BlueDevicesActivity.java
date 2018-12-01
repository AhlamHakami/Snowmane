package com.car.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.car.CommunicationService;
import com.task.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class BlueDevicesActivity extends AppCompatActivity {
    public static String TAG = BlueDevicesActivity.class.getSimpleName();

    @BindView(R.id.lv_devices)
    ListView lvDevices;

    private BluetoothAdapter mBluetoothAdapter = null;
    List<String> mDevices = new ArrayList<String>();

    private void refreshDevices() {
        // Search paired devices
        mBluetoothAdapter.cancelDiscovery();
        mDevices.clear();
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            mDevices.add(device.getName() + "\n" + device.getAddress());
        }
        lvDevices.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDevices));
    }

    @OnItemClick(R.id.lv_devices)
    void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = mDevices.get(position);
        String deviceAddress = str.substring(str.indexOf("\n") + 1);
        Log.d(TAG, "device Address:" + deviceAddress);
        Intent service = new Intent(BlueDevicesActivity.this, CommunicationService.class);
        service.putExtra(CommunicationService.OPERATION, 1);
        service.putExtra(CommunicationService.ADDRESS, deviceAddress);
        startService(service);
        startActivity(new Intent(BlueDevicesActivity.this, EntranceActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluedevices);
        ButterKnife.bind(BlueDevicesActivity.this);
        Log.d(TAG, "onCreate");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        refreshDevices();
    }

    @OnClick({R.id.ib_refresh, R.id.ib_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_refresh:
                Log.d(TAG, "refresh");
                refreshDevices();
                break;
            case R.id.ib_exit:
                Intent service = new Intent(BlueDevicesActivity.this, CommunicationService.class);
                service.putExtra(CommunicationService.OPERATION, 0);
                service.putExtra(CommunicationService.ADDRESS, "");
                stopService(service);
                TaskManager.stop();
                finish();
                break;
        }
    }
}
