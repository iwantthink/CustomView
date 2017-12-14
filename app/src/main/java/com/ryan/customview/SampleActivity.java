package com.ryan.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.ryan.customview.progressbar.RyanProgressbar;

import java.util.Random;

public class SampleActivity extends AppCompatActivity {

    AppCompatSeekBar mSeekBar;
    RyanProgressbar mRyanProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.pb_a);
        mRyanProgressbar = (RyanProgressbar) findViewById(R.id.ryan);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("SampleActivity", "progress:" + progress);
                mRyanProgressbar.setProgress(progress);
//                mRyanProgressbar.setTotalProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRyanProgressbar.setIProgressChangedListener(new RyanProgressbar.IProgressChangedListener() {
            @Override
            public void startChanged() {
                Log.d("SampleActivity", "progress change start");
            }

            @Override
            public void endChanged() {
                Log.d("SampleActivity", "progress change end");

            }

            @Override
            public void progressChanged(float progress) {
                Log.d("SampleActivity", "progress = " + progress);

            }
        });
    }

    public void change_width(View view) {
        mRyanProgressbar.setBackgroundColor(Color.YELLOW);
        mRyanProgressbar.setBackgroundWidth(new Random().nextInt(100));
    }

}
