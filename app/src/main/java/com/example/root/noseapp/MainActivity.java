package com.example.root.noseapp;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    TextView tv1;
    Button b1;
    EditText et1;
    EditText et2;
    EditText startText;
    EditText endText;
    Spinner spinner;
    int bottomspeaker = AudioManager.STREAM_SYSTEM;
    String filePrefix;
    String filename;
    private int requestCode;
    private int grantResults[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv1 = (TextView) findViewById(R.id.textView);
        b1 = (Button) findViewById(R.id.button);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        startText = (EditText) findViewById(R.id.startText);
        endText = (EditText) findViewById(R.id.endText);
        spinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"chirp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},requestCode);
        onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},grantResults);
    }

    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "granted");
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    onDestroy();
                }
                return;
            }
        }
    }

    public void onClick(View v) {

        try {
            filePrefix = et1.getText().toString()+"-";
            pulsetest(bottomspeaker);
        } catch (Exception e) {

        }
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    AudioSpeaker sp1;
    String time;

    public void pulsetest(final int speakerType) {
        new SendChirp(this).execute(speakerType);
        time = getTime();
        filename = "pulse-" + filePrefix + time;
        tv1.setText(filename);
    }

    private class SendChirp extends AsyncTask<Integer,Void,Void> {

        Context context;
        public SendChirp(Context context) {
            this.context=context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Button b1 = (Button) findViewById(R.id.button);
            b1.setEnabled(false);
        }

        protected void onPostExecute (Void result) {
            super.onPostExecute(result);
            Button b1 = (Button) findViewById(R.id.button);
            b1.setEnabled(true);
        }

        public Void doInBackground(Integer... params) {
            int speakerType=params[0];

            int samplingFreq = 48000;

            int count = 5;
            double gapduration = Double.parseDouble(et2.getText().toString());
            int gaplength = (int) (samplingFreq * gapduration);

            int recordDurationInSeconds = 5;
            int initialSleepInSeconds=2;

            short[] pulse;
            if (spinner.getSelectedItem().toString().equals("pulse")) {
                int startfreq = 156;
                int endfreq = 10000;

                double chirpdurationInMilliSeconds = 0.2;
                double chirpdurationInSeconds = chirpdurationInMilliSeconds/1000;
                int chirplength = (int) (samplingFreq * chirpdurationInSeconds);

                pulse = SignalGenerator.continuouspulse(
                        chirplength, startfreq, endfreq, samplingFreq, gaplength, count);
            }
            else if (spinner.getSelectedItem().toString().equals("gpulse")) {
                double chirpdurationInMilliSeconds = 1;
                double chirpdurationInSeconds = chirpdurationInMilliSeconds/1000;

                pulse = SignalGenerator.continuousgpulse(chirpdurationInSeconds/2, 10000, .2, samplingFreq, gaplength, count);
            }
            else if (spinner.getSelectedItem().toString().equals("chirp")) {
                pulse = SignalGenerator.continuouspulse(
                        (int)(samplingFreq*0.15), Integer.parseInt(startText.getText().toString()),
                        Integer.parseInt(endText.getText().toString()), samplingFreq, 0, 1);
            }
            else {
                pulse = new short[1];
            }

//            String ss=Arrays.toString(pulse);

//            FileOperations.writetofile(pulse,"input-"+time+".txt", "data");

            sp1 = new AudioSpeaker(context, pulse, samplingFreq, speakerType);

//            int micType;
//            //        do this is regular record
//            if (speakerType == bottomspeaker) {
//                micType = MediaRecorder.AudioSource.DEFAULT;
//            } else {
//                //        do this if headphone record
//                micType = MediaRecorder.AudioSource.CAMCORDER;
//            }

//            boolean stereo = false;
//            OfflineRecorder rec = new OfflineRecorder(samplingFreq, initialSleepInSeconds+recordDurationInSeconds,
//                    micType, filename + ".txt", "data", stereo);

            try {
//                rec.start();

                Thread.sleep(initialSleepInSeconds*1000);

                sp1.play(1.0);

                Thread.sleep(recordDurationInSeconds * 1000);

                sp1 = null;
            } catch (Exception e) {

            }

            return null;
        }
    }

    public String getTime() {
        return (System.currentTimeMillis() / 1000L)+"";
    }
}
