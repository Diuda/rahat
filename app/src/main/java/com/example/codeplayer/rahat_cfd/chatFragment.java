package com.example.codeplayer.rahat_cfd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class chatFragment extends Fragment {

    private EditText messageToSend;
    private MessageAdapter messageAdapter;
    final private String SERVICE_ID = "RAHAT_CFD";
    private ListView messagesView;
    private Button sendButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageToSend = view.findViewById(R.id.messageToSend);
        messageAdapter = new MessageAdapter(view.getContext());
        messagesView =  view.findViewById(R.id.messages_view);
        sendButton = view.findViewById(R.id.sendButton);
        messagesView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(sendMessageListener);


    }

    private View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = messageToSend.getText().toString();
            //Logic to send message

        }
    };


}

