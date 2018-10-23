package com.example.codeplayer.rahat_cfd;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    BluetoothAdapter myDevice;
    String deviceName;
    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageItemView;
        private final TextView nameItemView;
        private final TextView timeItemView;

        private  MessageViewHolder(View itemView) {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message_body);
            nameItemView = itemView.findViewById(R.id.name);
            timeItemView = itemView.findViewById(R.id.msgTime);
        }

    }

    private final LayoutInflater layoutInflater;

    private List<messageStruct> mMessages;

    MessageListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

        myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==0) {
            View itemView = layoutInflater.inflate(R.layout.my_message, parent, false);
            return new MessageViewHolder(itemView);
        }
        if(viewType==1){
            View itemView = layoutInflater.inflate(R.layout.their_message,parent,false);
            return  new MessageViewHolder(itemView);
        }

            View itemView = layoutInflater.inflate(R.layout.location_message,parent,false);
            return new MessageViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (mMessages != null) {
            messageStruct current = mMessages.get(position);
            holder.messageItemView.setText(current.getMessage());
            if(holder.nameItemView!=null)
            holder.nameItemView.setText(current.getUsername());
            if(holder.timeItemView!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String str = sdf.format(new Date());
                holder.timeItemView.setText(str);
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.messageItemView.setText("No Word");
        }
    }

    void setWords(List<messageStruct> words){
        mMessages = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mMessages != null)
            return mMessages.size();
        else return 0;
    }


    @Override
    public int getItemViewType(int position) {
        messageStruct message = mMessages.get(position);
        if(message.getMessageType()==2){
            return 2;
        }

        if(message.getUsername().equals(deviceName)){

            return 0;
        }
        else{
            return 1;
        }

    }
}
