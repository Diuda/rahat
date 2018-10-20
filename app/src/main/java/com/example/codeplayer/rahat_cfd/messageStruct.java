package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class messageStruct {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "messageid")
    private String messageid;

    @ColumnInfo(name = "user")
    private String username;

    @ColumnInfo(name = "message")
    private String message;


    public messageStruct( String username, String message){
        this.messageid = UUID.randomUUID().toString();
        this.username = username;
        this.message = message;
    }


    public void setMessageid(String messageid){
        this.messageid = messageid;
    }



    public String getMessageid(){
        return messageid;
    }

    public String getUsername(){
        return username;
    }

    public String getMessage(){
        return message;
    }





}



