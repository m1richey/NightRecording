package ricardotrindade.nightrecording;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ricardo on 04/02/2016.
 */
public class SensorService extends Service implements SensorEventListener {
    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;


    private SensorManager mSensorManager;
    private ArrayList<Float> ax = new ArrayList<>();
    private ArrayList<Float> ay = new ArrayList<>();
    private ArrayList<Float> az = new ArrayList<>();
    /*private ArrayList<Float> gx = new ArrayList<>();
    private ArrayList<Float> gy = new ArrayList<>();
    private ArrayList<Float> gz = new ArrayList<>();*/
    private ArrayList<Long> ta = new ArrayList<>();
   // private ArrayList<Long> tg = new ArrayList<>();


    public void onCreate(){
        super.onCreate();
        startMeasurement();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMeasurement();
    }

    private void startMeasurement() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        Log.d("lister", "listeners");
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(SENS_ACCELEROMETER);

        // Register the listener
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, accelerometerSensor,1000000);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == SENS_ACCELEROMETER){
            ax.add(event.values[0]);
            ay.add(event.values[1]);
            az.add(event.values[2]);
            ta.add(System.currentTimeMillis());
        }

    }

    public void write(){
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sensor_data/" + timeStamp + ".csv");
        try{
            FileWriter fw = new FileWriter(file);
            BufferedWriter buffWriter = new BufferedWriter(fw,50*1024);
            for(int i=0;i<ax.size();i++){
                buffWriter.write(String.valueOf(ax.get(i)));
                buffWriter.write(",");
                buffWriter.write(String.valueOf(ay.get(i)));
                buffWriter.write(",");
                buffWriter.write(String.valueOf(az.get(i)));
                buffWriter.write(",");
                buffWriter.write(String.valueOf(ta.get(i)));
                buffWriter.write("\n");
            }
            buffWriter.flush();
            buffWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void stopMeasurement() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
        write();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
