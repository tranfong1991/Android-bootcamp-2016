package com.bottlerocketapps.bootcamp.flickr.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bottlerocketapps.bootcamp.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnPhotoViewClickListener {
        void onPhotoViewClick(int adapterPosition);
    }

    private OnPhotoViewClickListener mPhotoViewClickListener;

    public ImageView photo;

    public PhotoViewHolder(View itemView) {
        super(itemView);

        //Cache photo findViewById calls
        photo = (ImageView) itemView.findViewById(R.id.psgi_photo);

        //Set up photo click listener.
        photo.setOnClickListener(this);
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener listener) {
        mPhotoViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        //Pass photo click to photo click listener.
        if(mPhotoViewClickListener != null){
            mPhotoViewClickListener.onPhotoViewClick(getAdapterPosition());
        }
    }
}
