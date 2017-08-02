package com.vishal.intentsimpletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG;
    private Button sendMessageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TAG  = getResources().getString(R.string.app_log_tag);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        //Data was sent using Bundle - intent.putExtras(bundle);
        if(bundle != null) {
            try {
                Log.v(TAG, "message : " + bundle.getString("message"));
                Log.v(TAG, "string-bundle : " + bundle.getString("string"));
                Log.v(TAG, "int-bundle : " +  String.valueOf(bundle.getInt("int")));
                Log.v(TAG, "boolean-bundle : " +  String.valueOf(bundle.getBoolean("boolean")));
                Log.v(TAG, "request-code : " +  String.valueOf(bundle.getInt("request-code")));
            }
            catch (Exception e) {
                Log.e(TAG, "Caught Exception when trying to read data from the bundle", e);
                e.printStackTrace();
            }
        }

        //Data was sent using putExtra method
        try {
            Log.v(TAG, "message : " + intent.getStringExtra("message"));
            Log.v(TAG, "string-extra : " + intent.getStringExtra("string"));
            Log.v(TAG, "int-extra : " + String.valueOf(intent.getIntExtra("int", -1)));
            Log.v(TAG, "boolean-extra : " + String.valueOf(intent.getBooleanExtra("boolean", false)));
            Log.v(TAG, "request-code : " + String.valueOf(intent.getIntExtra("request-code", -1)));
        }
        catch (Exception e) {
            Log.e(TAG, "Caught Exception when trying to read data from the bundle", e);
            e.printStackTrace();
        }

        sendMessageBtn = (Button) findViewById(R.id.sendMessageBack);
        sendMessageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sendMessageBack:
                Intent i1 = getIntent();
                i1.putExtra("MESSAGE-FROM-SECOND-ACTIVITY", ((EditText)findViewById(R.id.editText)).getText().toString().trim());
                setResult(RESULT_OK, i1);
                finish();
                break;
        }
    }
}
