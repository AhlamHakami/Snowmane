package com.car.ui;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.car.CommunicationService;
import com.common.Constant;
import com.db.model.Command;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ControlModelActivity extends AppCompatActivity {
    public static String TAG = ControlModelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlmodel);
        ButterKnife.bind(ControlModelActivity.this);
        Log.d(TAG, "onCreate");
    }

    @OnTouch({R.id.forward, R.id.left, R.id.stop, R.id.right, R.id.backward, R.id.ib_back})
    //Creating and sending commands
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.forward:
                    CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_FORWARD.getIndex(), "Forward", "1", new Long(0)));
                    Log.d(TAG, "forward");
                    break;
                case R.id.backward:
                    CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_BACKWORD.getIndex(), "Backward", "1", new Long(0)));
                    Log.d(TAG, "backward");
                    break;
                case R.id.left:
                    CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_LEFT.getIndex(), "Left", "1", new Long(0)));
                    Log.d(TAG, "left");
                    break;
                case R.id.right:
                    CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_RIGHT.getIndex(), "Right", "1", new Long(0)));
                    Log.d(TAG, "right");
                    break;
                case R.id.stop:
                    CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_STOP.getIndex(), "Stop", "1", new Long(0)));
                    Log.d(TAG, "stop");
                    break;
                case R.id.ib_back:
                    Log.d(TAG, "back");
                    finish();
                    return true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP && view.getId() != R.id.ib_back) {
            CommunicationService.sendCommand(new Command(new Long(0), Constant.Direction.DIRECTION_STOP.getIndex(), "Stop", "1", new Long(0)));
            Log.d(TAG, "stop");
        }
        return true;
    }
}
