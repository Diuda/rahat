package com.example.codeplayer.rahat_cfd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewMessageActivity extends AppCompatActivity {


    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";


    private EditText username;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_new_message);

        username = findViewById(R.id.username);
        message = findViewById(R.id.message);

        final Button btn = findViewById(R.id.button_save);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(username.getText())||TextUtils.isEmpty(message.getText())){
                    setResult(RESULT_CANCELED, replyIntent);

                    Log.i("messagegyahinhi", "hello");

                }
                else{


                    String USERNAME = username.getText().toString();
                    String MESSAGE = message.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME", USERNAME);
                    bundle.putString("MESSAGE", MESSAGE);
                    replyIntent.putExtras(bundle);
                    setResult(1, replyIntent);

                    Log.i("messagekivalueinitent", "hello");
                }
                finish();
            }
        });
    }
}
