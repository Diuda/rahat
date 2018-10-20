package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class messageStruct {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userid")
    private int userid;

    @ColumnInfo(name = "user")
    private String username;

    @ColumnInfo(name = "message")
    private String message;


    public messageStruct(@NonNull int userid, String username, String message){
        this.userid = userid;
        this.username = username;
        this.message = message;
    }


    public void setUserid(int id){
        this.userid = id;
    }


    public void setUsername(String username){
        this.username = username;
    }


    public void setMessage(String message){
        this.message = message;
    }

    public int getUserid(){
        return userid;
    }

    public String getUsername(){
        return username;
    }

    public String getMessage(){
        return message;
    }





}



