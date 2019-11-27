package com.example.root.noseapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;


public class OfflineRecorder extends Thread {
    public boolean recording;
    int samplingfrequency;
    public short[] samples;
    public short[] temp;
    int count;
    AudioRecord rec;
    int minbuffersize;
    int duration;
    String filename;
    String dirname;

//    public OfflineRecorder()
//    {
//        samplingfrequency = 44100;
//        count = 0;
//        duration = 10;
//        minbuffersize = AudioRecord.getMinBufferSize(samplingfrequency,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
//        rec = new AudioRecord(MediaRecorder.AudioSource.MIC,samplingfrequency,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,minbuffersize);
//        temp = new short[minbuffersize];
//        samples = new short[duration*samplingfrequency];
//        filename = "Samples.txt";
//    }

    public OfflineRecorder(int samplingfrequency,int duration,int microphone,String filename,String dirname, boolean stereo)
    {
        count = 0;
        this.samplingfrequency = samplingfrequency;
        this.duration = duration;
        this.filename = filename;

        int channels=AudioFormat.CHANNEL_IN_MONO;
        if (stereo) {
            channels=AudioFormat.CHANNEL_IN_STEREO;
        }

        minbuffersize = AudioRecord.getMinBufferSize(samplingfrequency,channels,AudioFormat.ENCODING_PCM_16BIT);

        rec = new AudioRecord(microphone,samplingfrequency,channels,AudioFormat.ENCODING_PCM_16BIT,minbuffersize);

        temp = new short[minbuffersize];
        samples = new short[duration*samplingfrequency];
        this.dirname = dirname;
    }

    public static boolean works(int rate, int channelConfig, int audioFormat) {
        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
            AudioRecord recorder = new AudioRecord(AudioManager.STREAM_VOICE_CALL, rate, channelConfig, audioFormat, bufferSize);
            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                Log.e("hello",">>> works true");
                return true;
            }
        }
        Log.e("hello",">>> works false");
        return false;

    }

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("hello", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
                        Log.e("hello",bufferSize+","+AudioRecord.ERROR_BAD_VALUE);
                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(AudioManager.STREAM_VOICE_CALL, rate, channelConfig, audioFormat, bufferSize);
                            Log.e("hello",recorder.getState()+","+AudioRecord.STATE_INITIALIZED);
                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                Log.e("hello", "worked");
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
//                        Log.e(C.TAG, rate + "Exception, keep trying.",e);
                        Log.e("hello",e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    public void run()
    {
        int bytesread;
        rec.startRecording();
        recording = true;
        while(recording)
        {
            bytesread = rec.read(temp,0,minbuffersize);
            //count = count+bytesread;
    		/*if( count >= samples.length)
    		{
    			rec.stop();
    			rec.release();
    			recording = false;
    		}*/
            for(int i=0;i<bytesread;i++)
            {
                if(count >= samples.length)
                {
                    rec.stop();
                    rec.release();
                    recording = false;
                    FileOperations.writetofile(samples,filename,dirname);
                    break;
                }
                else
                {
                    samples[count]=temp[i];
                    count++;
                }
            }
        }
    }
}
