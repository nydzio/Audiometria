package com.nyderek.dawid.audiometria;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.pow;


public class ToneExam extends AppCompatActivity {

    private Button playTone, startExam, canHear, cannotHear;

    private final int duration = 2; // seconds
    private final int sampleRate = 44100; // Hz
    private final int sampleNum = duration * sampleRate;
    private final double sample[] = new double[sampleNum];
    private int[] freqArray = {250, 500, 1000, 2000, 4000, 6000, 8000}; // Hz
    private int soundLevel; // dB
    private int idx = 2;
    private int counter = 0;
    private int actualFreq = freqArray[idx];
    private boolean higherFreqs = false;
    private boolean firstPhase = false;
    private boolean secondPhase = false;
    private boolean possibleThreshold = false;
    private int tmp;
    private int checkValue = 0;
    private float rightGain = 1.0f;
    private float leftGain = 0.0f;
    private int checkAmplitude;
    ArrayList<Integer> rightEarList = new ArrayList<>();
    ArrayList<Integer> leftEarList = new ArrayList<>();
    private final byte generatedSnd[] = new byte[2 * sampleNum];
    AudioManager am;
    private AudioTrack audioTrack;
    private boolean checkReliability = false;
    Handler handler = new Handler();
    Integer[] rightEar = new Integer[7];
    Integer[] leftEar = new Integer[7];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_exam);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        playTone = (Button) findViewById(R.id.playButton);
        playTone.setEnabled(false);
        startExam = (Button) findViewById(R.id.startExamButton);
        canHear = (Button) findViewById(R.id.canHearButton);
        canHear.setEnabled(false);
        cannotHear = (Button) findViewById(R.id.cannotHearButton);
        cannotHear.setEnabled(false);

        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {

                            public void run() {
                                try {
                                    runExam();
                                } catch (Exception e) {
                                    Toast.makeText(ToneExam.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        canHear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    playMoreQuiet();
                                } catch (Exception e) {
                                    Toast.makeText(ToneExam.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        cannotHear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {

                            public void run() {
                                try {
                                    playLouder();
                                } catch (Exception e) {
                                    Toast.makeText(ToneExam.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        playTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {

                            public void run() {
                                try {
                                    genTone();
                                    playSound();
                                } catch (Exception e) {
                                    Toast.makeText(ToneExam.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });
    }

    void runExam() {
        soundLevel = 40;
        genTone();
        playSound();
        startExam.setEnabled(false);
        startExam.setBackgroundColor(Color.GRAY);
        canHear.setEnabled(true);
        cannotHear.setEnabled(true);
        playTone.setEnabled(true);
    }

    void playMoreQuiet() {
        if (!firstPhase)
            firstPhase = true;
        if (!secondPhase && soundLevel > 0) {
            soundLevel -= 15;
            genTone();
            playSound();
        }
        if (possibleThreshold && soundLevel == tmp && checkValue <= 1 && counter < 2 && soundLevel > 0) {
            counter++;
        }
        if (possibleThreshold && soundLevel != tmp && checkValue <= 1 && counter < 2 && soundLevel > 0) {
            checkValue++;
            if (checkValue <= 1) {
                soundLevel -= 10;
                genTone();
                playSound();
            }
        }
        if (possibleThreshold && soundLevel != tmp && checkValue > 1 && counter < 2 && soundLevel > 0) {
            soundLevel -= 10;
            checkValue = 0;
            counter = 0;
            possibleThreshold = false;
            secondPhase = false;
            genTone();
            playSound();
        }
        if (secondPhase && !possibleThreshold && soundLevel > 0) {
            tmp = soundLevel;
            soundLevel -= 10;
            possibleThreshold = true;
            counter++;
            genTone();
            playSound();
        }
        if (counter == 2 || soundLevel <= 0) {
            if (idx == 2 && leftGain == 0.0f) {
                checkAmplitude = soundLevel;
            }
            changeFrequency();
            checkValue = 0;
            counter = 0;
            soundLevel = 40;
            possibleThreshold = false;
            firstPhase = false;
            secondPhase = false;
            genTone();
            playSound();
        }
    }

    void playLouder() {
        if (!firstPhase && soundLevel < 80) {
            soundLevel += 20;
            genTone();
            playSound();
        } else if (!firstPhase) {
            changeFrequency();
            soundLevel = 40;
            genTone();
            playSound();
        }
        if (firstPhase && !secondPhase)
            secondPhase = true;
        if (secondPhase && soundLevel < 80) {
            soundLevel += 5;
            genTone();
            playSound();
        }
        if (secondPhase && soundLevel >=  80) {
            changeFrequency();
            soundLevel = 40;
            genTone();
            playSound();
        }
    }

    void changeFrequency() {
        if (idx == 2 && leftGain == 0.0f && higherFreqs) {
            if (soundLevel <= checkAmplitude + 5 && soundLevel >= checkAmplitude - 5){
                checkReliability = true;
            }
            else {
                Toast.makeText(ToneExam.this, "Badanie zostało przerwane", Toast.LENGTH_LONG).show();
                Intent examChoice = new Intent(getApplicationContext(), ExaminationChoice.class);
                startActivity(examChoice);
            }
        }
        if (rightGain == 1.0f && leftGain == 0.0f) {
            if (soundLevel <= 0) {
                soundLevel = 0;
            }
            if (!(idx == 2 && checkReliability)) {
                rightEar[idx] = soundLevel;
            }
        }
        else if (rightGain == 0.0f && leftGain == 1.0f) {
            if (soundLevel <= 0) {
                soundLevel = 0;
            }
            leftEar[idx] = soundLevel;
        }
        if (idx == 6 && leftGain == 1.0f) {
            rightEarList.addAll(Arrays.asList(rightEar));
            leftEarList.addAll(Arrays.asList(leftEar));
            Intent examResult = new Intent(getApplicationContext(), ExamResult.class);
            examResult.putExtra("rightEarList", rightEarList);
            examResult.putExtra("leftEarList", leftEarList);
            startActivity(examResult);
        }
        if (idx >= 0 && !higherFreqs) {
            if (idx == 0 && leftGain == 0.0f) {
                idx = 2;
                actualFreq = freqArray[idx];
                higherFreqs = true;
            }
            if (idx == 0 && leftGain == 1.0f) {
                idx = 3;
                actualFreq = freqArray[idx];
                higherFreqs = true;
            }
            if (!higherFreqs) {
                actualFreq = freqArray[--idx];
            }
        }
        if (idx == 6 && leftGain == 0.0f) {
            leftGain = 1.0f;
            rightGain = 0.0f;
            idx = 2;
            actualFreq = freqArray[idx];
            higherFreqs = false;
            firstPhase = false;
            secondPhase = false;
            checkReliability = false;
        }
        if (idx < 6 && higherFreqs && checkReliability) {
            actualFreq = freqArray[++idx];
        }
        if (leftGain == 1.0f && idx == 3) {
            checkReliability = true;
        }
    }

    void genTone() {
        for (int i = 0; i < sampleNum; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / actualFreq));
        }

        int idx = 0;

        int ramp = sampleNum / 20 ;

        for (int i = 0; i < ramp; ++i) {
            double dVal = sample[i];
            final short val = (short) ((dVal * (pow(10, soundLevel / 20) * i/ramp)));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = ramp; i < sampleNum - ramp; ++i) {
            double dVal = sample[i];
            final short val = (short) ((dVal * (pow(10, soundLevel / 20))));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = sampleNum - ramp; i < sampleNum; ++i) {
            double dVal = sample[i];
            final short val = (short) ((dVal * (pow(10, soundLevel / 20) * (sampleNum - i) / ramp)));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    void playSound() {
        try {
            if (audioTrack != null) {
                audioTrack.release();
                audioTrack = null;
            }
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length * 2, AudioTrack.MODE_STATIC);
            audioTrack.setStereoVolume(leftGain, rightGain);
            audioTrack.write(generatedSnd, 0, generatedSnd.length);
            audioTrack.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ToneExam.this, "Badanie zostało przerwane", Toast.LENGTH_LONG).show();
        Intent examChoice = new Intent(getApplicationContext(), ExaminationChoice.class);
        startActivity(examChoice);
    }
}