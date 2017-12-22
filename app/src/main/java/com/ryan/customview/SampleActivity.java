package com.ryan.customview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ryan.customview.progressbar.SquareView;

public class SampleActivity extends AppCompatActivity {

    AppCompatSeekBar mSeekBar;
    SquareView mSquareView;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mLinearLayout = findViewById(R.id.ll_content);
        mSquareView = findViewById(R.id.spb);
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

    ValueAnimator mValueAnimator;
    private float mRotation;
    private final float DEFAULT_ROTATION = 90;

    public void change_width(View view) {
        mLinearLayout.addView(new SquareView(SampleActivity.this));
//        startRoll();
//        mSeekBar.setRotation(mSeekBar.getRotationX()+45);


//        mRyanProgressbar.setBackgroundProgressColor(Color.YELLOW);
//        mRyanProgressbar.setForegroundProgressColor(Color.GREEN);
//        mRyanProgressbar.setForegroundCap(Paint.Cap.BUTT);
//        mRyanProgressbar.setPercentTextColor(Color.GRAY);
//        mRyanProgressbar.setPercentTextSize(30);


    }

    public void startRoll() {
        if (null != mValueAnimator && mValueAnimator.isRunning()) {
            return;
        }
        mValueAnimator = ValueAnimator.ofFloat(1);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mRotation = DEFAULT_ROTATION * fraction;

                mSquareView.setRotation(mRotation);

//                mSquareProgressbar.setRotationY(mRotation);

            }
        });
        mValueAnimator.start();
    }

}
