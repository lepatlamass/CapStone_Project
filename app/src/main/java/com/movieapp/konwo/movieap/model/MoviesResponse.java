package com.movieapp.konwo.movieap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> result;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_page")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public List<Movie> getResult() {
        return result;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setResult(List<Movie> result) {
        this.result = result;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
