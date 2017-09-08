package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by renbo on 2017/8/22.
 */

public class TestView extends BaseView {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, 100);
        } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, MeasureSpec.getSize(heightMeasureSpec));
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 100);
        }
    }


    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initAnim();
        mPaint.setColor(Color.RED);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAnimator && !mAnimator.isRunning()) {
                    mCurrentCollapse = CollapseOrientation.BOTTOM;
                    mAnimator.start();
                }
            }
        });
    }

    private ObjectAnimator mAnimator;
    private int mCurrentCount = 0;

    private void initAnim() {

        mAnimator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
        mAnimator.setInterpolator(new LinearInterpolator());
//        Path path = new Path();
//        path.lineTo(0.125f, 1);
//        path.lineTo(0.25f, 0);
//        path.lineTo(0.375f, 1);
//        path.lineTo(0.5f, 0);
//        path.lineTo(0.625f, 1);
//        path.lineTo(0.75f, 0);
//        path.lineTo(0.875f, 1);
//        path.lineTo(1, 0);
//        path.lineTo(1, 1);
//        mAnimator.setInterpolator(new PathInterpolator(path));
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d("TestView", "mCurrentCount:" + mCurrentCount);
                switch (mCurrentCount) {
                    case 0:
                        mCurrentCollapse = CollapseOrientation.RIGHT;
                        break;
                    case 1:
                        mCurrentCollapse = CollapseOrientation.TOP;
                        break;
                    case 2:
                        mCurrentCollapse = CollapseOrientation.LEFT;
                        break;
                }
                mCurrentCount++;
            }
        });
        mAnimator.setRepeatCount(3);
        mAnimator.setDuration(2000);

    }


    private float mFraction;
    private static final float DEFAULT_ROTATION = 45;
    private Paint mPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.drawColor(Color.BLACK);
        drawNormal(canvas);
//        drawCollapse(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = 100;
        mTop = 100;
        mRight = mWidth - 100;
        mBottom = mHeight / 2;
    }

    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;

    private CollapseOrientation mCurrentCollapse = CollapseOrientation.BOTTOM;

    enum CollapseOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private void drawNormal(Canvas canvas) {
        switch (mCurrentCollapse) {
            case LEFT:
                mLeft = 100;
                mTop = 100;
                mRight = mWidth / 2;
                mBottom = mHeight - 100;
                break;
            case RIGHT:
                mLeft = mWidth / 2;
                mTop = 100;
                mRight = mWidth - 100;
                mBottom = mHeight - 100;
                break;
            case TOP:
                mLeft = 100;
                mTop = 100;
                mRight = mWidth - 100;
                mBottom = mHeight / 2;
                break;
            case BOTTOM:
                mLeft = 100;
                mTop = mHeight / 2;
                mRight = mWidth - 100;
                mBottom = mHeight - 100;
                break;
        }

        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);

    }

    private void drawCollapse(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, mHeight / 2, mWidth, mHeight);
        Camera camera = new Camera();
        camera.setLocation(0, 0, -15);
        canvas.translate(mWidth / 2, mHeight / 2);
        camera.save();
        camera.rotateX(DEFAULT_ROTATION * mFraction);
        camera.applyToCanvas(canvas);
        canvas.translate(-mWidth / 2, -mHeight / 2);
        camera.restore();
        canvas.drawRect(100, mHeight / 2, mWidth - 100, mHeight - 100, mPaint);
        canvas.restore();
    }

}
