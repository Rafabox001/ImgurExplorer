package com.blackboxstudios.rafael.imgurexplorer;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;


import com.blackboxstudios.rafael.imgurexplorer.fragments.Detail;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.anim_toolbar)Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.header) ImageView header;
    private ImgurImage imgurImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        imgurImage = getIntent().getExtras().getParcelable("imageSelected");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(5f);

        collapsingToolbarLayout.setTitle(imgurImage.getTitle());

        Glide.with(this).load(imgurImage.getLink()).into(header);

        Detail detail = new Detail();
        Bundle bundle = new Bundle();
        bundle.putParcelable("imageSelected", imgurImage);
        detail.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,detail).commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
