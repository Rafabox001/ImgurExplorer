package com.blackboxstudios.rafael.imgurexplorer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import com.blackboxstudios.rafael.imgurexplorer.listeners.onImageSelectedListener;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.blackboxstudios.rafael.imgurexplorer.DetailActivity;
import com.blackboxstudios.rafael.imgurexplorer.MainActivity;
import com.blackboxstudios.rafael.imgurexplorer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ImgurImage> images;
    private onImageSelectedListener listener;
    private boolean isFirstTime;

    public ImageAdapter(Context context, ArrayList<ImgurImage> all_images) {
        mContext = context;
        images = all_images;
        isFirstTime = true;
    }
    public ImageAdapter(Context context, ArrayList<ImgurImage> all_movies, onImageSelectedListener listener) {
        mContext = context;
        images = all_movies;
        this.listener = listener;
        isFirstTime = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View convertView = inflater.inflate(R.layout.poster_preview_item, null, false);
        viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(mContext).load(images.get(position).getLink())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.image);
        final int pos = position;
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.two_views) {
                    listener.onImageSelected(images.get(pos));
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable("imageSelected", images.get(pos));
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtras(args);
                    mContext.startActivity(intent);
                }
            }
        });
        viewHolder.description.setText(images.get(position).getTitle());
        if(MainActivity.two_views && position == 0 && isFirstTime) {
            listener.onImageSelected(images.get(position));
            isFirstTime = false;
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imagePosterItem) ImageView image;
        @Bind(R.id.textDescription) TextView description;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
