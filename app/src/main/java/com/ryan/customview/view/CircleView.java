package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by renbo on 2017/8/17.
 */

public class CircleView extends View implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener {

    private static final float C = 0.551915024494f;
    private float mRadius = 100;
    private ValueAnimator mAnimator;
    private float mTranslationX = 5.0f;
    private long mAnimDuration = 1000;

    ArrayList<PointF> mPoints = new ArrayList<>();
    PointF[] mPointFs = null;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    {
        setOnClickListener(this);
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();

    private void init() {

        initPoint();

        mPaint.setStyle(Paint.Style.STROKE);
        calculatePath();

        mAnimator = ValueAnimator.ofFloat(1);
        mAnimator.addUpdateListener(this);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(mAnimDuration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initPoint();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void initPoint() {
        mPoints.clear();
        mPoints.add(new PointF(0, 1 * mRadius));
        mPoints.add(new PointF(C * mRadius, 1 * mRadius));
        mPoints.add(new PointF(1 * mRadius, C * mRadius));
        mPoints.add(new PointF(1 * mRadius, 0));

        mPoints.add(new PointF(1 * mRadius, -C * mRadius));
        mPoints.add(new PointF(C * mRadius, -1 * mRadius));
        mPoints.add(new PointF(0, -1 * mRadius));

        mPoints.add(new PointF(-C * mRadius, -1 * mRadius));
        mPoints.add(new PointF(-1 * mRadius, -C * mRadius));
        mPoints.add(new PointF(-1 * mRadius, 0));

        mPoints.add(new PointF(-1 * mRadius, C * mRadius));
        mPoints.add(new PointF(-C * mRadius, 1 * mRadius));
        mPointFs = mPoints.toArray(new PointF[mPoints.size()]);
    }

    private void calculatePath() {
        mPath.reset();
        mPath.moveTo(mPointFs[0].x, mPointFs[0].y);
        mPath.cubicTo(mPointFs[1].x, mPointFs[1].y,
                mPointFs[2].x, mPointFs[2].y,
                mPointFs[3].x, mPointFs[3].y);
        mPath.cubicTo(mPointFs[4].x, mPointFs[4].y,
                mPointFs[5].x, mPointFs[5].y,
                mPointFs[6].x, mPointFs[6].y);
        mPath.cubicTo(mPointFs[7].x, mPointFs[7].y,
                mPointFs[8].x, mPointFs[8].y,
                mPointFs[9].x, mPointFs[9].y);

        mPath.cubicTo(mPointFs[10].x, mPointFs[10].y,
                mPointFs[11].x, mPointFs[11].y,
                mPointFs[0].x, mPointFs[0].y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinateSystem(canvas);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        drawPoint(canvas);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

    }

    private void drawPoint(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        canvas.drawPoint(mPointFs[0].x, mPointFs[0].y, mPaint);
        canvas.drawPoint(mPointFs[1].x, mPointFs[1].y, mPaint);
        canvas.drawPoint(mPointFs[2].x, mPointFs[2].y, mPaint);
        canvas.drawPoint(mPointFs[3].x, mPointFs[3].y, mPaint);
        canvas.drawPoint(mPointFs[4].x, mPointFs[4].y, mPaint);
        canvas.drawPoint(mPointFs[5].x, mPointFs[5].y, mPaint);
        canvas.drawPoint(mPointFs[6].x, mPointFs[6].y, mPaint);
        canvas.drawPoint(mPointFs[7].x, mPointFs[7].y, mPaint);
        canvas.drawPoint(mPointFs[8].x, mPointFs[8].y, mPaint);
        canvas.drawPoint(mPointFs[9].x, mPointFs[9].y, mPaint);
        canvas.drawPoint(mPointFs[10].x, mPointFs[10].y, mPaint);
        canvas.drawPoint(mPointFs[11].x, mPointFs[11].y, mPaint);
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawCoordinateSystem(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
    }

    @Override
    public void onClick(View v) {
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();

        if (fraction < 0.2f) {
            move1();
        } else if (fraction < 0.4f) {
            move2();
        } else if (fraction < 0.6f) {
            move3();
        } else if (fraction < 0.8f) {
            move1();
            move2();
            move3();
            move4();
            move5();
        } else if (fraction < 1.0f) {
        }

        calculatePath();
        postInvalidate();
    }

    private void move5() {
        mPointFs[8].x += mTranslationX;
        mPointFs[9].x += mTranslationX;
        mPointFs[10].x += mTranslationX;
    }

    private void move4() {
        mPointFs[11].x += mTranslationX;
        mPointFs[7].x += mTranslationX;
    }

    private void move3() {
        mPointFs[0].x += mTranslationX;
        mPointFs[6].x += mTranslationX;
    }

    private void move2() {
        mPointFs[1].x += mTranslationX;
        mPointFs[5].x += mTranslationX;
    }

    private void move1() {
        mPointFs[2].x += mTranslationX;
        mPointFs[3].x += mTranslationX;
        mPointFs[4].x += mTranslationX;
    }
}
