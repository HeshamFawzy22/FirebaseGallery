package com.example.firebasegallery.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebasegallery.R;
import com.example.firebasegallery.model.WallpapersModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WallpapersListAdapter extends RecyclerView.Adapter<WallpapersListAdapter.MyViewHolder> {

    private List<WallpapersModel>models;


    private OnItemClicked onItemClicked;

    public WallpapersListAdapter(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final String imageUrl = models.get(position).getImage();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .centerCrop()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.onItemClick(imageUrl);
            }
        });

    }


    @Override
    public int getItemCount(){
        return models == null ? 0 : models.size();
    }

    public void setModels(List<WallpapersModel> models) {
        this.models = models;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ProgressBar progressBar;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_single_image);
            progressBar = itemView.findViewById(R.id.list_single_progress);
        }


    }
    public interface OnItemClicked{
        void onItemClick(String img);
    }
}
