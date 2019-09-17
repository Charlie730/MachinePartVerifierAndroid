package com.humminbird.machinepartverifierandroid.Activities.Camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.humminbird.machinepartverifierandroid.R;
import com.humminbird.machinepartverifierandroid.Utilities.DateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    public static Camera mCamera;
    public static CameraPreview mPreview;
    private static int MY_PERMISSIONS_REQUEST_CAMERA = 5;
    public static boolean isSuccessful;
    public static float face_centerX = 0;
    public static float face_centerY = 0;
    public static boolean newFace;
    public static int camera = 1;
    private boolean shouldReturn = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        shouldReturn = getIntent().getStringExtra("part")!= null;



        // Create an instance of Camera
        mCamera = getCameraInstance(this,this,0);


        // set Camera parameters
        Camera.Parameters params = null;
        if (mCamera != null) {
            params = mCamera.getParameters();
        }

        FrameLayout layout = findViewById(R.id.camera_preview);

        if (params != null && params.getMaxNumMeteringAreas() > 0) { // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(0, 0, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(new Rect(0, 0, 50, 50), 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            params.setMeteringAreas(meteringAreas);

        }




        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this);
        final FrameLayout preview = findViewById(R.id.camera_preview);
        mPreview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        preview.addView(mPreview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
            getWindow().setNavigationBarColor(getColor(R.color.colorPrimaryDark));
        }


        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null) {
                    try{
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {

                            }
                        });
                    } catch (RuntimeException x){
                        x.printStackTrace();
                        Toast.makeText(CameraActivity.this, "Failed to focus", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCamera = getCameraInstance(CameraActivity.this,CameraActivity.this,1);
                    mPreview = new CameraPreview(CameraActivity.this);
                    preview.removeAllViews();
                    preview.addView(mPreview);
                }
            }
        });




        // Add a listener to the Capture button
        CardView captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mCamera!= null){
                            // get an image from the camera
// Here, thisActivity is the current activity
                            if (ContextCompat.checkSelfPermission(CameraActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // Permission is not granted
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(CameraActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_CAMERA);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.



                            } else {
                                // Permission has already been granted
                                try{

                                    mCamera.takePicture(null, null, mPicture);
                                } catch (RuntimeException ex){
                                    ex.printStackTrace();
                                    Toast.makeText(CameraActivity.this, "Failed to take a picture. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            finish();
                        }
                    }
                }
        );

        CardView swit = findViewById(R.id.button_switch);
        final Camera.Parameters finalParams = params;
        swit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera!=null){
                    if(camera<1){
                        //camera is 0
                        camera = 1;

                        preview.removeAllViews();
                        releaseCamera();
                        mCamera = getCameraInstance(CameraActivity.this,CameraActivity.this,camera);
                        mPreview = new CameraPreview(CameraActivity.this);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(5,5);
                        lp.leftMargin = (int) resampleWidthToGetX(face_centerX);
                        lp.topMargin = (int) resampleHeightToGetY(face_centerY);
                        //mPreview.setLayoutParams(lp);
                        if (finalParams != null) {
                            finalParams.setPreviewSize(getWindow().getWindowManager().getDefaultDisplay().getWidth(),getWindow().getWindowManager().getDefaultDisplay().getHeight());
                        }
                        preview.addView(mPreview);
                    } else {
                        //camera = 1
                        camera = 0;
                        preview.removeAllViews();
                        releaseCamera();
                        mCamera = getCameraInstance(CameraActivity.this,CameraActivity.this,camera);
                        mPreview = new CameraPreview(CameraActivity.this);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(5,5);
                        lp.leftMargin = (int) resampleWidthToGetX(face_centerX);
                        lp.topMargin = (int) resampleHeightToGetY(face_centerY);
                        //mPreview.setLayoutParams(lp);
                        preview.addView(mPreview);
                    }
                }
            }
        });




        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mCamera!= null){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(newFace){
                        // a new face has been added to the array. let's get it
                        //final ImageView filter = findViewById(R.id.FilterView);
                        //final ImageView speck = findViewById(R.id.speck);
                        //final float x = face_centerX.get(face_centerX.size()-1);
                        //final float y = face_centerY.get(face_centerY.size()-1);
                        if(face_centerX != 0 && face_centerY!= 0) {

                            //ObjectAnimator animX = ObjectAnimator.ofFloat(filter,"x",x);
                            //ObjectAnimator animY = ObjectAnimator.ofFloat(filter,"y",y);
                            //AnimatorSet animSetXY = new AnimatorSet();
                            //animSetXY.playTogether(animX,animY);
                            //animSetXY.start();

                            //ObjectAnimator animXspeck = ObjectAnimator.ofFloat(speck,"x",resampleWidthToGetX(face_centerX));
                            //ObjectAnimator animYspeck = ObjectAnimator.ofFloat(speck,"y",resampleHeightToGetY(face_centerY));
                            //AnimatorSet animSetXYspeck = new AnimatorSet();
                            //animSetXYspeck.playTogether(animXspeck,animYspeck);
                            //animSetXYspeck.start();

                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(5,5);
                            lp.leftMargin = (int) resampleWidthToGetX(face_centerX);
                            lp.topMargin = (int) resampleHeightToGetY(face_centerY);
                            //filter.setLayoutParams(lp);
                        }
                        newFace = false;
                    }
                }
            }
        }).start();

    }

    private float resampleWidthToGetX(float cameraSampleWidth){
        final FrameLayout preview = findViewById(R.id.camera_preview);
        float previewWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        float max = 2000;
        float x = 0;

        //here's the formula
        x = (cameraSampleWidth * previewWidth)/max; //This is the proportion method

        if(x < 0){
            //The converted number is negative
            x = -x;
        }

        return x*2;
    }

    private float resampleHeightToGetY(float cameraSampleHeight){
        float previewHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        float max = 2000;
        float x = 0;

        //here's the formula
        x = (cameraSampleHeight * previewHeight)/max; //This is the proportion method

        if(x < 0){
            //The converted number is negative
            x = -x;
        }

        Log.w("Resampler","camera gave me: "+cameraSampleHeight+", and I returned "+x);

        return x;
    }

    private void releaseMediaRecorder(){
        //if (mMediaRecorder != null) {
            //mMediaRecorder.reset();   // clear recorder configuration
            //mMediaRecorder.release(); // release the recorder object
            //mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        //}
    }

    private void restartThis(){
        finish();
        Intent in = new Intent(this, CameraActivity.class);
        startActivity(in);
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopFaceDetection();
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Toast.makeText(CameraActivity.this, "Saving photo", Toast.LENGTH_SHORT).show();
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){

                Log.e(this.toString(), "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream os = new FileOutputStream(pictureFile);
                os.write(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap image = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
            FirebaseVisionBarcodeDetectorOptions options =
                    new FirebaseVisionBarcodeDetectorOptions.Builder()
                            .setBarcodeFormats(
                                    FirebaseVisionBarcode.FORMAT_QR_CODE,
                                    FirebaseVisionBarcode.FORMAT_AZTEC)
                            .build();
            FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(image);
            FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector();

            Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(img)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            // Task completed successfully
                            // ...
                            for (FirebaseVisionBarcode barcode: barcodes) {
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();

                                String rawValue = barcode.getRawValue();
                                Toast.makeText(CameraActivity.this, "Value: "+rawValue, Toast.LENGTH_SHORT).show();


                                if(shouldReturn){
                                    Intent result = new Intent("This will never be heard.");
                                    result.putExtra("result",rawValue);
                                    result.putExtra("part",getIntent().getStringExtra("part"));
                                    setResult(Activity.RESULT_OK, result);
                                    finish();
                                }

                                int valueType = barcode.getValueType();
                                // See API reference for complete list of supported types
                                switch (valueType) {
                                    case FirebaseVisionBarcode.TYPE_WIFI:
                                        String ssid = barcode.getWifi().getSsid();
                                        String password = barcode.getWifi().getPassword();
                                        int type = barcode.getWifi().getEncryptionType();
                                        break;
                                    case FirebaseVisionBarcode.TYPE_URL:
                                        String title = barcode.getUrl().getTitle();
                                        String url = barcode.getUrl().getUrl();
                                        break;
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });

            //restartThis();
        }
    };



    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "MPVACam");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }



        // Create a media file name
        String timeStamp = DateUtils.getTimestamp();
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }




    private boolean checkCameraHardware(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // this device has a camera
                return true;
            } else {
                // no camera on this device
                return false;
            }
        } else {
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(Context context, Activity activity, int camera){
        Camera c = null;

// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            return null;

        } else {
            // Permission has already been granted
            try {
                if(camera < 1){
                    c = Camera.open();
                } else {
                    c = Camera.open(camera); // attempt to get a Camera instance
                }

            } catch (Error e) {
                // Camera is not available (in use or does not exist)
                Toast.makeText(context, "Message: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Num of cams = "+ Camera.getNumberOfCameras(), Toast.LENGTH_SHORT).show();
                Log.e(context.toString(),"The camera could not be accessed. (CameraActivity 217:6)");
            } catch (RuntimeException d){
                Toast.makeText(context, d.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Loading camera "+camera, Toast.LENGTH_SHORT).show();
            }
        }
        return c; // returns null if camera is unavailable
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // camera-related task you need to do.
                    mCamera = getCameraInstance(this,this,1);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    finish();
                    Toast.makeText(this, "Camera permission declined.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}