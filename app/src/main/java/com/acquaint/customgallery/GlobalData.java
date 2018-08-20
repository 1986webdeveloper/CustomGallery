package com.acquaint.customgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by acquaint on 15/8/18.
 */

public class GlobalData {
    public static Bitmap generateImageFromPdf(Uri pdfUri, Context context, ImageView thumbnail) {
        int pageNumber = 0;

        Bitmap bitmap=null;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {

            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);

            saveImage(bmp,context,thumbnail);


            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(Exception e) {

            e.printStackTrace();

        }
        return bitmap;

    }
    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
    public static void saveImage(Bitmap bmp,Context context,ImageView thumbnail) {
        FileOutputStream out = null;
        File file=null;
        try {
            File folder = new File(FOLDER);
            if(!folder.exists())
                folder.mkdirs();
            file=File.createTempFile("pdf","png",folder);

            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            Uri uri = Uri.fromFile(file);
            Glide.with(context).load(uri)
                    .apply(new RequestOptions().override(300, 300).centerCrop().skipMemoryCache(true).error(android.R.drawable.stat_notify_error))

                    .into(thumbnail);
        } catch (Exception e) {
            //todo with exception
            Log.i("Exception","Pdf"+e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }

    }
    public static void getThumbnailforAudio(Context mContext,Uri uri,ImageView thumbnail){
try{
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    byte[] rawArt;
    Bitmap art=null;
    BitmapFactory.Options bfo=new BitmapFactory.Options();
    mmr.setDataSource(mContext, uri);
    rawArt = mmr.getEmbeddedPicture();
    if (null != rawArt){
        art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        thumbnail.setImageBitmap(art);
    }
    else {
        thumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_music));
    }
}catch (RuntimeException e){
    e.printStackTrace();
}

    }
}
