package com.device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import com.car.BlueState;
import com.car.CommunicationService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueToothCar {
    public static final String TAG = BlueToothCar.class.getSimpleName();
    public static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0; // we're doing nothing
    // connections
    public static final int STATE_CONNECTING = 2; // now in;itiating an outgoing
    // connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote
    // device
    public static final int STATE_FAILED = 5; // connect failed

    public static final int STATE_LOST = 6; // connect lost

    private final BluetoothAdapter mAdapter;
    private final CommunicationService mService;
    private ConnectThread mConnectThread = null;
    private ConnectedThread mConnectedThread = null;
    private int mState;

    public BlueToothCar(CommunicationService service) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mService = service;
    }

    public synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // notify the bluetooth state.
        CommunicationService.sendBlueState(new BlueState(state));
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     */
    public synchronized void connected(BluetoothSocket socket, final String socketType) {
        Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    public void write(byte[] out) {
        Log.d(TAG, "write");

        if (mAdapter == null || !mAdapter.isEnabled()) {
            Toast.makeText(mService, "Bluetooth off", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mConnectedThread == null) {
            Toast.makeText(mService, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        synchronized (this) {
            if (mState == STATE_CONNECTED) {
                mConnectedThread.write(out);
            }
        }
    }

    public class ConnectThread extends Thread {
        public final String TAG = ConnectThread.class.getSimpleName();
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final String mSocketType;

        @SuppressLint("NewApi")
        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(BlueToothCar.MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(BlueToothCar.MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                tmp = null;
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "ConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Make a connection to the BluetoothSocket
            try {
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                setState(BlueToothCar.STATE_FAILED);
                e.printStackTrace();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                synchronized (BlueToothCar.this) {
                    mConnectThread = null;
                }
                return;
            }

            synchronized (BlueToothCar.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ConnectedThread extends Thread {
        public final String TAG = ConnectedThread.class.getSimpleName();
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private boolean isRunning = true;
        private String mSocketType;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "ConnectedThread: " + socketType);
            mSocketType = socketType;
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "ConnectedThread SocketType:" + mSocketType);
            setName("ConnectedThread" + mSocketType);
            byte data;
            while (isRunning) {
                try {
                    data = (byte) mmInStream.read();
                    Log.d(TAG, byte2Hex(data));
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            setState(STATE_LOST);
            synchronized (BlueToothCar.this) {
                mConnectedThread = null;
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                isRunning = false;
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String byte2Hex(byte b) {
        StringBuffer hs = new StringBuffer(1);
        String stmp = "";
        int len = 1;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }
}
