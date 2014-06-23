package de.impierium.snapwear;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class SnapWearActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "SnapWearActivity";
    private Button takePictureButton;
    private boolean takePicture = false;
    private boolean surfaceValid = false;
    private int mWidth = 1;
    private int mHeight = 1;


    public SurfaceView mSurfaceView;
    public SurfaceHolder mSurfaceHolder;
    public Camera mCamera;

    private static final String ACTION_TAKE_PHOTO = "de.impierium.snapwear.TAKE_PHOTO";

    private View.OnClickListener takePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SnapWearActivity.this, "Take picture", Toast.LENGTH_LONG).show();
            createNotification();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(this.ACTION_TAKE_PHOTO)) {
            takePicture = true;
        }
    }

    private PictureTakenCallback pictureTakenCallback = new PictureTakenCallback() {

        @Override
        public void onPictureTaken() {
            configureCamera(mWidth, mHeight);
            mCamera.startPreview();
        }
    };

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean b, Camera camera) {

            mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext(), pictureTakenCallback));
        }
    };

    private void takePhoto() {
        if (mCamera != null) {
            mCamera.autoFocus(autoFocusCallback);
            Log.d(TAG, "Picture taken");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //request fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set layout
        setContentView(R.layout.activity_snap_wear);

        //register listeners
        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(takePictureClickListener);
    }

    private void initializeCamera() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e(TAG, "No camera on this device");
            return;
        } else {
            int cameraId = CameraUtil.findCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            if (cameraId < 0) {
                Log.e(TAG, "No front facing camera found.");
                return;
            } else {
                mCamera = Camera.open(cameraId);
                mCamera.startPreview();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSurfaceView = (SurfaceView) findViewById(R.id.previewSurface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            initializeCamera();
            if(surfaceValid) {
                configureCamera(mWidth, mHeight);
            }
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            Log.e(TAG, "Couldnt create preview.");
        }

        createNotification();

        if(takePicture) {
            takePicture = false;
            takePhoto();
        }
    }

    @Override
    protected void onPause() {
        NotificationManagerCompat.from(this).cancel(0);
        mCamera.stopPreview();
        mCamera.release();
        super.onPause();
    }

    private void createNotification() {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(this, SnapWearActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        // Build intent for additional action: take picture with service
        Intent takePictureIntent = new Intent(this, SnapWearActivity.class);
        takePictureIntent.setAction(SnapWearActivity.ACTION_TAKE_PHOTO);
        PendingIntent takePicturePendingIntent =
                PendingIntent.getActivity(this, 0, takePictureIntent, 0);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_ausdruck)
                        .setContentTitle("SnapWear")
                        .setContentText("Cheeese!")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.ic_action_camera, "Take Picture", takePicturePendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        surfaceValid = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        mWidth = width;
        mHeight = height;
        configureCamera(mWidth, mHeight);

    }

    private void configureCamera(int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.e(TAG, "Failed to stop preview.");
        }

        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation() == Surface.ROTATION_0) {
            parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(90);
        }

        if (display.getRotation() == Surface.ROTATION_90) {
            parameters.setPreviewSize(width, height);
        }

        if (display.getRotation() == Surface.ROTATION_180) {
            parameters.setPreviewSize(height, width);
        }

        if (display.getRotation() == Surface.ROTATION_270) {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        mCamera.setParameters(parameters);


        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceValid = false;
    }
}
