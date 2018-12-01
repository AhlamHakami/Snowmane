package com.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntranceActivity extends AppCompatActivity {
    public static String TAG = EntranceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ButterKnife.bind(EntranceActivity.this);
        Log.d(TAG, "onCreate");
    }
//user interface main menu
    @OnClick({R.id.ib_model, R.id.ib_new,R.id.ib_load,R.id.ib_delete,R.id.ib_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_model:
                Log.d(TAG, "control model");
                startActivity(new Intent(EntranceActivity.this, ControlModelActivity.class));
                break;
            case R.id.ib_new:
                Log.d(TAG, "new");
                startActivity(new Intent(EntranceActivity.this, NewActivity.class));
                break;
            case R.id.ib_load:
                Log.d(TAG, "load");
                startActivity(new Intent(EntranceActivity.this, LoadActivity.class));
                break;
            case R.id.ib_delete:
                Log.d(TAG, "load");
                startActivity(new Intent(EntranceActivity.this, DeleteActivity.class));
                break;
            case R.id.ib_exit:
                Log.d(TAG, "exit");
                finish();
                break;
        }
    }
}
