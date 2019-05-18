package com.movieapp.konwo.movieap.data;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.movieapp.konwo.movieap.model.Movie;

import java.util.List;

class MovieRepository {

    private MovieDAO movieDAO;

    private LiveData<List<Movie>> movies;

    MovieRepository(Application application) {
        MovieDatabase movieDatabase = MovieDatabase.getDatabase(application);
        movieDAO = movieDatabase.movieDAO();
        movies = movieDAO.getAll();
    }

    LiveData<List<Movie>> getMovies() {
        return movies;
    }

    void insert(Movie movie) {
        new InsertAsyncTask(movieDAO).execute(movie);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO asyncDao;

        InsertAsyncTask(MovieDAO dao) {
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            asyncDao.insert(movies[0]);
            return null;
        }
    }
}
