package com.example.a4ia2.picapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO Later after folder handling

public class PicturesActivity extends AppCompatActivity {

    private LinearLayout.LayoutParams lparams;
    private File currentDirectory;
    private List<File> pictures;
    private LinearLayout childLayout;
    private LinearLayout imagesLayout;
    private FrameLayout removeButton;
    private TextView textMessage;
    private Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_pictures);

        //CustomImageView civ = new CustomImageView(PicturesActivity.this, 0xff00ff00);
        Bundle bundle = getIntent().getExtras();
        currentDirectory = new File(MainActivity.photosPath + "/" +  bundle.getString("folder").toString() + "/");

        imagesLayout = (LinearLayout) findViewById(R.id.imagesLayout);

        removeButton = (FrameLayout) findViewById(R.id.removeFolder);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(File file : currentDirectory.listFiles()) {
                    file.delete();
                }
//                Intent intent = new Intent(PicturesActivity.this,AlbumsActivity.class);
//                intent.putExtra("key", "hello");

//                startActivity(intent);
                AlertDialog.Builder alert = new AlertDialog.Builder(PicturesActivity.this);
                alert.setTitle("Confirm");
                alert.setMessage("Remove folder?");
//ok
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String alertDeletedFolder = "deleted";
                        Toast.makeText(getApplicationContext(), alertDeletedFolder, Toast.LENGTH_SHORT).show();

                        currentDirectory.delete();
                        PicturesActivity.this.finish();
                    }

                });

//no
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //wyswietl which
                    }
                });

                alert.show();


            }
        });


        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        updateImagesList();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        updateImagesList();
    }

    private Bitmap betterImageDecode(String filePath) {

        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 1; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private void updateImagesList() {

        // in case of restart
        imagesLayout.removeAllViews();

        lparams = new LinearLayout.LayoutParams(300, 300);


//        anyView.setLayoutParams(lparams);

//        File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
//        File dir = new File(pic, "tondos");
//        File dirInner = new File(dir, "ludzie");
        File[] files = currentDirectory.listFiles(); // tablica plików
//        int length = files.length;
//
        pictures = new ArrayList<>();
//

        for (File file : currentDirectory.listFiles()) {
            if (file.isFile()) {
                pictures.add(file);
            }
        }

        int listSize = pictures.size();

        if (listSize == 0) {
            textMessage = (TextView) findViewById(R.id.textMessage);
            textMessage.setText("No pictures in this folder");
        }

        int baseHeight = size.y / 5;
        int small = size.x / 3;
        int big = (size.x / 3) * 2;
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, baseHeight);
        childLayout = new LinearLayout(getApplicationContext());
        childLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLayout.setLayoutParams(lparams);

        int picturesCount = 1;
        // for properly image structure
        Boolean leftIsSmaller = true;
        for (File file : pictures) {
            Bitmap betterImage = betterImageDecode(file.getAbsolutePath());
            CustomImageView civ = new CustomImageView(PicturesActivity.this, file);

            if (betterImage.getWidth() >= betterImage.getHeight()){

                // works somehow
                betterImage = Bitmap.createBitmap(
                        betterImage,
                        betterImage.getWidth()/2 - betterImage.getHeight()/2,
                        0,
                        betterImage.getHeight(),
                        betterImage.getHeight()
                );

            }else{

                betterImage = Bitmap.createBitmap(
                        betterImage,
                        0,
                        betterImage.getHeight()/2 - betterImage.getWidth()/2,
                        betterImage.getWidth(),
                        betterImage.getWidth()
                );
            }

            if (leftIsSmaller) {
                //betterImage = Bitmap.createScaledBitmap(betterImage, small, betterImage.getHeight(), true);
                civ.setLayoutParams(new LinearLayout.LayoutParams(small, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            } else {
//                betterImage = Bitmap.createScaledBitmap(betterImage, big, betterImage.getHeight(), true);
                civ.setLayoutParams(new LinearLayout.LayoutParams(big, LinearLayout.LayoutParams.MATCH_PARENT, 2));
            }
            civ.setImageBitmap(betterImage);
            civ.setId(picturesCount - 1);
            leftIsSmaller = !leftIsSmaller;
            if (listSize == picturesCount && picturesCount % 2 != 0) {
                civ.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                childLayout.addView(civ);
                imagesLayout.addView(childLayout);
                break;
            }
            childLayout.addView(civ);
            if (picturesCount % 2 == 0) {
                leftIsSmaller = !leftIsSmaller;
                imagesLayout.addView(childLayout);

                // create new layout to prevent bug
                childLayout = new LinearLayout(getApplicationContext());
                childLayout.setOrientation(LinearLayout.HORIZONTAL);
                childLayout.setLayoutParams(lparams);
            }
            picturesCount++;
        }
    }
}
