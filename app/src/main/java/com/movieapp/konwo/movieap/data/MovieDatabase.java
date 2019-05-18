package com.movieapp.konwo.movieap.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.movieapp.konwo.movieap.model.Movie;

/**
 * Movie database class used to store favorite movie data
 */
@Database(entities =  {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    public static volatile MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class,"movie_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MovieDAO movieDAO();

}
