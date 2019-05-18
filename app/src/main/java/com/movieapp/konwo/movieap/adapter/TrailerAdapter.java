package com.movieapp.konwo.movieap.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.movieapp.konwo.movieap.R;
import com.movieapp.konwo.movieap.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

//    private List<Trailer> trailerList = new ArrayList();
    private Context mContext;
    private List<Trailer> trailerList;

    public TrailerAdapter(Context mContext){
        this.mContext = mContext;
    }

    public TrailerAdapter(Context mContext, List<Trailer> trailerList){
        this.mContext = mContext;
        this.trailerList = trailerList;
    }


    @NonNull
    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.MyViewHolder viewHolder, int i) {
        Trailer trailerItem = trailerList.get(i);
        viewHolder.title.setText(trailerList.get(i).getName());

        Uri thumbnailUri = Uri.parse("https://img.youtube.com/vi/")
                .buildUpon().appendPath(trailerItem.getKey()).appendPath("hqdefault.jpg").build();
        Glide.with(mContext)
                .load(thumbnailUri)
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }
  
    // instanciate a new ArrayList then add all trailers items in
    public void setItems(List<Trailer> items) {
        trailerList = new ArrayList<>();
        this.trailerList.addAll(items);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Trailer clickedDataItem = trailerList.get(pos);
                        String videoId = trailerList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
                        intent.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(intent);
                        Toast.makeText(view.getContext(), "You Clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
