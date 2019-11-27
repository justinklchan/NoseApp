package com.example.root.noseapp;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioSpeaker extends Thread {

    AudioTrack track1;
    int SamplingFreq;
    Context mycontext;
    short[] samples;
    int speakerType;
    AudioManager man;

    public AudioSpeaker(Context mycontext,short[] samples,int samplingFreq, int speakerType)
    {
        this.mycontext = mycontext;
        man = (AudioManager)mycontext.getSystemService(Context.AUDIO_SERVICE);
        SamplingFreq = samplingFreq;
        this.samples = samples;
        this.speakerType = speakerType;
        track1 = new AudioTrack(speakerType,SamplingFreq,AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,samples.length*2,AudioTrack.MODE_STATIC);
        track1.write(samples,0,samples.length);
    }

//    for S6 volume is .01, 1 unit of volume
//    for S7 volume is .02, 1 unit of volume
//    for pixel use .01 of volume, 1 unit of volume
//    for s9 volume is .03, 1 unit of volume (ringtone)

    public void play(double vol)
    {
        try
        {
            track1.setLoopPoints(0,samples.length,-1);
//            setVolume(1);
            track1.setVolume((float)vol);
//            track1.setStereoVolume((float) .01, (float) .01);
            track1.play();
        }catch(Exception e)
        {

        }
    }

    public void run()
    {
        track1.setLoopPoints(0,samples.length,-1);
        track1.play();
    }

    public void pause()
    {
        track1.pause();
    }
}
