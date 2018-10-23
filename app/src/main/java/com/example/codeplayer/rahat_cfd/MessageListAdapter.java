package com.example.codeplayer.rahat_cfd;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private BluetoothAdapter myDevice;
    private String deviceName;


    private final LayoutInflater layoutInflater;

    private List<messageStruct> mMessages;

    private class myMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageItemView;


        private  myMessageViewHolder(View itemView) {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message_body);

        }

    }

    private class theirMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageItemView;
        private final TextView nameItemView;

        private  theirMessageViewHolder(View itemView) {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message_body);
            nameItemView = itemView.findViewById(R.id.name);
        }

    }

    private class mapMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageItemView;
        private final TextView timeItemView;

        private  mapMessageViewHolder(View itemView) {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message_body);

            timeItemView = itemView.findViewById(R.id.msgTime);
        }

    }

    MessageListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

        myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==0) {
            View itemView = layoutInflater.inflate(R.layout.my_message, parent, false);
            return new myMessageViewHolder(itemView);
        }
        if(viewType==1){
            View itemView = layoutInflater.inflate(R.layout.their_message,parent,false);
            return  new theirMessageViewHolder(itemView);
        }

            View itemView = layoutInflater.inflate(R.layout.location_message,parent,false);
            return new mapMessageViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

         int type= viewHolder.getItemViewType();
         messageStruct message = mMessages.get(i);


         if(type==0){
             myMessageViewHolder mvh = (myMessageViewHolder)viewHolder;
             mvh.messageItemView.setText(message.getMessage());
         }
         else if(type==1){
             theirMessageViewHolder tmh = (theirMessageViewHolder)viewHolder;
             tmh.messageItemView.setText(message.getMessage());
             tmh.nameItemView.setText(message.getUsername());
         }
         else if(type==2){
             SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             String str = sdf.format(new Date());


             mapMessageViewHolder mmh = (mapMessageViewHolder)viewHolder;
             mmh.messageItemView.setText(message.getMessage());
             mmh.timeItemView.setText(str);
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
