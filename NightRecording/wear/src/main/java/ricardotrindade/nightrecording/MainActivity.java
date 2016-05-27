package ricardotrindade.nightrecording;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity implements MessageApi.MessageListener {


    private BoxInsetLayout mContainerView;
    PowerManager pm;
    PowerManager.WakeLock w1;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        w1 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    public void Start(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
        final int repeat = -1;
        vibrator.vibrate(vibrationPattern, repeat);
        w1.acquire();
        startService(new Intent(this, SensorService.class));
    }

    public void Stop(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
        final int repeat = -1;
        vibrator.vibrate(vibrationPattern, repeat);
        w1.release();
        stopService(new Intent(this, SensorService.class));
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));

        } else {
            mContainerView.setBackground(null);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("google")) {
            String v = new String(messageEvent.getData()); //Start ou stop
            if (v.equals("start")) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
                final int repeat = -1;
                vibrator.vibrate(vibrationPattern, repeat);
                w1.acquire();
                startService(new Intent(this, SensorService.class));
            }
            if (v.equals("stop")) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
                final int repeat = -1;
                vibrator.vibrate(vibrationPattern, repeat);
                w1.release();
                stopService(new Intent(this, SensorService.class));
            }
        }
    }

}
