package com.blackboxstudios.rafael.imgurexplorer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import java.text.DecimalFormat;

import com.blackboxstudios.rafael.imgurexplorer.listeners.onImageSelectedListener;
import com.blackboxstudios.rafael.imgurexplorer.network.NetworkConnection;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.blackboxstudios.rafael.imgurexplorer.MainActivity;
import com.blackboxstudios.rafael.imgurexplorer.R;
import com.bumptech.glide.Glide;


public class Detail extends Fragment implements onImageSelectedListener {

    @Bind(R.id.ratingBar)RatingBar ratingBar;
    @Bind(R.id.points)ImageView pointsImage;
    @Bind(R.id.votes)ImageView votes;
    @Bind(R.id.image_detail) ImageView imageDetail;
    @Bind(R.id.description_detail) TextView description;
    @Bind(R.id.ups) TextView ups;
    @Bind(R.id.downs) TextView downs;
    @Bind(R.id.pointsText) TextView points;
    @Bind(R.id.scoreText) TextView score;
    private ImgurImage imgurImage = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgurImage = getArguments().getParcelable("imageSelected");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);

        ButterKnife.bind(this, view);

        Glide.with(getActivity()).load(imgurImage.getLink()).into(imageDetail);

        description.setText(imgurImage.getTitle());
        ups.setText(getString(R.string.ups, imgurImage.getUps()));
        downs.setText(getString(R.string.downs, imgurImage.getDowns()));
        points.setText(String.valueOf(imgurImage.getPoints()));
        score.setText(String.valueOf(imgurImage.getScore()));
        ratingBar.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));
        pointsImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));
        votes.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));

        ratingBar.setRating((float) getRating(imgurImage.getPoints()));


        return view;
    }

    @Override
    public void onImageSelected(ImgurImage imgurImageSelected) {
        imgurImage = imgurImageSelected;
        Glide.with(getActivity()).load(imgurImage.getLink()).into(imageDetail);
        description.setText(imgurImage.getTitle());
        NetworkConnection connection = new NetworkConnection(getActivity(), NetworkConnection.Request.dataRequest);
        connection.execute(new String[]{String.valueOf(imgurImage.getId())});
    }

    /** This method will evaluate the amount of points
     * in comparison with the total of votes, and depending on the result will asign a rating
     * from 1-5 stars
     * @param points This is the number of points the image has (ups - downs)
     * @return the number of stars we will asign to the image in the ratingBar
     */
    public int getRating(long points){
        long total = imgurImage.getUps() + imgurImage.getDowns();
        double score = (points * 5) / total;
        if(score < 0.5){
            return 0;
        }else if (score <1.5){
            return 1;
        }else if (score < 2.5){
            return 2;
        }else if (score < 3.5){
            return 3;
        }else if (score < 4.5){
            return 4;
        }else{
            return 5;
        }
    }
}
