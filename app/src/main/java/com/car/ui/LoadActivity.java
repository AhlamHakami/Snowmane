package com.car.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.common.Constant;
import com.db.DBManager;
import com.db.model.Command;
import com.task.TaskManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadActivity extends AppCompatActivity {
    public static String TAG = LoadActivity.class.getSimpleName();

    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(LoadActivity.this);
        Log.d(TAG, "onCreate");
    }

    @OnClick({R.id.ib_load, R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_load:
                Log.d(TAG, "load");
                List<Command> list = null;
                // read data from db
                if(etName.getText().toString().length() > 0) {
                    list = DBManager.getCommands(etName.getText().toString());
                }
                if(list != null && list.size() > 0){
                    // send command to blue tootch
                    list.add(new Command(new Long(0), Constant.Direction.DIRECTION_STOP.getIndex(), "", "1", new Long(0)));
                    TaskManager.executeCommands(list);
                    etName.setText("");
                }
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}