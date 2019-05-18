package com.movieapp.konwo.movieap.data;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.movieapp.konwo.movieap.model.Movie;
import com.movieapp.konwo.movieap.model.Tv_shows;

import java.util.List;

public class Tv_Repository {

    private Tv_showDAO tvShowDAO;

    private LiveData<List<Tv_shows>> tvShows;

    Tv_Repository (Application application) {
        Tv_showsDatabase tv_showsDatabase = Tv_showsDatabase.getDatabase(application);
        tvShowDAO = tv_showsDatabase.tvShowDAO();
        tvShows = tvShowDAO.getAll();
    }

    LiveData<List<Tv_shows>> getTvShows(){return tvShows;}

    void insert(Tv_shows tv_shows){
        new InsertAsyncTask(tvShowDAO).execute(tv_shows);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertAsyncTask extends AsyncTask<Tv_shows, Void, Void> {

        private Tv_showDAO asyncDao;

        InsertAsyncTask(Tv_showDAO dao) {
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(Tv_shows... tv_shows) {
            asyncDao.insert(tv_shows[0]);
            return null;
        }
    }
}
