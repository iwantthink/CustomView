package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by renbo on 2017/9/7.
 */

public class RadialGradientView extends BaseView {
    public RadialGradientView(Context context) {
        super(context);
    }

    public RadialGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setShader(DEFAULT_RADIUS);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mValueAnimator = ValueAnimator.ofFloat(DEFAULT_RADIUS, w);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (float) animation.getAnimatedValue();
                setShader(mRadius);
                invalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setShader(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setShader(float radius) {
        if (radius > 0) {
            mGradient = new RadialGradient(mTouchX, mTouchY, radius, 0x00FFFFFF, 0xFFcccccc,
                    Shader.TileMode.CLAMP);
            mPaint.setShader(mGradient);
        } else {
            mRadius = 0;
            invalidate();
        }

    }

    private ValueAnimator mValueAnimator;
    private RadialGradient mGradient;
    private float mTouchX, mTouchY, mRadius = DEFAULT_RADIUS;
    private static final float DEFAULT_RADIUS = 50;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = event.getX();
        mTouchY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mValueAnimator != null && mValueAnimator.isRunning()) {
                mValueAnimator.end();
            }
            mRadius = DEFAULT_RADIUS;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mValueAnimator != null && !mValueAnimator.isRunning()) {
                mValueAnimator.start();
            }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mTouchX, mTouchY, mRadius, mPaint);
    }
}
