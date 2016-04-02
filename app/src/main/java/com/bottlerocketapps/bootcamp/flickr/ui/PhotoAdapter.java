package com.bottlerocketapps.bootcamp.flickr.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.api.PhotoApi;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;
import com.squareup.picasso.Picasso;

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
        //Inflate photo item view
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.photo_search_grid_item, viewGroup, false);

        //Create new photo view holder object
        PhotoViewHolder viewHolder = new PhotoViewHolder(itemView);

        //Set up photo view click listener
        viewHolder.setOnPhotoViewClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder viewHolder, int position) {
        //Get data from list item at position being shown by view holder
        Photo photo = mPhotos.get(position);

        //Load the thumbnail image into the photo image view
        Picasso.with(mContext)
                .load(PhotoApi.getThumbnailPhotoUri(photo))
                .into(viewHolder.photo);
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
