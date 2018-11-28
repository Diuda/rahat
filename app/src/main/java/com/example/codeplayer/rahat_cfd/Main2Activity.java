package com.example.codeplayer.rahat_cfd;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Main2Activity extends AppCompatActivity {



    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final MessageListAdapter adapter = new MessageListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        messageViewModel.getmAllMessage().observe(this, new Observer<List<messageStruct>>() {
            @Override
            public void onChanged(@Nullable List<messageStruct> messageStructs) {
                adapter.setWords(messageStructs);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, NewMessageActivity.class);
                startActivityForResult(intent, 2);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("messagekivaluesupar", data.getStringExtra("MESSAGE"));

        if(requestCode == 2 && resultCode == 1) {
            Log.i("messagekivalues", data.getStringExtra("MESSAGE"));
//            messageStruct messageStruct = new messageStruct( data.getStringExtra("USERNAME"), data.getStringExtra("MESSAGE"),1);
//            messageViewModel.insert(messageStruct);

        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}
