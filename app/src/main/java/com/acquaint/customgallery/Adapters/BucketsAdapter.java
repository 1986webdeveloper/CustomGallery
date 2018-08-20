package com.acquaint.customgallery.Adapters;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquaint.customgallery.BuildConfig;
import com.acquaint.customgallery.GlobalData;
import com.acquaint.customgallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.MyViewHolder> {
    Boolean pdf=false;
    int mode=0;
    private List<String> bucketNames, bitmapList;
    private Context context;

    public BucketsAdapter(List<String> bucketNames, List<String> bitmapList, Context context) {
        this.bucketNames = bucketNames;
        this.bitmapList = bitmapList;
        this.context = context;
    }
    public BucketsAdapter(List<String> bucketNames, List<String> bitmapList, Context context, Boolean pdf) {
        this.bucketNames = bucketNames;
        this.bitmapList = bitmapList;
        this.context = context;
        this.pdf=pdf;
    }
    public BucketsAdapter(List<String> bucketNames, List<String> bitmapList, Context context, int mode) {
        this.bucketNames = bucketNames;
        this.bitmapList = bitmapList;
        this.context = context;
        this.mode=mode;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.title.setText(bucketNames.get(position));
        if(pdf){
            holder.thumbnail.setVisibility(View.VISIBLE);
            File file = new File(bitmapList.get(position));
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
            GlobalData.generateImageFromPdf(uri,context,holder.thumbnail);


        }
        else if(mode==1){
            holder.thumbnail.setVisibility(View.VISIBLE);
            File file = new File(bitmapList.get(position));
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
         if(uri!=null){
             GlobalData.getThumbnailforAudio(context,uri,holder.thumbnail);
         }


        }
        else {

            holder.thumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(bitmapList.get(position))
                    .apply(new RequestOptions().override(300, 300).centerCrop().error(android.R.drawable.stat_notify_error))
                    .into(holder.thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return bucketNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public LinearLayout ll_pdf;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.image);
        }
    }

}

