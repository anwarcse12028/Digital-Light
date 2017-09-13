package com.wadud.anwar.torchlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by Anwar Hussen on 07-Oct-16.
 */
public class FlashController {
    public enum LightPower {
        OFF, ON;
    }

    private Camera camera;
    private Camera.Parameters parameters;
    private LightPower currentPower;

    public static boolean isFlashAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void release() {
        setPower(LightPower.OFF);
    }

    private void turnFlashON() {
        camera = Camera.open();
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        currentPower = LightPower.ON;
    }

    private void turnFlashOFF() {
        camera.stopPreview();
        camera.release();
        currentPower = LightPower.OFF;
    }

    public void setPower(LightPower value) {
        switch (value) {
            case ON:
                if (getPower() == LightPower.OFF) {
                    turnFlashON();
                }
                break;
            case OFF:
                if (getPower() == LightPower.ON) {
                    turnFlashOFF();
                }
                break;
            default:
                break;
        }
    }

    public LightPower getPower() {
        if (currentPower == null) {
            return LightPower.OFF;
        }
        return currentPower;
    }
}
