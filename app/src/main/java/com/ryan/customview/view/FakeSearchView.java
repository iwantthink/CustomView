package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by renbo on 2017/8/18.
 */

public class FakeSearchView extends View implements View.OnClickListener {

    public enum SEARCH_STATE {
        NONE, STARTING, SEARCHING, ENDING,
    }

    private SEARCH_STATE mCurrentState = SEARCH_STATE.NONE;
    private PathMeasure mPathMeasure = new PathMeasure();
    private boolean IS_SEARCHING_FINISHED = false;
    private int mDefaultSearchingCount = 2;
    private int mCurrentSearchingCount = 1;

    public void stopSearching() {
        IS_SEARCHING_FINISHED = true;
    }


    public FakeSearchView(Context context) {
        super(context);
    }

    public FakeSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private float[] mPos;
    private float[] mTan;

    private void init() {
        mPos = new float[2];
        mTan = new float[2];
        initPaint();
        initPath();
        setOnClickListener(this);
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    private Path mCirclePath = new Path();
    private Path mSearchPath = new Path();
    private Path mDst = new Path();

    private void initPath() {
        mCirclePath.addArc(-75, -75, 75, 75, 45, 359.9f);
        mSearchPath.addArc(-30, -30, 30, 30, 45, 359.9f);
        mPathMeasure.setPath(mCirclePath, false);
        mPathMeasure.getPosTan(0, mPos, mTan);
        mSearchPath.lineTo(mPos[0], mPos[1]);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case NONE:
                drawSearch(canvas);
                if (mAnimator != null) {
                    mAnimator.end();
                    mCurrentSearchingCount = 1;
                }
                break;
            case STARTING:
                drawStarting(canvas);
                break;
            case SEARCHING:
                drawSearching(canvas);
                break;
            case ENDING:
                drawEnding(canvas);
                break;
        }

    }

    private void drawEnding(Canvas canvas) {
        mDst.reset();
        mPathMeasure.setPath(mSearchPath, false);
        mPathMeasure.getSegment((1 - mFrac) * mPathMeasure.getLength(),
                mPathMeasure.getLength(), mDst, true);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mDst, mPaint);
        canvas.restore();
    }

    private void drawStarting(Canvas canvas) {
        mDst.reset();
        mPathMeasure.setPath(mSearchPath, false);
        mPathMeasure.getSegment(mFrac * mPathMeasure.getLength(),
                mPathMeasure.getLength(), mDst, true);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mDst, mPaint);
        canvas.restore();
    }

    private void drawSearch(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mSearchPath, mPaint);
        canvas.restore();
    }

    private void drawSearching(Canvas canvas) {
        mPathMeasure.setPath(mCirclePath, true);
        float length = mPathMeasure.getLength();
        float startD = length * mFrac;
        float stopD = (float) (startD + (0.5 - Math.abs(0.5 - mFrac)) * 200);
        mDst.reset();
        mPathMeasure.getSegment(startD, stopD, mDst, true);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mDst, mPaint);
        canvas.restore();
    }

    private float mFrac;
    private ValueAnimator mAnimator;

    @Override
    public void onClick(View v) {
        mCurrentState = SEARCH_STATE.STARTING;
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFrac = (float) animation.getAnimatedValue();
                invalidate();

            }
        });
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
                Log.d("FakeSearchView", "mCurrentSearchingCount:" + mCurrentSearchingCount);
                if (mCurrentState == SEARCH_STATE.STARTING) {
                    mCurrentState = SEARCH_STATE.SEARCHING;
                } else if (mCurrentState == SEARCH_STATE.SEARCHING && (IS_SEARCHING_FINISHED
                        || mDefaultSearchingCount <= mCurrentSearchingCount)) {
                    mCurrentState = SEARCH_STATE.ENDING;
                } else if (mCurrentState == SEARCH_STATE.SEARCHING) {
                    mCurrentSearchingCount++;
                } else if (mCurrentState == SEARCH_STATE.ENDING) {
                    mCurrentState = SEARCH_STATE.NONE;
                }
            }
        });
        mAnimator.start();
    }
}
