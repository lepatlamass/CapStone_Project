package com.movieapp.konwo.movieap.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class Tv_shows implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("poster_path")
    private String posterpath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("first_air_date")
    private String releaseDate;
    @SerializedName("original_name")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("name")
    private String title;
    @SerializedName("backdrop_path")
    private String backDropPath;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("vote_average")
    private Double voteAverage;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
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

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getId() {
        return id;
    }

    public String getPosterpath() {
        return "https://image.tmdb.org/t/p/w500" + posterpath;
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    //constructor tv_shows
    public Tv_shows (
            Integer id, String posterpath, String overview, String releaseDate, String originalTitle, String originalLanguage, String title, String backDropPath,
            Integer voteCount, Double voteAverage){
        this.id = id;
        this.posterpath = posterpath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backDropPath = backDropPath;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    protected Tv_shows (Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        posterpath = in.readString();
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

        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    public static final Creator<Tv_shows> CREATOR = new Creator<Tv_shows>() {
        @Override
        public Tv_shows createFromParcel(Parcel in) {
            return new Tv_shows(in);
        }

        @Override
        public Tv_shows[] newArray(int i) {
            return new Tv_shows[i];
        }
    };

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
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
    }

}
