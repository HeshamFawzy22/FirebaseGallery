package com.example.firebasegallery.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseRepository {

    private static final String WALLPAPERS = "Wallpapers";
    private FirebaseFirestore firebaseFirestore ;
    private OnFirebaseTaskComplete onFirebaseTaskComplete;

    public FirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }

    public FirebaseFirestore getFirebaseFirestore() {
        if (firebaseFirestore == null){
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseFirestore;
    }

    public void getWallpapers(){
        getFirebaseFirestore().collection(WALLPAPERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            onFirebaseTaskComplete.wallpapersAdded(task.getResult().toObjects(WallpapersModel.class));
                        }else {
                            onFirebaseTaskComplete.onError(task.getException());
                        }

                    }
                });
    }

    public interface OnFirebaseTaskComplete{
        void wallpapersAdded(List<WallpapersModel> wallpapersModels);
        void onError(Exception e);
    }
}
