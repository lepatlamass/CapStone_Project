package com.movieapp.konwo.movieap.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.movieapp.konwo.movieap.model.Tv_shows;

import java.util.List;

public class Tv_showViewModel extends AndroidViewModel {

    private Tv_Repository repository;
    private LiveData<List<Tv_shows>> tvShows;

    public Tv_showViewModel(@NonNull Application application) {
        super(application);
        repository = new Tv_Repository(application);
        tvShows = repository.getTvShows();
    }

    public LiveData<List<Tv_shows>> getAllTv_shows() {
        return tvShows;
    }

    public void insert(Tv_shows tvShows) {
        repository.insert(tvShows);
    }
}
