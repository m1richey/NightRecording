package ricardotrindade.nightrecording;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {


    private BoxInsetLayout mContainerView;
    PowerManager pm;
    PowerManager.WakeLock w1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        w1 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Tag");
        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    public void Start(View view){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50,500,50,500,50};
        final int repeat = -1;
        vibrator.vibrate(vibrationPattern, repeat);
        w1.acquire();
        startService(new Intent(this,SensorService.class));
    }

    public void Stop(View view){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50,500,50,500,50};
        final int repeat = -1;
        vibrator.vibrate(vibrationPattern, repeat);
        w1.release();
        stopService(new Intent(this,SensorService.class));
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
}
