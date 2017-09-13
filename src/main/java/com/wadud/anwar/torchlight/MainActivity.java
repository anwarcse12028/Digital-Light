package com.wadud.anwar.torchlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private boolean lightON = false;
    private boolean noFlash = false;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ServiceClass.ACTION_SERVICE_STOPPED.equals(intent.getAction())) {
                imageButton.setBackgroundResource(R.drawable.btn_switch_off);
                lightON = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"Developed by M A H Wadud",Toast.LENGTH_LONG).show();
        if (!FlashController.isFlashAvailable(getApplicationContext())) {
            noFlash = true;
        }
        imageButton = (ImageButton) findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightAction();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
        updateBackground();
        registerReceiver(broadcastReceiver, new IntentFilter(ServiceClass.ACTION_SERVICE_STOPPED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void lightAction() {
        if (lightON) {
            stopService(new Intent(getApplicationContext(), ServiceClass.class));
            lightON = false;
        } else {
            if (noFlash) {
                Toast.makeText(getApplicationContext(), R.string.no_flash, Toast.LENGTH_LONG).show();
                return;
            }
            startService(new Intent(getApplicationContext(), ServiceClass.class));
            lightON = true;
        }
        updateBackground();
    }

    private void updateStatus() {
        lightON = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(ServiceClass.SERVICE_RUNNING, false);
    }

    private void updateBackground() {
        if (lightON) {

            imageButton.setBackgroundResource(R.drawable.btn_switch_on);

        } else {
            imageButton.setBackgroundResource(R.drawable.btn_switch_off);
        }
    }
}
