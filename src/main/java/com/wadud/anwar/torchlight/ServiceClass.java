package com.wadud.anwar.torchlight;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Anwar Hussen on 07-Oct-16.
 */
public class ServiceClass extends Service {

    public static final String SERVICE_RUNNING = ServiceClass.class.getName() + ".PREF_SERVICE_RUNNING";
    public static final String ACTION_SERVICE_STOPPED = ServiceClass.class.getName() + ".ACTION_SERVICE_STOPPED";

    private static final String ACTION_STOP_REQUEST = ServiceClass.class.getName() + ".ACTION_STOP_REQUEST";
    private static final int SERVICE_ID = 79290;

    private FlashController mFlashManager = new FlashController();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && ACTION_STOP_REQUEST.equals(intent.getAction())) {
            stopSelfResult(startId);
        }
        mFlashManager.setPower(FlashController.LightPower.ON);
        startForeground(SERVICE_ID, buildRunningNotification());
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(SERVICE_RUNNING, true).commit();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFlashManager.release();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(SERVICE_RUNNING, false).commit();
        sendBroadcast(new Intent(ACTION_SERVICE_STOPPED));
        stopForeground(true);
    }

    private Notification buildRunningNotification() {
        Intent stopService = new Intent(getApplicationContext(), getClass());
        stopService.setAction(ACTION_STOP_REQUEST);
        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, stopService, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("Torch Light");
        builder.setContentText(getResources().getText(R.string.notification));
        builder.setSmallIcon(R.drawable.btn_switch_on);
        builder.setContentIntent(pIntent);
        Notification runningNotification = builder.build();
        return runningNotification;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
