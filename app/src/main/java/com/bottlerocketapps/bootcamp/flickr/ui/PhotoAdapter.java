package com.bottlerocketapps.bootcamp.flickr.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bottlerocketapps.bootcamp.flickr.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder>
        implements PhotoViewHolder.OnPhotoViewClickListener {

    public interface OnPhotoClickListener {

        void onPhotoClicked(Photo photo);
    }

    private Context mContext;
    private List<Photo> mPhotos;
    private OnPhotoClickListener mListener;

    public PhotoAdapter(Context context) {
        mContext = context;
        mPhotos = new ArrayList<>();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //TODO: Inflate photo item view

        //TODO: Create new photo view holder object
        PhotoViewHolder viewHolder = null;

        //TODO: Set up photo view click listener

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder viewHolder, int position) {
        //TODO: Get data from list item at position being shown by view holder

        //TODO: Load the thumbnail image into the photo image view
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mListener = listener;
    }

    public void swapItems(List<Photo> photos) {
        //Create a shallow copy of the supplied list to avoid
        //updates to list outside of this adapter.
        mPhotos = new ArrayList<>(photos);

        //Notify the adapter that the data has been modified.
        notifyDataSetChanged();
    }

    public void clear() {
        //Empty the list
        mPhotos.clear();

        //Notify the adapter that the data has been modified.
        notifyDataSetChanged();
    }

    @Override
    public void onPhotoViewClick(int adapterPosition) {
        if (mListener != null) {
            mListener.onPhotoClicked(mPhotos.get(adapterPosition));
        }
    }
}
