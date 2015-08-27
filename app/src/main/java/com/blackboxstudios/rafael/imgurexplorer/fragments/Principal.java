package com.blackboxstudios.rafael.imgurexplorer.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.blackboxstudios.rafael.imgurexplorer.Settings;
import com.blackboxstudios.rafael.imgurexplorer.adapters.ImageAdapter;
import com.blackboxstudios.rafael.imgurexplorer.listeners.onImageSelectedListener;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.blackboxstudios.rafael.imgurexplorer.MainActivity;
import com.blackboxstudios.rafael.imgurexplorer.R;
import com.blackboxstudios.rafael.imgurexplorer.objects.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Principal extends Fragment {

    @Bind(R.id.gridView) RecyclerView grid;
    private ArrayList<ImgurImage> images;
    int numColumns = 3;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = getArguments().getParcelableArrayList("key");


        //listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                if(key.equals(getString(R.string.layout_list_key))){
                    Log.d("Layout preference: ", key);
                    String displayMode = Utils.getLayout(getActivity());
                    Log.d("LAYOUT_MODE", displayMode);
                    switch (displayMode){
                        case "grid":
                            GridLayoutManager glm = new GridLayoutManager(getActivity(),numColumns);
                            grid.setHasFixedSize(true);
                            grid.setLayoutManager(glm);
                            break;
                        case "list":
                            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            grid.setHasFixedSize(true);
                            grid.setLayoutManager(llm);
                            break;
                        case "staggered":
                            StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
                            sgm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                            grid.setLayoutManager(sgm);
                            break;
                    }
                    ImageAdapter adapter;
                    if(MainActivity.two_views){
                        adapter = new ImageAdapter(getActivity(),images, (onImageSelectedListener) getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_DETAIL_TAG));
                    }else {
                        adapter = new ImageAdapter(getActivity(),images);
                    }
                    grid.setAdapter(adapter);
                    //grid.setOnScrollListener(onScrollListener);
                    adapter.notifyDataSetChanged();
                }


            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal,null,false);
        ButterKnife.bind(this, view);
        makeAnUpdate(images);
        return view;
    }

    public void makeAnUpdate(ArrayList<ImgurImage> images){

        int[] sizes = MainActivity.obtainingScreenSize(getActivity());
        int width = sizes[0];
        int height = sizes[1];
        if(grid.getTag().toString().equalsIgnoreCase(getActivity().getString(R.string.phone_tag))) {
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numColumns = (width > 2000 && height > 1000) ? 5 : 3; //landscape mode for nexus 6
            }else {
                //set 3 columns in the grid if the device has more than 1000px width and more than 2000px like nexus 6 (portrait)
                numColumns = (width > 1000 && height > 2000) ? 3 : 2;
            }
        }
        if(grid.getTag().toString().equalsIgnoreCase(getActivity().getString(R.string.tablet_tag))) {
            numColumns = (width > 1000 && height > 2000) ? 2 : 3;
        }
        String displayMode = Utils.getLayout(getActivity());
        Log.d("LAYOUT_MODE", displayMode);
        switch (displayMode){
            case "grid":
                GridLayoutManager glm = new GridLayoutManager(getActivity(),numColumns);
                grid.setHasFixedSize(true);
                grid.setLayoutManager(glm);
                break;
            case "list":
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                grid.setHasFixedSize(true);
                grid.setLayoutManager(llm);
                break;
            case "staggered":
                StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
                sgm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                grid.setLayoutManager(sgm);
                break;
        }
        ImageAdapter adapter;
        if(MainActivity.two_views){
            adapter = new ImageAdapter(getActivity(),images, (onImageSelectedListener) getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_DETAIL_TAG));
        }else {
            adapter = new ImageAdapter(getActivity(),images);
        }
        grid.setAdapter(adapter);
        //grid.setOnScrollListener(onScrollListener);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
