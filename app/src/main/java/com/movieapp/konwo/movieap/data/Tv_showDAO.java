package com.movieapp.konwo.movieap.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.movieapp.konwo.movieap.model.Tv_shows;

import java.util.List;

@Dao
public interface Tv_showDAO {
    // get all movies
    @Query("SELECT * FROM Tv_shows")
    LiveData<List<Tv_shows>> getAll();

    // insert movie into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tv_shows tv_shows);

    @Query("SELECT * FROM Tv_shows Where id = :id")
    Tv_shows getTvShowById(int id);

    @Delete
    void delete(Tv_shows tv_shows);
}
