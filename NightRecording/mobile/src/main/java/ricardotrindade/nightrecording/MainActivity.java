package ricardotrindade.nightrecording;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient googleApiClient;
    Button record;
    boolean recording = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record = (Button) findViewById(R.id.record_btn);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

    }


    public void onDestroy(){
        super.onDestroy();

    }


    public void Record(View view) throws IOException {

        if (!recording) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recording = true;
                    record.setText("Stop");
                    Toast.makeText(MainActivity.this, "Started Recording", Toast.LENGTH_SHORT).show();
                }
            });
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... none) {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "google", "start".getBytes());
                    }
                    return null;
                }
            }.execute();
        } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        record.setText("Start");
                        Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                    }
                });
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... none) {
                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "google", "stop".getBytes());
                        }
                        return null;
                    }
                }.execute();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(MainActivity.this,"Connected to Watch",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MainActivity.this,"Connection Suspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,"Connection failed",Toast.LENGTH_SHORT).show();
    }
}

