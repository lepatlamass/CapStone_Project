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
import com.movieapp.konwo.movieap.DetailShowActivity;
import com.movieapp.konwo.movieap.R;
import com.movieapp.konwo.movieap.model.Movie;
import com.movieapp.konwo.movieap.model.Tv_shows;

import java.util.List;

public class Tv_showsAdapter extends RecyclerView.Adapter<Tv_showsAdapter.MyViewHolder> {

    private static final String TVSHOW_KEY = "tvshow_key";
    private Context tvShowContext;
    private List<Tv_shows> tvShowsList;

    public Tv_showsAdapter(Context tvShowContext, List<Tv_shows> tvShowsList){
        this.tvShowContext = tvShowContext;
        this.tvShowsList = tvShowsList;

    }

    @Override
    public Tv_showsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Tv_showsAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(tvShowsList.get(i).getOriginalTitle());
//         viewHolder.userText.setText(String.valueOf(movieList.get(i).getVoteAverage()));


        Glide.with(tvShowContext).load(tvShowsList.get(i).getPosterpath()).into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount(){
        return tvShowsList.size();
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
                        Tv_shows clickedDataItem = tvShowsList.get(pos);
                        Intent intent = new Intent(tvShowContext, DetailShowActivity.class);
                        intent.putExtra(TVSHOW_KEY,clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        tvShowContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked" + clickedDataItem.getOriginalLanguage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
