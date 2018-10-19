package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {messageStruct.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

}
