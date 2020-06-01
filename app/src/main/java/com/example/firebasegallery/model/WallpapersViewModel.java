package com.example.firebasegallery.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WallpapersViewModel extends ViewModel implements FirebaseRepository.OnFirebaseTaskComplete {

    private MutableLiveData<List<WallpapersModel>> wallPapersList = new MutableLiveData<>();
    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

    public WallpapersViewModel() {
        firebaseRepository.getWallpapers();
    }

    public LiveData<List<WallpapersModel>> getWallPapersList() {
        return wallPapersList;
    }

    @Override
    public void wallpapersAdded(List<WallpapersModel> wallpapersModels) {
        wallPapersList.setValue(wallpapersModels);
    }

    @Override
    public void onError(Exception e) {

    }
}
