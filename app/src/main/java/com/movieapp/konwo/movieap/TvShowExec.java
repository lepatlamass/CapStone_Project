package com.movieapp.konwo.movieap;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class TvShowExec implements Executor {

    @Override
    public void execute(@NonNull Runnable command) {
        new Thread(command).start();
    }
}
