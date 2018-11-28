package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {messageStruct.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao MessageDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "m_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(mRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback mRoomDatabaseCallback =
            new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();

        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
        private final MessageDao messageDao;

        PopulateDbAsync(AppDatabase db){
            messageDao = db.MessageDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            messageStruct messageStruct = new messageStruct(1,"diuda", "Scene kya hai?");
//            messageDao.insertAll(messageStruct);
//            messageStruct = new messageStruct(2,"paddy", "Mera sab sahi hai tera scene kya hia?");
//            messageDao.insertAll(messageStruct);
            return null;
        }
    }



}
