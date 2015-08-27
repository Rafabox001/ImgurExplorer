package com.blackboxstudios.rafael.imgurexplorer.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.blackboxstudios.rafael.imgurexplorer.listeners.onNetworkDataListener;
import com.blackboxstudios.rafael.imgurexplorer.objects.ImgurImage;
import com.blackboxstudios.rafael.imgurexplorer.R;
import com.blackboxstudios.rafael.imgurexplorer.objects.Utils;


public class NetworkConnection extends AsyncTask<String,Void,Boolean> {

    private final String NETWORK_TAG = NetworkConnection.class.getSimpleName();
    private final Context mContext;
    private onNetworkDataListener listener;
    private JSONObject data;
    private String responseJsonStr = null;
    private List<ImgurImage> movies;
    private Request typeRequest;

    public NetworkConnection(Context c){
        mContext = c;
        listener = (onNetworkDataListener) mContext;
        typeRequest = Request.dataRequest;
    }

    public NetworkConnection(Context c, Request type){
        mContext = c;
        listener = (onNetworkDataListener) mContext;
        typeRequest = type;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Uri requestURL = null;
        final String BASE_URL = "https://api.imgur.com/3";
        final String IMGUR_SECTION_PATH = Utils.getSection(mContext);
        String IMGUR_SORT_PATH = Utils.getSortedBy(mContext);
        String IMGUR_WINDOW_PATH = Utils.getWindow(mContext);
        final String BASE_URL_IMGUR = "gallery";
        final String VIRAL_PARAM = "showViral";
        //If user selected rising sort in a category different to user we change the value back to its default (viral in this case)
        if (IMGUR_SORT_PATH.equals("rising") && !IMGUR_SECTION_PATH.contentEquals("user")){
            IMGUR_SORT_PATH = "viral";
        }

        switch (typeRequest){
            case dataRequest:{
                String sortBy = String.valueOf(Utils.showViral(mContext));

                if (IMGUR_SECTION_PATH.contentEquals("top")){
                    //Construction of the request URL for top section (we can add window parameter in this section)
                    requestURL = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(BASE_URL_IMGUR)
                            .appendPath(IMGUR_SECTION_PATH)
                            .appendPath(IMGUR_SORT_PATH)
                            .appendPath(IMGUR_WINDOW_PATH)
                            .appendPath("0.json")
                            .appendQueryParameter(VIRAL_PARAM, sortBy)
                            .build();
                }else{
                    //Construction of the request URL
                    requestURL = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(BASE_URL_IMGUR)
                            .appendPath(IMGUR_SECTION_PATH)
                            .appendPath(IMGUR_SORT_PATH)
                            .appendPath("0.json")
                            .appendQueryParameter(VIRAL_PARAM, sortBy)
                            .build();
                }
                break;
            }
        }
        try{
            Log.w(NETWORK_TAG,requestURL.toString());
        }catch (NullPointerException e){
            Log.w(NETWORK_TAG,"Error url");
        }
        return retrieveData(requestURL);
    }

    private boolean retrieveData(Uri requestedURL){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{

            //Final URL for request
            URL url = new URL(requestedURL.toString());

            //Creation for the request of movies data
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("Authorization", "Client-ID " + "8388822b74f310b");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return false; //Nothing to do.
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return false; //Has no lines. String empty.
            }

            responseJsonStr = buffer.toString();
            Log.w(NETWORK_TAG,responseJsonStr);
            return true;
        }catch (IOException e){
            Log.e(NETWORK_TAG,e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            try {
                data = new JSONObject(responseJsonStr);
                if(typeRequest == Request.dataRequest) {
                    listener.onReceivedData(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public static enum Request {
        videoRequest,dataRequest,reviewsRequest
    }


}
