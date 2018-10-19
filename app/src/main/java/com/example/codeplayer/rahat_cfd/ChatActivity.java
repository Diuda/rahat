package com.example.codeplayer.rahat_cfd;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends AppCompatActivity {
    private EditText messageToSend;
    private MessageAdapter messageAdapter;
    final private String SERVICE_ID = "RAHAT_CFD";
    private ListView messagesView;

    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    String deviceName = myDevice.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageToSend = findViewById(R.id.messageToSend);
        messageAdapter = new MessageAdapter(this);
        messagesView =  findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
    }

    public void sendMessage(View view) {
        String message = messageToSend.getText().toString();


        //  --------Implement message sending here--------

//        if (message.length() > 0) {
//            scaledrone.publish(roomName, message);
//            editText.getText().clear();
//        }

        //---------------------------
    }
}
class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
