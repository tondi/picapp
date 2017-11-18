package com.example.a4ia2.picapp;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private LinearLayout cameraLayout;
    private LinearLayout albumsLayout;
    public static String photosPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init
        String[] baseSubFolders = new String[]{ "places", "people", "things" };
        File storageFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = "MateuszTondos";
        File mainDir = new File(storageFolder, mainFolderName);

        // create
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }

        if(mainDir.isDirectory()) {
            for (String folderName : baseSubFolders) {
                File tmpFolder = new File(mainDir, folderName);
                if (!tmpFolder.exists()) {
                    tmpFolder.mkdir();
                }
            }
        }

        photosPath = mainDir.toString();

        cameraLayout = (LinearLayout) findViewById(R.id.cameraLayout);
        albumsLayout = (LinearLayout) findViewById(R.id.albumsLayout);

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,CameraActivity.class);
            intent.putExtra("key", "hello");

            startActivity(intent);
            }
        });

        albumsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AlbumsActivity.class);
                intent.putExtra("key", "hello");

                startActivity(intent);
            }
        });
    }
    // bt
}

