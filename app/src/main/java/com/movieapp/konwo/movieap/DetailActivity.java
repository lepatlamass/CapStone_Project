package com.movieapp.konwo.movieap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.movieapp.konwo.movieap.adapter.ReviewAdapter;
import com.movieapp.konwo.movieap.adapter.TrailerAdapter;
import com.movieapp.konwo.movieap.api.Client;
import com.movieapp.konwo.movieap.api.Service;
import com.movieapp.konwo.movieap.data.MovieDatabase;
import com.movieapp.konwo.movieap.data.Tv_showsDatabase;
import com.movieapp.konwo.movieap.model.Movie;
import com.movieapp.konwo.movieap.model.ReviewResponse;
import com.movieapp.konwo.movieap.model.Review;
import com.movieapp.konwo.movieap.model.Trailer;
import com.movieapp.konwo.movieap.model.TrailerResponse;
import com.movieapp.konwo.movieap.model.Tv_shows;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {

    // movie key from Parcelable value
    private static final String MOVIE_KEY = "movie_key";
    private static final String TVSHOW_KEY = "tvshow_key";
    private static final String TAG = DetailActivity.class.getSimpleName();

    TextView movieOfName, plotSynopsis, userRating, releaseDate;
    ImageView imageView;

    private MultiSnapRecyclerView recyclerView;
    private MultiSnapRecyclerView reviewRecycler;
    private TrailerAdapter adapter;
    private Movie movie;
    private Tv_shows tv_shows;
    private MovieDatabase movieDb;
    private Tv_showsDatabase tv_showsDatabase;
    private Executor executor;
    private ReviewAdapter reviewAdapter;
    private boolean isFavorite;

    private final AppCompatActivity activity = DetailActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        initCollapsingToolbar();

        movieDb = MovieDatabase.getDatabase(this);
        tv_showsDatabase = Tv_showsDatabase.getDatabase(this);
        executor = new MovieExec();
        executor = new TvShowExec();

        imageView = findViewById(R.id.thumbnail_image_header);
        movieOfName = findViewById(R.id.title);
        plotSynopsis = findViewById(R.id.overviewText);
        userRating = findViewById(R.id.userText);
        releaseDate = findViewById(R.id.releaseDate);

        // get movie parcelable from intent
        Intent intentThatStartedThisActivity = getIntent();
        movie = intentThatStartedThisActivity.getParcelableExtra(MOVIE_KEY);

//        Intent intentThatStartedThisActivityStart = getIntent();
//        tv_shows = intentThatStartedThisActivityStart.getParcelableExtra(TVSHOW_KEY);


//        //movie details
        movieOfName.setText(movie.getOriginalTitle());
        plotSynopsis.setText(movie.getOverview());
        String movieName = movie.getOriginalTitle();
        Double voteCount = movie.getVoteAverage();
        userRating.setText(String.valueOf(voteCount));
        releaseDate.setText(movie.getReleaseDate());
        Glide.with(this).load(movie.getPosterpath()).into(imageView);

//        //tv show details
//        movieOfName.setText(tv_shows.getOriginalTitle());
//        plotSynopsis.setText(tv_shows.getOverview());
//        String tvShowName = tv_shows.getOriginalTitle();
//        Double voteCounttv = tv_shows.getVoteAverage();
//        userRating.setText(String.valueOf(voteCounttv));
//        releaseDate.setText(tv_shows.getReleaseDate());
//        Glide.with(this).load(tv_shows.getPosterpath()).into(imageView);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(movieName);
    //    ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(tvShowName);

        final MaterialFavoriteButton materialFavoriteButton = findViewById(R.id.favorite_button);
        materialFavoriteButton.setFavorite(false);

        // check if movie is already in favorites
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Movie movie1 = movieDb.movieDAO().getMovieById(movie.getId());
                // if movi1 is null, movie is'nt in favorite
                if (movie1 == null) {
                    // set the button state to false
                    materialFavoriteButton.setFavorite(false);
                } else {
                    // movie is already in favorite, set the button state to true
                    materialFavoriteButton.setFavorite(true);
                }
            }
        });

//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                Tv_shows tv_shows1 = tv_showsDatabase.tvShowDAO().getMovieById(tv_shows.getId());
//                // if movi1 is null, movie is'nt in favorite
//                if (tv_shows1 == null) {
//                    // set the button state to false
//                    materialFavoriteButton.setFavorite(false);
//                } else {
//                    // movie is already in favorite, set the button state to true
//                    materialFavoriteButton.setFavorite(true);
//                }
//            }
//        });

        // check if tvshow is already in favorites

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        materialFavoriteButton.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        // if movie is favorite
                        if (favorite) {
                            SharedPreferences.Editor editor =
                                    getSharedPreferences(".DetailActivity", MODE_PRIVATE).edit();
                            saveFavorite();
                            editor.putBoolean("Added to favorites", true);
                            editor.apply();
                            Snackbar.make(buttonView, "added to favorite", Snackbar.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences.Editor editor =
                                    getSharedPreferences(".DetailActivity", MODE_PRIVATE).edit();
                            removeFavorite();
                            editor.putBoolean("Removed from favorites", true);
                            editor.apply();
                            Snackbar.make(buttonView, "removed from Favorite", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        );

       initViews();
    }


    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view1);
        reviewRecycler = findViewById(R.id.review_recycler);
        // call to populateTrailers()
        populateTrailers();
        populateReviews();
//        populateTrailersShows();
//        populateReviewsShow();
    }

    //populating trailer shows
    private void populateTrailersShows() {
        // set the LayoutManager
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new TrailerAdapter(this);
        recyclerView.setAdapter(adapter);

        // load shows
        Service service = Client.getClient().create(Service.class);
        Call<TrailerResponse> call = service.getTvTrailer(tv_shows.getId(), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()) {
                    // TrailerResponse's results;
                    List<Trailer> trailers = response.body().results;
                    adapter.setItems(trailers);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //populating movie trailers
    private void populateTrailers() {
        // set the LayoutManager
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new TrailerAdapter(this);
        recyclerView.setAdapter(adapter);

        // load movies
        Service service = Client.getClient().create(Service.class);
        Call<TrailerResponse> call = service.getMovieTrailer(movie.getId(), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()) {
                    // TrailerResponse's results;
                    List<Trailer> trailers = response.body().results;
                    adapter.setItems(trailers);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //populate tv reviews
    private void populateReviewsShow() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setNestedScrollingEnabled(false);

        reviewAdapter = new ReviewAdapter(this);
        reviewRecycler.setAdapter(reviewAdapter);

        // load reviews
        Service service = Client.getClient().create(Service.class);
        Call<ReviewResponse> call = service.getTvReview(tv_shows.getId(), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    List<Review> reviews = response.body().results;
                    reviewAdapter.setItems(reviews);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
    }

    //populate movie reviews
    private void populateReviews() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setNestedScrollingEnabled(false);

        reviewAdapter = new ReviewAdapter(this);
        reviewRecycler.setAdapter(reviewAdapter);

        // load reviews
        Service service = Client.getClient().create(Service.class);
        Call<ReviewResponse> call = service.getReview(movie.getId(), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    List<Review> reviews = response.body().results;
                    reviewAdapter.setItems(reviews);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
    }

    /** private void Review(){
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please get your API Key", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Client client = new Client();
                Service apiService = Client.getClient().create(Service.class);
                Call<ReviewResponse> call = apiService.getReview(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);

                call.enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null) {
                                List<Review> reviewResults = response.body().getResults();
                                MultiSnapRecyclerView recyclerView2 = findViewById(R.id.review_recycler);
                                LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                recyclerView2.setLayoutManager(firstManager);
                                recyclerView2.setAdapter(new ReviewAdapter(getApplicationContext(), reviewResults));
                                recyclerView2.smoothScrollToPosition(0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, "unable to fetch data", Toast.LENGTH_SHORT).show();
        }
    } */

    public void saveFavorite () {
        // add a selected movie to favorite
        executor.execute(new Runnable() { // running in separate thread
            @Override
            public void run() {
                movieDb.movieDAO().insert(movie);
            }
        });

    }

    private void removeFavorite() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                movieDb.movieDAO().delete(movie);
            }
        });

    }

}
