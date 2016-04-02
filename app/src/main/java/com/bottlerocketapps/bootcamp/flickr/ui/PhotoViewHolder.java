package com.bottlerocketapps.bootcamp.flickr.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnPhotoViewClickListener {

        void onPhotoViewClick(int adapterPosition);
    }

    private OnPhotoViewClickListener mPhotoViewClickListener;

    public ImageView photo;

    public PhotoViewHolder(View itemView) {
        super(itemView);

        //TODO: Cache photo findViewById calls

        //TODO: Set up photo click listener.

    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener listener) {
        mPhotoViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        //TODO: Pass photo click to photo click listener.
    }
}
