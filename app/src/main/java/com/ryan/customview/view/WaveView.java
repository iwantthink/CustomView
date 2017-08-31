package com.ryan.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by renbo on 2017/8/31.
 */

public class WaveView extends BaseView implements View.OnClickListener {

    private int mWaveLength;
    private int mCenterY;
    private int mWaveCount;
    private int offset;

    private Path mPath;


    private ValueAnimator mValueAnimatior;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.LTGRAY);
        mWaveLength = 800;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 需要计算出屏幕能容纳多少个波形
        mPath = new Path();
        mCenterY = h / 2;
        mWaveCount = (int) Math.round(w / mWaveLength + 1.5); // 计算波形的个数
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(-mWaveLength, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + offset, mCenterY + 60, -mWaveLength / 2 + i * mWaveLength + offset, mCenterY);
            mPath.quadTo(-mWaveLength / 4 + i * mWaveLength + offset, mCenterY - 60, i * mWaveLength + offset, mCenterY);
        }
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }


    @Override
    public void onClick(View v) {
        mValueAnimatior = ValueAnimator.ofInt(0, mWaveLength);
        mValueAnimatior.setDuration(1000);
        mValueAnimatior.setInterpolator(new LinearInterpolator());
        mValueAnimatior.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatior.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimatior.start();

    }
}

