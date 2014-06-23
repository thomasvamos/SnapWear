package de.impierium.snapwear;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by tomek on 15.06.14.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {


    public CameraPreview(Context context) {
        super(context);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
