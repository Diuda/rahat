package com.example.codeplayer.rahat_cfd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {


    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageItemView;

        private  MessageViewHolder(View itemView) {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message_body);
        }

    }

    private final LayoutInflater layoutInflater;

    private List<messageStruct> mMessages;

    MessageListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.my_message, parent, false);
        return new MessageViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (mMessages != null) {
            messageStruct current = mMessages.get(position);
            holder.messageItemView.setText(current.getMessage());
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






}
