package com.movieapp.konwo.movieap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movieapp.konwo.movieap.R;
import com.movieapp.konwo.movieap.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Review> reviewResults;

    public ReviewAdapter(Context mContext, List<Review> reviewResults) {
        this.mContext = mContext;
        this.reviewResults = reviewResults;
    }

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(reviewResults.get(i).getAuthor());
        myViewHolder.content.setText(reviewResults.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewResults == null ? 0 : reviewResults.size();
    }

    public void setItems(List<Review> reviews) {
        reviewResults = new ArrayList<>();
        this.reviewResults.addAll(reviews);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.tv_review_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }

}
