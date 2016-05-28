package ricardotrindade.nightrecording;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by ricardotrindade on 28/05/16.
 */
public class MessageReceiverService extends WearableListenerService {

    PowerManager pm;
    PowerManager.WakeLock w1;
    private boolean isHeld = false;
    private boolean isStarted = false;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String v = new String(messageEvent.getData()); //Start ou stop
        if (messageEvent.getPath().equals("google")) {
            if (v.equals("start")) {
                if(!isStarted){
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
                    final int repeat = -1;
                    vibrator.vibrate(vibrationPattern, repeat);
                    releaseWakeLock();
                    isHeld = true;
                    startService(new Intent(this, SensorService.class));
                    isStarted = true;
                }
                else{
                    return;
                }
            }
            if (v.equals("stop")) {
                if(isStarted){
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long[] vibrationPattern = {0, 500, 50, 500, 50, 500, 50};
                    final int repeat = -1;
                    vibrator.vibrate(vibrationPattern, repeat);
                    releaseWakeLock();
                    isHeld = false;
                    isStarted = false;
                    stopService(new Intent(this, SensorService.class));
                }else{
                    return;
                }
            }
        }
    }

    private void releaseWakeLock() {
        if (!isHeld) {
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            w1 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
            w1.acquire();
        } else {
            w1.release();
        }
    }
}

