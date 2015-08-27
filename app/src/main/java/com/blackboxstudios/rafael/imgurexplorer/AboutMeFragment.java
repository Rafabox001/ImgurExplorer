package com.blackboxstudios.rafael.imgurexplorer;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutMeFragment extends Fragment {

    @Bind(R.id.mailImage)ImageView mail;
    @Bind(R.id.linkedinImage)ImageView linkedin;
    @Bind(R.id.portfolio)ImageView udacity;
    @Bind(R.id.phoneImage)ImageView phone;
    @Bind(R.id.github)ImageView github;

    public AboutMeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me,container,false);

        ButterKnife.bind(this, view);

        /*mail.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));
        udacity.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));
        linkedin.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_inverse));
        phone.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_inverse));
        github.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_inverse));*/

        return view;
    }

    @OnClick(R.id.mailImage)
    public void sendMail(ImageView imageView){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rafabox001@gmail.com"});

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @OnClick(R.id.linkedinImage)
    public void goLinkedIn(ImageView imageView){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.linkedinUrl)));
        startActivity(i);
    }

    @OnClick(R.id.portfolio)
    public void goUdacity(ImageView imageView){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.udacityUrl)));
        startActivity(i);
    }

    @OnClick(R.id.github)
    public void goGithub(ImageView imageView){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.githubUrl)));
        startActivity(i);
    }

    @OnClick(R.id.phoneImage)
    public void callMe(ImageView imageView){

        String uri = "tel:" + getString(R.string.phone).trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
