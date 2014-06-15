package de.impierium.snapwear;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by tomek on 14.06.14.
 */
public class SnapWearService extends IntentService {

    private static final String TAG = "SNAPWEARSERVICE";
    private Camera mCamera;
    private MediaRecorder mPreview;

    public SnapWearService() {
        super("SnapWearService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e(TAG, "No camera on this device");
            return;
        } else {
            int cameraId = findCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            if (cameraId < 0) {
                Log.e(TAG, "No front facing camera found.");
                return;
            } else {
                mCamera = Camera.open(cameraId);

            }
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mCamera.startPreview();
        mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
        Log.d(TAG, "Picture taken");
    }

    private int findCamera(int cameraFacing) {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraFacing) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

}
