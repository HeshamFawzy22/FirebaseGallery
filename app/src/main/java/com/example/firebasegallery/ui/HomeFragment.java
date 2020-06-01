package com.example.firebasegallery.ui;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.firebasegallery.R;
import com.example.firebasegallery.adapters.WallpapersListAdapter;
import com.example.firebasegallery.model.WallpapersModel;
import com.example.firebasegallery.model.WallpapersViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements WallpapersListAdapter.OnItemClicked {


    //UI Elements
    private RecyclerView recyclerView;


    //Declare
    private NavController navController;
    private WallpapersListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WallpapersViewModel wallpapersViewModel;

    private FirebaseAuth firebaseAuth;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Check if user logged in
        checkUseer();

        //Initialize navController
        navController = Navigation.findNavController(view);

        //Initialize recyclerView
        recyclerView = view.findViewById(R.id.wallpapers_list);
        listAdapter = new WallpapersListAdapter(this);
        layoutManager = new GridLayoutManager(getContext(),3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(layoutManager);

        //Reached Bottom Of Recycler View
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void checkUseer() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user == null){
            //Not Logged in
            navController.navigate(R.id.action_homeFragment_to_registerFragment);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        wallpapersViewModel = new ViewModelProvider(getActivity()).get(WallpapersViewModel.class);
        wallpapersViewModel.getWallPapersList().observe(getViewLifecycleOwner(), new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                listAdapter.setModels(wallpapersModels);

                listAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onItemClick(String img) {
        //Navigate to DetailsFragment
        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment();
        action.setWallpaperimage(img);
        navController.navigate(action);
    }
}
