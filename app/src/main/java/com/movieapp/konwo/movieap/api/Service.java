package com.movieapp.konwo.movieap.api;

import com.movieapp.konwo.movieap.model.MoviesResponse;
import com.movieapp.konwo.movieap.model.ReviewResponse;
import com.movieapp.konwo.movieap.model.TrailerResponse;
import com.movieapp.konwo.movieap.model.Tv_showsResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    // movie api
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apikey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apikey);

    //api tv_shows
    @GET("tv/popular")
    Call<Tv_showsResponses> getPopularTvShows(@Query("api_key") String apikey);

    @GET("tv/top_rated")
    Call<Tv_showsResponses> getTopRatedTvShows(@Query("api_key") String apikey);

    //api tv_shows trailer
    @GET("tv/{id}/videos")
    Call<TrailerResponse> getTvTrailer(@Path("id") int id, @Query("api_key") String apikey);

    @GET("tv/{tv_id}/reviews")
    Call<ReviewResponse> getTvReview(@Path("tv_id") int id, @Query("api_key") String apikey);


    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("id") int id, @Query("api_key") String apikey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReview(@Path("movie_id") int id, @Query("api_key") String apikey);

}
