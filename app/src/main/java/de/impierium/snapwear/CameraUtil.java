package de.impierium.snapwear;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by tomek on 15.06.14.
 */
public class CameraUtil {

    public static int findCamera(int cameraFacing) {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraFacing) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

}
