package com.example.firebasegallery.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.firebasegallery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {

    //UI Elements
    TextView welcomeText;
    ProgressBar progressBar;

    //Decalre
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    //Log
    private static final String START_TAG = "START_LOG";

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeText = view.findViewById(R.id.register_text);
        progressBar = view.findViewById(R.id.register_progress);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();

        welcomeText.setText("Checking User Account...");

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser current_user = firebaseAuth.getCurrentUser();
        if (current_user == null){
            welcomeText.setText("Creating Account...");
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                welcomeText.setText("Account Created Successfully \n Welcome to OnLine Wallpapers");
                                navController.navigate(R.id.action_registerFragment_to_homeFragment);
                            }else {
                                Log.e(START_TAG,"Start Log : "+task.getException().getMessage());
                            }
                        }
                    });
        }else {
            //Navigate To HomeFragment
            welcomeText.setText("Logged In...");
            navController.navigate(R.id.action_registerFragment_to_homeFragment);
        }

    }

}
