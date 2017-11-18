package com.example.a4ia2.picapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout _frameLayout;
    private int CAMERA_PERMISSION_CODE = 24;
    private ImageView takePicture;
    private ImageView savePicture;
    private byte[] fdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.camera_activity);

        Bundle bundle = getIntent().getExtras();
        String wartosc = bundle.getString("key").toString();

        Toast.makeText(CameraActivity.this,
                wartosc,
                Toast.LENGTH_SHORT
        ).show();
        Log.wtf("XXX", "create camera");

        initCamera();
        takePicture = (ImageView) findViewById(R.id.takePicture);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tutaj input
                Log.wtf("XXX", "take picture");

                camera.takePicture(null, null, camPictureCallback);
            }
        });


        savePicture = (ImageView) findViewById(R.id.savePicture);

        savePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Gdzie zapisać?");
                    //nie może mieć setMessage!!!
                //final String[] opcje = new String[]{ "places", "people", "things" };

                File storageFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String mainFolderName = "MateuszTondos";
                File mainDir = new File(storageFolder, mainFolderName);

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


                String[] albums = new String[length];

                // log
                i = 0;
                for (File file : files) {
                    albums[i] = file.getName();
                    i++;
                }

                final String[] opcje = albums;

                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // wyswietl opcje[which]);
                        Log.wtf("folder", opcje[which]);

                        savePicture(opcje[which]);
                    }
                });
//
                alert.show();
            }
        });

    }

    private void initCamera() {
        boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        if (!cam) {
            // uwaga - brak kamery

        } else {

            // ask for perimssion

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
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

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                CameraActivity.this.finish();

            } else {
                runCamera();
            }


        }
    }

    private int getCameraId () {
        int cid = 0;
        int camerasCount = Camera.getNumberOfCameras(); // gdy więcej niż jedna kamera

        for (int i = 0; i < camerasCount; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cid = i;
            }

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
               cid = i;
            }

        }

        return cid;
    }

    private void initPreview() {
        _cameraPreview = new CameraPreview(CameraActivity.this, camera);
        _frameLayout = (FrameLayout) findViewById(R.id.cameraFrame);
        _frameLayout.addView(_cameraPreview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == CAMERA_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you operate camera", Toast.LENGTH_LONG).show();

                runCamera();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void runCamera() {
        // wykorzystanie danych zwróconych przez kolejną funkcję getCameraId()

        cameraId = getCameraId();
        // jest jakaś kamera!
        if (cameraId < 0) {
            // brak kamery z przodu!
        } else {
            camera = Camera.open(cameraId);
            initPreview();
        }
    }

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            // zapisz dane zdjęcia w tablicy typu byte[]
            // do poźniejszego wykorzystania
            // ponieważ zapis zdjęcia w galerii powinien być dopiero po akceptacji butonem

            fdata = data;

            // odswież (lub nie) kamerę (zapobiega to przycięciu się kamery po zrobieniu zdjęcia)

            camera.startPreview();
        }
    };

    private void savePicture (String folder) {

        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String d = dFormat.format(new Date());

        Log.wtf("Sciezka: ", d);


        FileOutputStream fs = null;
        try {

            File storageFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                String mainFolderName = "MateuszTondos";
//                File mainDir = new File(storageFolder, mainFolderName);

            fs = new FileOutputStream(storageFolder + "/MateuszTondos/" + folder + "/" + d + ".jpg");
            fs.write(fdata);
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf("XXX", "pause camera");

        // jeśli nie zwolnimy (release) kamery, inna aplikacje nie może jej używać

        if (camera != null) {
            camera.stopPreview();
            //linijka nieudokumentowana w API, bez niej jest crash przy wznawiamiu kamery
            // TODO Fix crash
            _cameraPreview.getHolder().removeCallback(_cameraPreview);
            camera.release();
            camera = null;
        }

    }
//
    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("XXX", "resume camera");

        if (camera == null) {
            initCamera();
            //zainicjalizuj kamerę od nowa
            // czyli uruchom funkcje initCamera() i initPreview()
        }
    }



//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.wtf("XXX", "Restart camera");
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.wtf("XXX", "stop camera");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.wtf("XXX", "start camera");
//    }


}
