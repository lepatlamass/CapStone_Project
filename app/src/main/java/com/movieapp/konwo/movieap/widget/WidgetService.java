package com.movieapp.konwo.movieap.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.movieapp.konwo.movieap.model.Movie;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    private List<Movie> movies;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }


}
