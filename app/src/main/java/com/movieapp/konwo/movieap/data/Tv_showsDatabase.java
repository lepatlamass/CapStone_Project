package com.movieapp.konwo.movieap.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.movieapp.konwo.movieap.model.Tv_shows;

@Database(entities = {Tv_shows.class}, version = 1)
public abstract class Tv_showsDatabase extends RoomDatabase {

    public static volatile Tv_showsDatabase INSTANCE;
    public static Tv_showsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Tv_showsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Tv_showsDatabase.class, "tv_showDatabase").build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract Tv_showDAO tvShowDAO();
}
