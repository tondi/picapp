package com.example.a4ia2.picapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AlbumsActivity extends AppCompatActivity {

    private ListView listView;
    private FrameLayout createFolder;
    private String array;
    private ArrayAdapter adapter;
    private File dirInner;
    private File mainDir;
    private int STORAGE_PERMISSION_CODE = 23;
    private String[] albums;
//    private File files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_albums);
        listView = (ListView) findViewById(R.id.albumsListView);
        createFolder = (FrameLayout) findViewById(R.id.createFolder);

        File storageFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = "MateuszTondos";
        mainDir = new File(storageFolder, mainFolderName);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(AlbumsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AlbumsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumsActivity.this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(AlbumsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);

            ActivityCompat.requestPermissions(AlbumsActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            showFolders();
        }
//        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //test
                Log.d("TAG", "index = " + i);

//                for (File file : dir.listFiles()){
//                    Log.d("TAG", file.toString());
//                }
                String folderName = albums[i];

                Intent intent = new Intent(AlbumsActivity.this, PicturesActivity.class);
                intent.putExtra("folder", folderName);

                startActivity(intent);
            }
        });

        createFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tutaj input
                final EditText input = new EditText(AlbumsActivity.this);

                final AlertDialog.Builder addFolderDialog = new AlertDialog.Builder(AlbumsActivity.this);

                addFolderDialog.setView(input);

                addFolderDialog.setTitle("New directory");
                addFolderDialog.setMessage("Set name of new directory");

                addFolderDialog.setNeutralButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newFolderName = input.getText().toString();
                        File tmpFolder = new File(mainDir, newFolderName);
                        if(tmpFolder.exists()) {
                            Toast.makeText(AlbumsActivity.this, "Folder already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tmpFolder.mkdir();
                        Toast.makeText(AlbumsActivity.this, "Folder created succesfully", Toast.LENGTH_SHORT).show();
                        showFolders();
                    }
                });
                addFolderDialog.show();
            }
        });


//        albumsList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
////                intent.putExtra("key", "hello");
////
////                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

                showFolders();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void showFolders() {



        File[] filesArr = mainDir.listFiles(); // tablica plików
        List<File> files = new ArrayList<File>();


        int length = 0;

//        files.length
        int i = 0;
        for (File file : filesArr) {
            if(file.isDirectory()) {
                length++;
                files.add(file);
            }
            i++;
        }


        albums = new String[length];

        // log
        i = 0;
        for (File file : files) {
            albums[i] = file.getName();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AlbumsActivity.this,     // Context
                R.layout.verse,     // nazwa pliku xml naszego wiersza
                R.id.txt,         // id pola txt w wierszu
                albums);         // tablica przechowująca dane

        listView.setAdapter(adapter);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        showFolders();
    }
}
