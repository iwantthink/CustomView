package com.ryan.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class SampleActivity extends AppCompatActivity {

    AppCompatSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mSeekBar = findViewById(R.id.pb_a);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("SampleActivity", "progress:" + progress);
//                mRyanProgressbar.setTotalProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void change_width(View view) {

        mSeekBar.setRotation(mSeekBar.getRotationX()+45);

//        mRyanProgressbar.setBackgroundProgressColor(Color.YELLOW);
//        mRyanProgressbar.setForegroundProgressColor(Color.GREEN);
//        mRyanProgressbar.setForegroundCap(Paint.Cap.BUTT);
//        mRyanProgressbar.setPercentTextColor(Color.GRAY);
//        mRyanProgressbar.setPercentTextSize(30);



    }

}
