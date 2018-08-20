package com.acquaint.customgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.acquaint.customgallery.Fragments.FourFragment;
import com.acquaint.customgallery.Fragments.OneFragment;
import com.acquaint.customgallery.Fragments.ThreeFragment;
import com.acquaint.customgallery.Fragments.TwoFragment;
import com.acquaint.customgallery.R;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {
    public static int selectionTitle;
    public static String title;
    public static int maxSelection;
    public static int mode;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResult();
            }
        });

        title=getIntent().getExtras().getString("title");
        maxSelection=getIntent().getExtras().getInt("maxSelection");
        if (maxSelection==0) maxSelection = Integer.MAX_VALUE;
        mode=getIntent().getExtras().getInt("mode");
        setTitle(title);
        selectionTitle=0;

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        OpenGallery.selected.clear();
        OpenGallery.imagesSelected.clear();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(selectionTitle>0){
            setTitle(String.valueOf(selectionTitle));
        }
    }

    //This method set up the tab view for images and videos
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(mode==1 || mode==2 || mode==3) {
            adapter.addFragment(new OneFragment(), "Images");
        }
        if(mode==1 ||mode==4 || mode==3){
            adapter.addFragment(new TwoFragment(), "Videos");
        }
        if(mode==1 || mode==5){
            adapter.addFragment(new ThreeFragment(),"Pdf");
        }
        if(mode==1 || mode==6){
            adapter.addFragment(new FourFragment(),"Audio");
        }
        viewPager.setAdapter(adapter);
    }

    private void returnResult() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("result",OpenGallery.imagesSelected);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
