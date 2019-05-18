package com.movieapp.konwo.movieap.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("poster_path")
    private String posterpath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backDropPath;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;

    //constructor for movie
    public Movie(Integer id, String posterpath, boolean adult, String overview,
                 String releaseDate, String originalTitle,
                 String originalLanguage, String title, String backDropPath, Integer voteCount,
                 boolean video, Double voteAverage) {
        this.id = id;
        this.posterpath = posterpath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backDropPath = backDropPath;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        posterpath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backDropPath = in.readString();
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        video = in.readByte() != 0;
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterpath() {
        return "https://image.tmdb.org/t/p/w500" + posterpath;
    }

    //generated getter
    public Integer getId() {
        return id;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    //generated setter
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(posterpath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backDropPath);
        if (voteCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteCount);
        }
        dest.writeByte((byte) (video ? 1 : 0));
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
    }
}
