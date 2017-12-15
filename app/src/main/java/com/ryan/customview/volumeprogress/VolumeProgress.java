package com.ryan.customview.volumeprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by renbo on 2017/12/15.
 */

public class VolumeProgress extends View {

    private Context mContext;
    private float mWidth, mHeight;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public VolumeProgress(Context context) {
        super(context);
        init(context);
    }

    ValueAnimator mValueAnimator;

    private void init(Context context) {
        mContext = context;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        aaaaa.setStyle(Paint.Style.STROKE);
    }

    public VolumeProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(mContext, 300), dp2px(mContext, 300));
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(mContext, 300), heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, dp2px(mContext, 300));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mControlX = mWidth / 2;
        mControlY = mHeight / 2;
        mBesselLeftX = mWidth / 2;
        mBesselLeftY = mHeight / 2 + mBowLength / 2;
        mBesselRightX = mWidth / 2;
        mBesselRightY = mHeight / 2 - mBowLength / 2;
        initPath();
    }

    private Path mPath = new Path();
    private Path mBowPath = new Path();

    private float mControlX, mControlY;
    private float mBesselLeftX, mBesselRightX, mBesselLeftY, mBesselRightY;
    private float mBowLength = 200; //弓长度

    private void initPath() {
        mPath.reset();
//        mPath.moveTo(mBesselLeftX, mBesselLeftY);
//        mPath.quadTo(mControlX, mControlY, mBesselRightX, mBesselRightY);

        mBowPath.reset();
//        mBowPath.addArc(mBesselLeftX,mBesselLeftY,mBesselRightX,mBesselRightY,);
    }

    private Paint aaaaa = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawPath(mPath, mPaint);

//        canvas.drawLine(mControlX, mControlY, mWidth / 2, mHeight / 2, mPaint);
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, aaaaa);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, aaaaa);

        canvas.save();

        canvas.drawCircle(mControlX, mControlY, 10, mPaint);
        canvas.drawLine(mBesselLeftX, mBesselLeftY,
                mBesselRightX, mBesselRightY, mPaint);
        canvas.drawLine(mControlX, mControlY, mWidth / 2, mHeight / 2, aaaaa);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    private float mStartControlX, mStartControlY;

    private float mGapX;
    private float mGapY;

    private boolean mIsBacking = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsBacking) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > mWidth / 2 || event.getY() < mHeight / 2) {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:

                if (Math.sqrt((event.getX() - mWidth / 2) * (event.getX() - mWidth / 2) +
                        (event.getY() - mHeight / 2) * (event.getY() - mHeight / 2)) > mBowLength / 2) {

                } else {
                    mControlX = event.getX();
                    mControlY = event.getY();
                }


                if (mControlX > mWidth / 2) {
                    mControlX = mWidth / 2;
                }
                if (mControlY < mHeight / 2) {
                    mControlY = mHeight / 2;
                }

                float xDis = mWidth / 2 - mControlX;
                float yDis = mControlY - mHeight / 2;

                float degree = (float) Math.toDegrees(Math.atan(yDis / xDis));
                double atan = Math.atan(yDis / xDis);

                mBesselLeftX = (float) (mWidth / 2 - 50 - Math.sin(atan) * mBowLength / 2);
                mBesselLeftY = (float) (mHeight / 2 + 50 - Math.cos(atan) * mBowLength / 2);
                mBesselRightX = (float) (mWidth / 2 - 50 + Math.sin(atan) * mBowLength / 2);
                mBesselRightY = (float) (mHeight / 2 + 50 + Math.cos(atan) * mBowLength / 2);
                initPath();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                mGapX = mControlX - mStartControlX;
//                mGapY = mControlY - mStartControlY;
//
//                mValueAnimator = ValueAnimator.ofFloat(1, 0);
//                mValueAnimator.setDuration(1000);
//                mValueAnimator.setInterpolator(new OvershootInterpolator());
//                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
////                        float value = (float) valueAnimator.getAnimatedValue();
////                        Log.d("VolumeProgress", "value:" + value);
//                        float fraction = valueAnimator.getAnimatedFraction();
//                        mControlX = mStartControlX + mGapX * (1 - fraction);
//                        mControlY = mStartControlY + mGapY * (1 - fraction);
//                        initPath();
//                        invalidate();
//                        Log.d("VolumeProgress", "fraction:" + fraction);
//                    }
//                });
//                mValueAnimator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//                        mIsBacking = true;
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        mIsBacking = false;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//                        mIsBacking = false;
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//                    }
//                });
//                mValueAnimator.start();
                break;
        }

        return true;
    }

    public static int dp2px(Context context, int dpVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }

    public static int sp2px(Context context, int spVal) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

}
