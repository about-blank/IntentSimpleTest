package com.vishal.intentsimpletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox checkBox;
    private EditText editText;
    private String TAG;

    private Button buttonPutExtra, buttonPutExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TAG  = getResources().getString(R.string.app_log_tag);
        checkBox = (CheckBox) findViewById(R.id.checkBox2);
        buttonPutExtra = (Button) findViewById(R.id.sendMessageUsingPutExtra);
        buttonPutExtras = (Button) findViewById(R.id.sendMessageUsingPutExtras);

        buttonPutExtra.setOnClickListener(this);
        buttonPutExtras.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.editText0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            if(requestCode == 10001) {
                Log.v(TAG, "Received Result for request sent using putExtra");
                Log.v(TAG, "Response MSG : " + data.getStringExtra("MESSAGE-FROM-SECOND-ACTIVITY"));
            } else if(requestCode == 10002){
                Log.v(TAG, "Received Result for request sent using putExtras (Bundle)");
                Log.v(TAG, "Response MSG : " + data.getStringExtra("MESSAGE-FROM-SECOND-ACTIVITY"));
            } else {
                Log.e(TAG, "Received result for an unknown request-code of " + requestCode);
            }
        }
    }

    @Override
    public void onClick(View view) {

        boolean getResult = checkBox.isChecked();
        String message = editText.getText().toString().trim();

        int requestCode = -1;
        switch (view.getId()) {

            case R.id.sendMessageUsingPutExtra:
                requestCode = 10001;
                Intent i1 = new Intent(this, SecondActivity.class);
                i1.putExtra("message", message);
                i1.putExtra("string","string-extra-value");
                i1.putExtra("int",10);
                i1.putExtra("boolean", true);
                if(getResult) {
                    i1.putExtra("request-code", requestCode);
                    startActivityForResult(i1, requestCode);
                }
                else {
                    startActivity(i1);
                }
                break;
            case R.id.sendMessageUsingPutExtras:
                requestCode = 10002;
                Intent i2 = new Intent(this, SecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                bundle.putString("string","string-bundle-value");
                bundle.putInt("int", 20);
                bundle.putBoolean("boolean", true);
                if(getResult) {
                    bundle.putInt("request-code", requestCode);
                }
                i2.putExtras(bundle);
                if(getResult) {
                    startActivityForResult(i2, requestCode);
                }
                else {
                    startActivity(i2);
                }
                break;
        }
    }
}
