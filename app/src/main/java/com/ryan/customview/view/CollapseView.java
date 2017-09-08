package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by renbo on 2017/9/8.
 */

public class CollapseView extends BaseView {
    public CollapseView(Context context) {
        super(context);
    }

    public CollapseView(Context context, AttributeSet attrs) {
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
        mPaint.setShader(new LinearGradient(0, 0, 500, 500, Color.BLUE, Color.YELLOW, Shader.TileMode.REPEAT));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAnimator && !mAnimator.isRunning()) {
                    mCurrentCollapse = CollapseOrientation.BOTTOM;
                    mCurrentCount = 0;
                    mAnimator.start();
                }
            }
        });
    }

    private ObjectAnimator mAnimator;
    private int mCurrentCount = 0;
    private static final float DEFAULT_ROTATION = 45;

    private void initAnim() {

        mAnimator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFraction = 0;
                mCurrentCollapse = CollapseOrientation.BOTTOM;
                mLastCollapse = CollapseOrientation.BOTTOM;
                postInvalidate();
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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.drawColor(Color.BLACK);
        drawNormal(canvas);
        switch (mCurrentCollapse) {
            //画右边 last is top
            case LEFT:
                mLastCollapse = CollapseOrientation.TOP;
                //draw left
                drawCollapse(canvas, mWidth / 2, mWidth, 0, mHeight,
                        mWidth / 2, mWidth - 100, 100, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                //draw top
                drawCollapse(canvas, 0, mWidth, mHeight / 2, mHeight,
                        100, mWidth - 100, mHeight / 2, mHeight - 100,
                        DEFAULT_ROTATION * (1 - mFraction), mLastCollapse);
                break;
            //画底部
            case TOP:
                mLastCollapse = CollapseOrientation.RIGHT;
                //draw top
                drawCollapse(canvas, 0, mWidth, mHeight / 2, mHeight,
                        100, mWidth - 100, mHeight / 2, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                //draw right
                drawCollapse(canvas, 0, mWidth / 2, 0, mHeight,
                        100, mWidth / 2, 100, mHeight - 100,
                        DEFAULT_ROTATION * (1 - mFraction), mLastCollapse);
                break;
            //画顶部
            case BOTTOM:
                mLastCollapse = CollapseOrientation.LEFT;
                //draw bottom
                drawCollapse(canvas, 0, mWidth, 0, mHeight / 2,
                        100, mWidth - 100, 100, mHeight / 2,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                //draw left
                drawCollapse(canvas, mWidth / 2, mWidth, 0, mHeight,
                        mWidth / 2, mWidth - 100, 100, mHeight - 100,
                        DEFAULT_ROTATION * (1 - mFraction), mLastCollapse);

                break;
            //画左边
            case RIGHT:
                mLastCollapse = CollapseOrientation.BOTTOM;
                //draw right
                drawCollapse(canvas, 0, mWidth / 2, 0, mHeight,
                        100, mWidth / 2, 100, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                //draw bottom
                drawCollapse(canvas, 0, mWidth, 0, mHeight / 2,
                        100, mWidth - 100, 100, mHeight / 2,
                        DEFAULT_ROTATION * (1 - mFraction), mLastCollapse);
                break;
        }

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
    private CollapseOrientation mLastCollapse = CollapseOrientation.BOTTOM;

    enum CollapseOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private void drawNormal(Canvas canvas) {
        switch (mCurrentCollapse) {
            case LEFT:
                mLeft = 100;
                mTop = 100;
                mRight = mWidth / 2;
                mBottom = mHeight / 2;
                break;
            case RIGHT:
                mLeft = mWidth / 2;
                mTop = mHeight / 2;
                mRight = mWidth - 100;
                mBottom = mHeight - 100;
                break;
            case TOP:
                mLeft = mWidth / 2;
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

    private void drawCollapse(Canvas canvas,
                              float clipLeft, float clipRight, float clipTop, float clipBottom,
                              float drawLeft, float drawRight, float drawTop, float drawBottom,
                              float rotateAngle, CollapseOrientation collapse) {
        canvas.save();
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        Camera camera = new Camera();
        camera.setLocation(0, 0, -12);
        canvas.translate(mWidth / 2, mHeight / 2);
        camera.save();
        switch (collapse) {
            case LEFT:
//                camera.rotateY(mFraction < 0.5f ? -DEFAULT_ROTATION * mFraction : -DEFAULT_ROTATION * (1 - mFraction));
                camera.rotateY(-rotateAngle);
                break;
            case TOP:
//                camera.rotateX(mFraction < 0.5f ? DEFAULT_ROTATION * mFraction : DEFAULT_ROTATION * (1 - mFraction));
                camera.rotateX(rotateAngle);
                break;
            case RIGHT:
//                camera.rotateY(mFraction < 0.5f ? DEFAULT_ROTATION * mFraction : DEFAULT_ROTATION * (1 - mFraction));
                camera.rotateY(rotateAngle);
                break;
            case BOTTOM:
//                camera.rotateX(mFraction < 0.5f ? -DEFAULT_ROTATION * mFraction : -DEFAULT_ROTATION * (1 - mFraction));
                camera.rotateX(-rotateAngle);
                break;
        }

        camera.applyToCanvas(canvas);
        canvas.translate(-mWidth / 2, -mHeight / 2);
        camera.restore();
        canvas.drawRect(drawLeft, drawTop, drawRight, drawBottom, mPaint);
        canvas.restore();
    }

    private float mFraction;

    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float fraction) {
        mFraction = fraction;
        postInvalidate();
    }
}
