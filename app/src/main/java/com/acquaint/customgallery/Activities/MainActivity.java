package com.acquaint.customgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.acquaint.customgallery.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button tv_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        initListeners();

    }

    private void initListeners() {
        tv_click.setOnClickListener(this);
    }

    private void initWidget() {
        tv_click=findViewById(R.id.tv_click);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_click) {
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            // Set the title
            intent.putExtra("title", "Select media");
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode", 1);
            intent.putExtra("maxSelection", 10); // Optional
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK){

            ArrayList<String> selectionResult1 = data.getStringArrayListExtra("result");
            for(int i = 0;i<selectionResult1.size();i++){
                Log.e("MainActivity","data"+selectionResult1.get(i));
            }

            //handle filepath of the selected files

        }

    }
}
