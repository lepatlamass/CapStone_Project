package com.movieapp.konwo.movieap.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.movieapp.konwo.movieap.DetailActivity;
import com.movieapp.konwo.movieap.R;
import com.movieapp.konwo.movieap.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private static final String MOVIE_KEY = "movie_key";
    private Context movieContext;
    private List<Movie> movieList;

    public MoviesAdapter(Context movieContext, List<Movie> movieList){
        this.movieContext = movieContext;
        this.movieList = movieList;

    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(movieList.get(i).getOriginalTitle());
//         viewHolder.userText.setText(String.valueOf(movieList.get(i).getVoteAverage()));


        Glide.with(movieContext).load(movieList.get(i).getPosterpath()).into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, userText;
        public ImageView thumbnail;

        public MyViewHolder(View view){
            super(view);
            title =  view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(movieContext, DetailActivity.class);
                        intent.putExtra(MOVIE_KEY,clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        movieContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked" + clickedDataItem.getOriginalLanguage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
