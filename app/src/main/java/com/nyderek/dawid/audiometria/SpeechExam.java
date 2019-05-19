package com.nyderek.dawid.audiometria;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SpeechExam extends AppCompatActivity {

    Button startExam;
    Handler handler = new Handler();
    MediaPlayer player;
    AssetFileDescriptor afd;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_exam);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        startExam = (Button) findViewById(R.id.startExamButton);

        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {

                            public void run() {
                                try {
                                    runSpeechExam();
                                } catch (Exception e) {
                                    Toast.makeText(SpeechExam.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });
    }

    void runSpeechExam() {
        try {
            if (player != null) {
                player.release();
                player = null;
            }
            afd = getAssets().openFd("pies.mp3");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(SpeechExam.this, "Badanie zostało przerwane", Toast.LENGTH_LONG).show();
        Intent examChoice = new Intent(getApplicationContext(), ExaminationChoice.class);
        startActivity(examChoice);
    }
}
