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
import android.widget.ImageButton;
import android.widget.ListView;

public class chatFragment extends Fragment {

    private EditText messageToSend;
    protected MessageAdapter messageAdapter;
    final private String SERVICE_ID = "RAHAT_CFD";
    private ListView messagesView;
    private ImageButton sendButton;
    MainActivity act;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageToSend = view.findViewById(R.id.messageToSend);
        messageAdapter = new MessageAdapter(getActivity());
        messagesView =  view.findViewById(R.id.messages_view);
        sendButton =  (ImageButton) view.findViewById(R.id.sendButton);
        messagesView.setAdapter(messageAdapter);
////
        sendButton.setOnClickListener(sendMessageListener);
        act = ((MainActivity)getActivity());



    }

    private View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = messageToSend.getText().toString();

            act.sendData(message,messageAdapter,null,false);

            //Logic to send message

        }
    };


}

