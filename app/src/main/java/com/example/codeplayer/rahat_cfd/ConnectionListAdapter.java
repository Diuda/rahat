package com.example.codeplayer.rahat_cfd;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConnectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String selectedConnection=null;
    Context context;
    int position = -1;
    private LayoutInflater layoutInflater;
    ArrayList <String> ConnectionsNameList = new ArrayList<>();
    Set<String> connectedList = new HashSet<>();

    public ConnectionListAdapter(Context context) {

            layoutInflater = LayoutInflater.from(context);
            this.context = context;

    }

    private class connectionItemViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView connectionName;

        private  connectionItemViewHolder(View view){
            super((view));
            cardView = view.findViewById(R.id.connectionCard);
            connectionName = view.findViewById(R.id.connection_name);
        }
    }

    @NonNull
    @Override


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View connectionItem = layoutInflater.inflate(R.layout.connection_item,viewGroup,false);
        return new connectionItemViewHolder(connectionItem);
    }

    public  String getCurrentConnection(){
        return selectedConnection;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        String name = ConnectionsNameList.get(i);
        connectionItemViewHolder itemViewHolder = (connectionItemViewHolder) viewHolder;

        if(connectedList.contains(name)){

            itemViewHolder.connectionName.setText("Connected to: "+name);

        }
        else
        itemViewHolder.connectionName.setText(name);

        ((connectionItemViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = i;
                notifyDataSetChanged();
            }
        });

        if(i==position){
            selectedConnection = itemViewHolder.connectionName.getText().toString();
            ((connectionItemViewHolder) viewHolder).cardView.setBackgroundColor(R.color.colorAccent);
        }




    }
    public void updateList(ArrayList<String> newList){
        this.ConnectionsNameList = newList;
        this.notifyDataSetChanged();
    }

    public void updateResultList(Set<String> newList){

        this.connectedList = newList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

            if (ConnectionsNameList != null)
                return ConnectionsNameList.size();
            else return 0;
        }

}
