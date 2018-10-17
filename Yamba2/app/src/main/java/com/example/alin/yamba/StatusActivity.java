package com.example.alin.yamba;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final String TAG = "StatusActivity";
    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount;
    private int defaultTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus =(EditText) findViewById(R.id.editStatus);
        buttonTweet=(Button) findViewById(R.id.buttonTweet);
        textCount =(TextView) findViewById(R.id.textCount) ;

        buttonTweet.setOnClickListener(this);

        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                if(count < 10){
                    textCount.setTextColor(Color.RED);
                }else{
                    textCount.setTextColor(defaultTextColor);
                }

            }
        });
    }
    @Override
    public void onClick(View view){
        String status = editStatus.getText().toString();
        Log.d(TAG,"onClick with status: " + status);

        new PostTask().execute(status);
    }

    private final class PostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            YambaClient yambaCloud = new YambaClient("student", "12345678");
            try{
                yambaCloud.postStatus( params[0] );
                return "Successfull post";
            } catch (YambaClientException e){
                e.printStackTrace();
                return  "Failed post";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
