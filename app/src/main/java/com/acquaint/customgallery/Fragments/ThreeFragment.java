package com.acquaint.customgallery.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.acquaint.customgallery.Activities.OpenGallery;
import com.acquaint.customgallery.Adapters.BucketsAdapter;
import com.acquaint.customgallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {
    public static List<String> videosList=new ArrayList<>();
    public static List<String> namesList=new ArrayList<>();
    public static List<Boolean> selected=new ArrayList<>();
    private static RecyclerView recyclerView;
    private final String[] projection = new String[]{ MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA };
    private final String[] projection2 = new String[]{MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA };
    private BucketsAdapter mAdapter;
    private List<String> bucketNames= new ArrayList<>();
    private List<String> bitmapList=new ArrayList<>();


    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bucket names reloaded
        bucketNames.clear();
        bitmapList.clear();
        videosList.clear();
        namesList.clear();
        getVideoBuckets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        populateRecyclerView();
        return view;
    }
    private void populateRecyclerView() {
        mAdapter = new BucketsAdapter(bucketNames,bitmapList,getContext(),true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                getVideos(bucketNames.get(position));
                Intent intent=new Intent(getContext(), OpenGallery.class);
                intent.putExtra("FROM","pdf");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter.notifyDataSetChanged();


    }


    public void getVideos(String bucket){
        selected.clear();
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Files.getContentUri("external"), projection2,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?"+" AND " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" ,new String[]{ bucket,mimeType }, MediaStore.Files.FileColumns.DATE_ADDED);

        ArrayList<String> imagesTEMP = new ArrayList<>(cursor.getCount());
        ArrayList<String> namesTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String name =cursor.getString(cursor.getColumnIndex(projection2[0]));
                String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
                file = new File(path);
                if (file.exists() && !albumSet.contains(path)) {
                    namesTEMP.add(name);
                    imagesTEMP.add(path);
                    albumSet.add(path);
                    selected.add(false);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (imagesTEMP == null) {

            imagesTEMP = new ArrayList<>();
        }
        if (namesTEMP == null) {

            namesTEMP = new ArrayList<>();
        }
        namesList.clear();
        namesList.addAll(namesTEMP);
        videosList.clear();
        videosList.addAll(imagesTEMP);
    }
    public void getVideoBuckets(){
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Files.getContentUri("external"), projection,
                        MediaStore.Files.FileColumns.MIME_TYPE+" =?",new String[]{ mimeType }, MediaStore.Files.FileColumns.DATE_ADDED);

        ArrayList<String> bucketNamesTEMP = new ArrayList<>(cursor.getCount());
        ArrayList<String> bitmapListTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                String image = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(image);
                if (file.exists() && !albumSet.contains(album)) {
                    bucketNamesTEMP.add(album);
                    bitmapListTEMP.add(image);
                    albumSet.add(album);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (bucketNamesTEMP == null) {
            bucketNames = new ArrayList<>();
        }
        bucketNames.clear();
        bitmapList.clear();
        bucketNames.addAll(bucketNamesTEMP);
        bitmapList.addAll(bitmapListTEMP);
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
