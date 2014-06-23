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
    protected void onHandleIntent(Intent intent) {
//        if(SnapWearActivity.mCamera != null) {
//            SnapWearActivity.mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
//            Log.d(TAG, "Picture taken");
//        }
    }

}
