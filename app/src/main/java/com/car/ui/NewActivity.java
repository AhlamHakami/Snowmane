package com.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.common.Constant;
import com.db.DBManager;
import com.db.model.Command;
import com.task.TaskManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewActivity extends AppCompatActivity {
    public static String TAG = NewActivity.class.getSimpleName();

    private String name;
    private String time;
    private long id;
    private int direction;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_direction)
    Spinner etDirection;
    @BindView(R.id.et_time)
    EditText etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ButterKnife.bind(NewActivity.this);
        Log.d(TAG, "onCreate");
    }

    @OnClick({R.id.ib_next, R.id.ib_start, R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_next:
                Log.d(TAG, "next step");
                id = DBManager.getCommandLastIndex();
                name = etName.getText().toString();
                time = etTime.getText().toString();
                direction = (int)(etDirection.getSelectedItemId());
                DBManager.insertCommand(new Command(new Long(id), direction, name, time, new Long(0)));
                Intent intent = new Intent(NewActivity.this,NewNextActivity.class);
                intent.putExtra("NAME",name);
                intent.putExtra("INDEX",id);
                startActivity(intent);
                finish();
                break;
            case R.id.ib_start:
                Log.d(TAG, "start");
                List<Command> list = null;
                // read data from db
                if(etName.getText().length() > 0) {
                    list = DBManager.getCommands(etName.getText().toString());
                }
                if(list != null && list.size() > 0){
                    // send command
                    list.add(new Command(new Long(0), Constant.Direction.DIRECTION_STOP.getIndex(), "", "1", new Long(0)));
                    TaskManager.executeCommands(list);
                }
                finish();
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}