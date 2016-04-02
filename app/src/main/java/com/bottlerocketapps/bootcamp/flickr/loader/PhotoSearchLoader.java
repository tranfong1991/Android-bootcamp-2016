package com.bottlerocketapps.bootcamp.flickr.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.bottlerocketapps.bootcamp.flickr.api.PhotoApi;
import com.bottlerocketapps.bootcamp.flickr.model.PhotoSearchResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoSearchLoader extends AsyncTaskLoader<PhotoSearchResult>{

    private static final String TAG = PhotoSearchLoader.class.getSimpleName();

    private Uri mSearchUri;
    private PhotoSearchResult mResults;
    private OkHttpClient mOkHttpClient;

    public PhotoSearchLoader(Context context, String searchTag) {
        super(context);
        mSearchUri = PhotoApi.getSearchUri(context, searchTag);
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        
        if (mResults != null) {
            //If we already have results, just deliver them. 
            deliverResult(mResults);
        } else {
            //We must call forceLoad to start loadInBackground().
            forceLoad();
        }
    }

    @Override
    public PhotoSearchResult loadInBackground() {
        PhotoSearchResult result = null;
        
        try {
            //Request the data from the flickr web service.
            URL url = new URL(mSearchUri.toString());
            
            Log.d(TAG, "Downloading " + url);
            String response = get(url);

            //Parse the result into a PhotoSearchResult object.
            result = PhotoSearchResult.fromJson(response);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in loadInBackground()", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException in loadInBackground()", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in loadInBackground()", e);
        }

        return result;
    }

    /**
     * Perform an HTTP GET request for the specified URL then 
     * return the result as a String.
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private String get(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected void onReset() {
        super.onReset();
        
        //Discard the result. This exact result will no longer be needed.
        mResults = null;
    }
}
