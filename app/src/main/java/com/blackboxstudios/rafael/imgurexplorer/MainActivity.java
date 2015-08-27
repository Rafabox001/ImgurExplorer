package com.blackboxstudios.rafael.imgurexplorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.blackboxstudios.rafael.imgurexplorer.fragments.Detail;
import com.blackboxstudios.rafael.imgurexplorer.fragments.Principal;
import com.blackboxstudios.rafael.imgurexplorer.listeners.onNetworkDataListener;
import com.blackboxstudios.rafael.imgurexplorer.network.NetworkConnection;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.blackboxstudios.rafael.imgurexplorer.objects.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements onNetworkDataListener {

    public static final String FRAGMENT_PRINCIPAL_TAG = Principal.class.getSimpleName();
    public static final String FRAGMENT_DETAIL_TAG = Detail.class.getSimpleName();
    @Bind(R.id.toolbar) Toolbar toolbar;
    private String mSortBy;
    public static boolean two_views = false;
    private ArrayList<ImgurImage> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(5f);
        }


        two_views = findViewById(R.id.detail_container) != null;

        if(savedInstanceState == null) {
            if(two_views){
                Detail detail = new Detail();
                Bundle bundle = new Bundle();
                bundle.putParcelable("imageSelected",new ImgurImage());
                detail.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,
                        detail,FRAGMENT_DETAIL_TAG).commit();
            }
            mSortBy = Utils.getSortedBy(this);
            refreshData();
        }else{
            mImages = savedInstanceState.getParcelableArrayList("key");
        }
    }

    private void refreshData() {
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.execute(mSortBy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = Utils.getSortedBy(this);
        if(sortBy != null && !sortBy.equals(mSortBy)){
            mSortBy = sortBy;
            refreshData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key",mImages);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Settings.class));
            return true;
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutMe.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceivedData(JSONObject object) {
        JSONObject data = object;
        mImages = getImgurData(data);
        Principal main = new Principal();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", mImages);
        main.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.principal_container,
                main, FRAGMENT_PRINCIPAL_TAG).commit();
    }

    public ArrayList<ImgurImage> getImgurData(JSONObject object){

        final String RESULT_ARRAY = "data";
        final String IMAGE_ID = "id";
        final String IMAGE_TITLE = "title";
        final String IMAGE_DESCRIPTION = "description";
        final String IMAGE_LINK = "link";
        final String IMAGE_UPS = "ups";
        final String IMAGE_DOWMNS = "downs";
        final String IMAGE_POINTS = "points";
        final String IMAGE_SCORE = "score";
        final String IS_ALBUM = "is_album";
        final String IMAGE_SIZE_PX;
        final String SMALLER_IMAGE_SIZE;

        //if the width of the screen is bigger than 1000px will set w500
        int width = obtainingScreenSize(this)[0];
        IMAGE_SIZE_PX = width > 1000 ? "w500//" : "w342//";
        SMALLER_IMAGE_SIZE = (IMAGE_SIZE_PX.contains("342")) ? "w185//" : "w342//";

        ArrayList<ImgurImage> content;

        try {
            JSONArray moviesArray = object.getJSONArray(RESULT_ARRAY);
            content = new ArrayList<>();

            //Because of memory issues I´m limiting the results to 30, if necesary I will implement a method to retrieve next images

            for (int i = 0 ; i < 30 ; i++){
                ImgurImage imgurImage = new ImgurImage();
                JSONObject obj = moviesArray.getJSONObject(i);
                imgurImage.setLink(obj.getString(IMAGE_LINK));
                imgurImage.setId(obj.getString(IMAGE_ID));
                imgurImage.setTitle(obj.getString(IMAGE_TITLE));
                imgurImage.setDescription(obj.getString(IMAGE_DESCRIPTION));
                imgurImage.setUps(obj.getLong(IMAGE_UPS));
                imgurImage.setDowns(obj.getLong(IMAGE_DOWMNS));
                imgurImage.setPoints(obj.getLong(IMAGE_POINTS));
                imgurImage.setScore(obj.getLong(IMAGE_SCORE));
                imgurImage.setIs_album(obj.getString(IS_ALBUM));
                content.add(imgurImage);
            }
            return content;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] obtainingScreenSize(Context context){
        Point size = new Point();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return (new int[]{width,height});
    }
}
