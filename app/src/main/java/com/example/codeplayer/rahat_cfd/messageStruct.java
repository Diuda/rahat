package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class messageStruct {

    @PrimaryKey
    private int userid;

    @ColumnInfo(name = "user")
    private String username;

    @ColumnInfo(name = "message")
    private String message;


    public void setUserID(int id){
        this.userid = id;
    }


    public void setUserName(String username){
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



