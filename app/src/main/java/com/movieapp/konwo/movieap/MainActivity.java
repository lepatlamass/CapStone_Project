package com.movieapp.konwo.movieap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.movieapp.konwo.movieap.adapter.MoviesAdapter;
import com.movieapp.konwo.movieap.adapter.Tv_showsAdapter;
import com.movieapp.konwo.movieap.api.Client;
import com.movieapp.konwo.movieap.api.Service;
import com.movieapp.konwo.movieap.data.MovieDatabase;
import com.movieapp.konwo.movieap.data.MovieViewModel;
import com.movieapp.konwo.movieap.data.Tv_showViewModel;
import com.movieapp.konwo.movieap.data.Tv_showsDatabase;
import com.movieapp.konwo.movieap.model.Movie;
import com.movieapp.konwo.movieap.model.MoviesResponse;
import com.movieapp.konwo.movieap.model.Tv_shows;
import com.movieapp.konwo.movieap.model.Tv_showsResponses;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MoviesAdapter.class.getName();

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    private static final String LIST_STATE = "list_state";

    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private Tv_showsAdapter tv_showsAdapter;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private List<Tv_shows> tvShows;
    private SwipeRefreshLayout swipeContainer;
    private AppCompatActivity activity = MainActivity.this;

    private Parcelable savedRecyclerLayoutState;
    private ArrayList<Movie> moviesInstance = new ArrayList<>();
    private ArrayList<Tv_shows> tvShowsInstance = new ArrayList<>();

    private MovieDatabase movieDatabase;
    private Tv_showsDatabase tv_showsDatabase;
    private MovieExec executor;
    private TvShowExec tvShowExec;
    private MovieViewModel viewModel;
    private Tv_showViewModel tv_showViewModel;

    //firebase analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    //analytics
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //add admobs to application
        MobileAds.initialize(this, "ca-app-pub-8969054910088588~2014101977");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState != null) {
            moviesInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            showData();
            showDataTvShow();
        } else {
            loadMovies(null);
        }

        // get viewModel from ViewModelProviders class
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        tv_showViewModel  = ViewModelProviders.of(this).get(Tv_showViewModel.class);

        movieDatabase = MovieDatabase.getDatabase(this);
        executor = new MovieExec();

        tv_showsDatabase = Tv_showsDatabase.getDatabase(this);
        tvShowExec = new TvShowExec();

    }

    //show movie datas
    private void showData() {
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new MoviesAdapter(this, moviesInstance);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        restoreLayoutManagerPosition();
        adapter.notifyDataSetChanged();
    }

    //show tvshow datas
    private void showDataTvShow() {
        recyclerView = findViewById(R.id.recycler_view2);

        tv_showsAdapter = new Tv_showsAdapter(this, tvShowsInstance);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tv_showsAdapter);
        restoreLayoutManagerPosition();
        tv_showsAdapter.notifyDataSetChanged();
    }

    //loading movies.
    public void loadMovies(final View v) {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.recycler_view2).setVisibility(View.GONE);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //restoreLayoutManagerPosition();
        //adapter.notifyDataSetChanged();

        swipeContainer = findViewById(R.id.main_activity);
        swipeContainer.setColorSchemeColors(android.R.color.white);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovies(v);
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        checkSortOder();

    }

    //loading tvshows.
    public void loadTvShows(final View v) {
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.recycler_view).setVisibility(View.GONE);


        tvShows = new ArrayList<>();
        tv_showsAdapter = new Tv_showsAdapter(this, tvShows);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tv_showsAdapter);
        //restoreLayoutManagerPosition();
        //adapter.notifyDataSetChanged();

        swipeContainer = findViewById(R.id.main_activity);
        swipeContainer.setColorSchemeColors(android.R.color.white);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTvShows(v);
                Toast.makeText(MainActivity.this, "TvShow Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        //checking sorting of things
        checkSortOderTvShow();

    }

    private void restoreLayoutManagerPosition() {
        if (savedRecyclerLayoutState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(LIST_STATE, moviesInstance);
        savedInstanceState.putParcelableArrayList(LIST_STATE, tvShowsInstance);
        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        moviesInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
        tvShowsInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        super.onRestoreInstanceState(savedInstanceState);
    }

    //loadfavorite movies
    private void loadFavoritesMovies(){
        recyclerView = findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getAllFavorite();
    }

    //loadfavorite tv shows
    private void loadFavoritesTvShows(){
        recyclerView = findViewById(R.id.recycler_view);

        tvShows = new ArrayList<>();
        tv_showsAdapter = new Tv_showsAdapter(this, tvShows);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tv_showsAdapter);
        tv_showsAdapter.notifyDataSetChanged();

        getAllFavoriteTvshows();
    }

    //loading popular movies
    private void loadPopularMovies(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtaib API key fron themoviedb.org", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>(){

                @Override
                public void onResponse(retrofit2.Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResult();
                    moviesInstance.addAll(movies);
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //loading popular tvshows
    private void loadPopularTvShows(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtaib API key fron themoviedb.org", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<Tv_showsResponses> call = apiService.getPopularTvShows(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<Tv_showsResponses>(){

                @Override
                public void onResponse(retrofit2.Call<Tv_showsResponses> call, Response<Tv_showsResponses> response) {
                    List<Tv_shows> tv_shows = response.body().getResult();
                    tvShowsInstance.addAll(tv_shows);
                    recyclerView.setAdapter(new Tv_showsAdapter(getApplicationContext(), tv_shows));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Tv_showsResponses> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //load top rated movies
    private void loadTopRatedMovies(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtaib API key fron themoviedb.org", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<MoviesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>(){

                @Override
                public void onResponse(retrofit2.Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResult();
                    moviesInstance.addAll(movies);
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //load top rated movies
    private void loadTopRatedTvShows(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtaib API key fron themoviedb.org", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<Tv_showsResponses> call = apiService.getTopRatedTvShows(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<Tv_showsResponses>(){

                @Override
                public void onResponse(retrofit2.Call<Tv_showsResponses> call, Response<Tv_showsResponses> response) {
                    List<Tv_shows> tv_shows = response.body().getResult();
                    tvShowsInstance.addAll(tv_shows);
                    recyclerView.setAdapter(new Tv_showsAdapter(getApplicationContext(), tv_shows));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Tv_showsResponses> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//checkorder for movies
    private void checkSortOder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.order_key),
                this.getString(R.string.most_pop)
        );
        if (sortOrder.equals(this.getString(R.string.most_pop))){
            Log.d(LOG_TAG,  "Sorting by most populsr");
            loadPopularMovies();
        } else if
                (sortOrder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
            loadFavoritesMovies();
        }
        else {
            Log.d(LOG_TAG, "Sorting by vote average");
            loadTopRatedMovies();
        }
    }

    //checkorder for movies
    private void checkSortOderTvShow() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.order_key),
                this.getString(R.string.most_pop)
        );
        if (sortOrder.equals(this.getString(R.string.most_pop))){
            Log.d(LOG_TAG,  "Sorting by most populsr");
            loadPopularTvShows();
        } else if
        (sortOrder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
            loadFavoritesTvShows();
        }
        else {
            Log.d(LOG_TAG, "Sorting by vote average");
            loadTopRatedTvShows();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // load favorites by observing changes from the database
    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite() {
        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
            }
        });
    }

    // load favorites by observing changes from the database
    @SuppressLint("StaticFieldLeak")
    private void getAllFavoriteTvshows() {
        tv_showViewModel.getAllTv_shows().observe(this, new Observer<List<Tv_shows>>() {
            @Override
            public void onChanged(@Nullable List<Tv_shows> tv_shows) {
                recyclerView.setAdapter(new Tv_showsAdapter(getApplicationContext(), tv_shows));
            }
        });
    }
}
