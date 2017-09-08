package com.ryan.customview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ryan.customview.view.CollapseView;

public class AnimActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapseView mView;
    private Button mBtnA, mBtnB, mBtnC, mBtnD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mView = (CollapseView) findViewById(R.id.collapse_view);
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
