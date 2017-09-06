package com.ryan.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by renbo on 2017/9/6.
 */

public class LinearGradientView extends BaseView implements View.OnClickListener {

    public LinearGradientView(Context context) {
        super(context);
    }

    private LinearGradient mLinearGradient;

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint.setTextSize(100);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mCenterX = (fontMetrics.bottom - fontMetrics.top) / 2;
        mTextLength = mTextPaint.measureText(mStr);
        setOnClickListener(this);
    }

    private float mDx, mFrac = 0, mTextLength;
    private float mCenterX;
    private String mStr = "你好自定义view!";


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, mHeight / 2);
        int[] colors = new int[]{Color.BLACK, Color.RED, Color.BLACK};
        float[] pos = new float[]{0, 0.5f, 1f};
        Log.d("LinearGradientView", "mFrac:" + mFrac);
        mLinearGradient = new LinearGradient(-mTextLength + mFrac * mTextLength * 2, mCenterX,
                mFrac * mTextLength * 2, mCenterX
                , colors, pos, Shader.TileMode.CLAMP);
        mTextPaint.setShader(mLinearGradient);
        canvas.drawText(mStr, 0, 0, mTextPaint);
        canvas.drawRect(0, 0, mTextLength, 50, mTextPaint);
    }


    @Override
    public void onClick(View v) {
        ValueAnimator animator = ValueAnimator.ofFloat(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFrac = animation.getAnimatedFraction();
                invalidate();
            }
        });
        animator.setDuration(2000).setRepeatCount(10);
        animator.start();
    }
}
