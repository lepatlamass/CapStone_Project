package com.movieapp.konwo.movieap.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.movieapp.konwo.movieap.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> movies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        movies = repository.getMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movies;
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }
}
