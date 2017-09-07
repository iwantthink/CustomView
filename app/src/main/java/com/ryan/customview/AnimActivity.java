package com.ryan.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.Button;

import com.ryan.customview.view.TestView;

public class AnimActivity extends AppCompatActivity implements View.OnClickListener {

    private TestView mView;
    private Button mBtnA, mBtnB, mBtnC, mBtnD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mView = (TestView) findViewById(R.id.testView);
        mBtnA = (Button) findViewById(R.id.btn_A);
        mBtnB = (Button) findViewById(R.id.btn_B);
        mBtnC = (Button) findViewById(R.id.btn_C);
        mBtnD = (Button) findViewById(R.id.btn_D);
        mBtnA.setOnClickListener(this);
        mBtnB.setOnClickListener(this);
        mBtnC.setOnClickListener(this);
        mBtnD.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_A:
                ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "fraction", 0, 1);
                Path path = new Path();
                path.lineTo(0.125f, 1);
                path.lineTo(0.25f, 0);
                path.lineTo(0.375f, 1);
                path.lineTo(0.5f, 0);
                path.lineTo(0.625f, 1);
                path.lineTo(0.75f, 0);
                path.lineTo(0.875f, 1);
                path.lineTo(1, 0);
                path.lineTo(1, 1);
                animator.setInterpolator(new PathInterpolator(path));
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Log.d("AnimActivity", "animation.getAnimatedValue():" + animation.getAnimatedValue());
                    }
                });
//                animator.setRepeatCount(10);
                animator.setDuration(2000);
                animator.start();
                break;
            case R.id.btn_B:

                break;
            case R.id.btn_C:

                break;
            case R.id.btn_D:

                break;
        }
    }
}
