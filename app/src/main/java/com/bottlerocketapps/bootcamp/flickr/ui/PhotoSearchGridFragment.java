package com.bottlerocketapps.bootcamp.flickr.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.loader.PhotoSearchLoader;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;
import com.bottlerocketapps.bootcamp.flickr.model.PhotoSearchResult;

import org.w3c.dom.Text;

public class PhotoSearchGridFragment extends Fragment {

    private static final int LOADER_ID_SEARCH = 1;
    private static final String LOADER_ARG_QUERY = "query";
    private static final String SAVED_STATE_QUERY = "query";

    private TextView mStatusText;
    private String mQuery;
    private boolean mSearchLoaderStarted;
    private PhotoAdapter mPhotoGridAdapter;

    public static PhotoSearchGridFragment newInstance() {
        return new PhotoSearchGridFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Required in order for a Fragment to get a call to onCreateOptionsMenu()
        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            mQuery = savedInstanceState.getString(SAVED_STATE_QUERY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(!TextUtils.isEmpty(mQuery)){
            performSearch(mQuery);
        }
    }

    //Called in cases like when the screen is rotated, instead of clean everything on the screen, it saves the current query and search for it again in onActivityCreated()
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_STATE_QUERY, mQuery);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout resource.
        View root = inflater.inflate(R.layout.photo_search_grid_fragment, container, false);

        //Obtain the recycler view from the layout.
        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.psg_grid);

        //Create a vertical grid layout manager with a span count of 4.
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);

        //Set the layout manager of the recycler view.
        recyclerView.setLayoutManager(layoutManager);

        //Create a new photo adapter.
        mPhotoGridAdapter = new PhotoAdapter(getActivity());

        //Setup the on photo item click listener.
        mPhotoGridAdapter.setOnPhotoClickListener(mPhotoClickListener);

        //Attach the adapter to the recycler view.
        recyclerView.setAdapter(mPhotoGridAdapter);

        //Obtain the text view to display status.
        mStatusText = (TextView) root.findViewById(R.id.psg_status);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Allow parent to inflate its menu.
        super.onCreateOptionsMenu(menu, inflater);

        //Inflate our menu using the base menu.
        inflater.inflate(R.menu.photo_search_grid, menu);

        //Find the search ActionView and attach a listener to it.
        MenuItem item = menu.findItem(R.id.psg_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
    }

    /*
     * Listener for changes to the search ActionView. 
     */
    private OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            performSearch(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    /**
     * Start or restart the search loader for the provided search query.
     */
    protected void performSearch(String query) {
        if (!TextUtils.isEmpty(query)) {

            //Inform the user that we are searching.
            mStatusText.setVisibility(View.VISIBLE);
            mStatusText.setText(R.string.pgs_status_searching);

            //Remove previous results.
            mPhotoGridAdapter.clear();

            //Put the search argument into a bundle.
            Bundle args = new Bundle();
            args.putString(LOADER_ARG_QUERY, query);

            //Initialize loader
            if(mSearchLoaderStarted && !TextUtils.equals(mQuery, query)){
                getLoaderManager().restartLoader(LOADER_ID_SEARCH, args, mSearchLoaderCallbacks);
            } else {
                getLoaderManager().initLoader(LOADER_ID_SEARCH, args, mSearchLoaderCallbacks);
            }

            //Save the search value and the fact that this instance has started the loader.
            mQuery = query;
            mSearchLoaderStarted = true;
        }
    }

    private LoaderCallbacks<PhotoSearchResult> mSearchLoaderCallbacks = new LoaderCallbacks<PhotoSearchResult>() {

        @Override
        public Loader<PhotoSearchResult> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case LOADER_ID_SEARCH:
                    if (args != null) {
                        //Create a new instance of the photo search loader with the provided query.
                        return new PhotoSearchLoader(getActivity(), args.getString(LOADER_ARG_QUERY));
                    }
                    break;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<PhotoSearchResult> loader, PhotoSearchResult result) {
            switch (loader.getId()) {
                case LOADER_ID_SEARCH:
                    //Display the results.
                    updateResult(result);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<PhotoSearchResult> loader) {
            switch (loader.getId()) {
                case LOADER_ID_SEARCH:
                    //Clear the grid.
                    updateResult(null);
                    break;
            }
        }
    };

    private PhotoSearchGridFragmentListener mFragmentListener;

    private void updateResult(PhotoSearchResult result) {
        if (result != null) {
            mPhotoGridAdapter.swapItems(result.getPhotos());
        } else {
            mPhotoGridAdapter.clear();
        }

        if (mPhotoGridAdapter.getItemCount() > 0) {
            //Hide status text if we received results.
            mStatusText.setVisibility(View.INVISIBLE);
        } else {
            //Display no results message.
            mStatusText.setVisibility(View.VISIBLE);
            mStatusText.setText(R.string.pgs_status_no_results);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity)context;

        //When the activity is attached we enforce that it is a listener for this fragment and store a reference.
        if (activity instanceof PhotoSearchGridFragmentListener) {
            mFragmentListener = (PhotoSearchGridFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.getClass().getCanonicalName() + " must implement " + PhotoSearchGridFragmentListener.class.getCanonicalName());
        }
    }

    /*
     * Listener for individual photos being touched in the grid. 
     */
    private PhotoAdapter.OnPhotoClickListener mPhotoClickListener = new PhotoAdapter.OnPhotoClickListener() {
        @Override
        public void onPhotoClicked(Photo photo) {
            mFragmentListener.onPhotoSelected(photo);
        }
    };

    /**
     * Listener that will receive callbacks from the PhotoSearchGridFragment when a photo is selected.
     */
    public interface PhotoSearchGridFragmentListener {
        void onPhotoSelected(Photo selectedPhoto);
    }

}
