package com.car.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.db.DBManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteActivity extends AppCompatActivity {
    public static String TAG = DeleteActivity.class.getSimpleName();

    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        ButterKnife.bind(DeleteActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ib_delete, R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_delete:
                Log.d(TAG, "delete");
                if(etName.getText().toString().length() > 0) {
                    DBManager.deleteCommands(etName.getText().toString());
                    etName.setText("");
                }
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}