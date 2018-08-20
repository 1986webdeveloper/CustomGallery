package com.acquaint.customgallery.Adapters;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import com.acquaint.customgallery.BuildConfig;
import com.acquaint.customgallery.GlobalData;
import com.acquaint.customgallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.io.File;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder>{
    Boolean pdf=false;
    int mode=0;
    private List<String> bitmapList;
    private List<String> mediaNameList;
    private List<Boolean> selected;
    private Context context;

    public MediaAdapter(List<String> bitmapList, List<Boolean> selected, Context context, int mode, List<String> audioNameList) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.selected=selected;
        this.mode=mode;
        this.mediaNameList=audioNameList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.tv_name.setText(mediaNameList.get(position));
        Log.e("Name","medialist"+mediaNameList.get(position));
        if(mode==3){
            File file = new File(bitmapList.get(position));
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
            GlobalData.generateImageFromPdf(uri,context,holder.thumbnail);
        }else if(mode==4){
            File file = new File(bitmapList.get(position));
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
            if(uri!=null){
                GlobalData.getThumbnailforAudio(context,uri,holder.thumbnail);
            }

        }
        else {
            Glide.with(context).load(bitmapList.get(position))
                    .apply(new RequestOptions().override(300, 300).centerCrop().skipMemoryCache(true).error(android.R.drawable.stat_notify_error))

                    .into(holder.thumbnail);
        }

        if(selected.get(position).equals(true)){
            holder.check.setVisibility(View.VISIBLE);
            holder.check.setAlpha(50);
            holder.check.bringToFront();
        }else{
            holder.check.setVisibility(View.GONE);
        }

    }

    @Override
   public int getItemCount() {
        return bitmapList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail,check;
        public TextView tv_name;


        public MyViewHolder(View view) {
            super(view);
            thumbnail= view.findViewById(R.id.image);
            check= view.findViewById(R.id.image2);
            tv_name=view.findViewById(R.id.tv_name);

        }
    }

}

