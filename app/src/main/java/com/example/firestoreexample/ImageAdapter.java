package com.example.firestoreexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.example.firestoreexample.R.*;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Upload> muploads;

    // creating constructor
    public ImageAdapter(Context context,List<Upload> uploads){
        mContext = context;
        muploads = uploads;
    }
    @NonNull
    @Override

    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(layout.image_item,viewGroup,false); // i think i am having problem here
        return new ImageViewHolder(v);

    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCurrent = muploads.get(i);
        imageViewHolder.textViewName.setText(uploadCurrent.getName());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .into(imageViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
         public ImageView imageView;

         public ImageViewHolder(View itemView){
             super(itemView);
             textViewName = itemView.findViewById(id.text_view_name);
             imageView = itemView.findViewById(id.image_view_upload);
         }
    }
}
