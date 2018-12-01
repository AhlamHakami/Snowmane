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

public class NewNextActivity extends AppCompatActivity {
    public static String TAG = NewNextActivity.class.getSimpleName();

    private Long id;
    private String name = "";
    private String time;
    private int direction;

    @BindView(R.id.et_direction)
    Spinner etDirection;
    @BindView(R.id.et_time)
    EditText etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnext);
        ButterKnife.bind(NewNextActivity.this);

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        id = intent.getLongExtra("INDEX", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ib_next, R.id.ib_start,R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_next:
                if (name == null || name.isEmpty() || id == 0)
                    return;
                time = etTime.getText().toString();
                direction = (int)(etDirection.getSelectedItemId());
                Command command = DBManager.getCommand(new Long(id));
                id++;
                command.setChild(new Long(id));
                DBManager.updateCommand(command);
                DBManager.insertCommand(new Command(new Long(id), direction, "", time, new Long(0)));
                etDirection.setSelection(0);
                etTime.setText("");
                Log.d(TAG, "next step");
                break;
            case R.id.ib_start:
                Log.d(TAG, "start");
                List<Command> list = null;
                // read data from db
                if(name.length() > 0) {
                    list = DBManager.getCommands(name);
                }
                if(list != null && list.size() > 0){
                    // send command to bluetooth

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