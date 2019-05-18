package com.movieapp.konwo.movieap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("id")
    private int id_trailer;
    // This method must be declared public in order to get the trailer response result
    @SerializedName("results")
    public List<Trailer> results;

    public void setId_trailer(int id_trailer) {
        this.id_trailer = id_trailer;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    public int getId_trailer() {
        return id_trailer;
    }

    public List<Trailer> getResults() {
        return results;
    }

}
