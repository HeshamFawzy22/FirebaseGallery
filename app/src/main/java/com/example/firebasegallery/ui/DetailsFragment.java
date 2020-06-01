package com.example.firebasegallery.ui;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import io.grpc.Context;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebasegallery.R;

import java.io.IOException;

public class DetailsFragment extends Fragment {

    //UI
    private ImageView detailsImage;
    private Button detailsWallpaperBtn;
    private ProgressBar detailsProgress;

    //Declare
    private String imagUrl;
    private Bitmap bitmap , bitmap2;
    private DisplayMetrics displayMetrics ;
    private int width, height;
    BitmapDrawable bitmapDrawable;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagUrl = DetailsFragmentArgs.fromBundle(getArguments()).getWallpaperimage();

        detailsImage = view.findViewById(R.id.details_image);
        detailsWallpaperBtn = view.findViewById(R.id.details_btn);
        detailsProgress = view.findViewById(R.id.details_progress);

        //set image
        Glide.with(getContext())
                .load(imagUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //Image Loaded Show Wallpaper Button
                        detailsWallpaperBtn.setVisibility(View.VISIBLE);

                        //Hide Progress Bar
                        detailsProgress.setVisibility(View.INVISIBLE);

                        return false;
                    }
                })
                .centerCrop()
                .into(detailsImage);

        detailsWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWallpaper(){
        //Change text and Disable Button
        detailsWallpaperBtn.setEnabled(false);
        detailsWallpaperBtn.setText("Wallpaper Set");
        detailsWallpaperBtn.setTextColor(getResources().getColor(R.color.colorDark,null));

        bitmapDrawable = (BitmapDrawable) detailsImage.getDrawable();

        bitmap = bitmapDrawable.getBitmap();

        //Background Task
        SetWallpaperTask setWallpaperTask = new SetWallpaperTask(getContext(),bitmap);
        setWallpaperTask.execute(true);

    }

    class SetWallpaperTask extends AsyncTask<Boolean,String,String>{
        android.content.Context context;
        Bitmap bitmap;

        SetWallpaperTask(android.content.Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            //Get The Best Size For Image
            GetScreenWidthHeight();
            SetBitmapSize();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
            try {
                //Set Wallpaper Background
                wallpaperManager.setBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void GetScreenWidthHeight(){

        displayMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels;

    }

    private void SetBitmapSize(){
        bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
