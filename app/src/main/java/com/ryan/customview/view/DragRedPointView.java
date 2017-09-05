package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by renbo on 2017/9/5.
 */

public class DragRedPointView extends BaseView {

    public DragRedPointView(Context context) {
        super(context);
    }

    public DragRedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPointA = new PointF(300, 300);
        mPointB = new PointF(800, 800);
        mRadiusA = 50;
        mRadiusB = 200;
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mDegreeA = Math.atan((mPointB.y - mPointA.y) / (mPointB.x - mPointA.x));

        mA0 = new PointF((float) (mPointA.x + Math.sin(Math.toRadians(mDegreeA)) * mRadiusA),
                (float) (mPointA.y - Math.cos(Math.toRadians(mDegreeA)) * mRadiusA));
        mB0 = new PointF((float) (mPointB.x + Math.sin(Math.toRadians(45)) * mRadiusB),
                (float) (mPointB.y - Math.cos(Math.toRadians(45)) * mRadiusB));

        mA1 = new PointF((float) (mPointA.x - Math.sin(Math.toRadians(mDegreeA)) * mRadiusA),
                (float) (mPointA.y + Math.cos(Math.toRadians(mDegreeA)) * mRadiusA));
        mB1 = new PointF((float) (mPointB.x - Math.sin(Math.toRadians(45)) * mRadiusB),
                (float) (mPointB.y + Math.cos(Math.toRadians(45)) * mRadiusB));


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mPointB.x = event.getX();
        mPointB.y = event.getY();

        mDegreeA = Math.atan((mPointB.y - mPointA.y) / (mPointB.x - mPointA.x));
        Log.d("DragRedPointView", "mDegreeA:" + mDegreeA);

        mA0.x = (float) (mPointA.x + Math.sin(mDegreeA) * mRadiusA);
        mA0.y = (float) (mPointA.y - Math.cos(mDegreeA) * mRadiusA);

        mB0.x = (float) (mPointB.x + Math.sin(mDegreeA) * mRadiusB);
        mB0.y = (float) (mPointB.y - Math.cos(mDegreeA) * mRadiusB);

        mA1.x = (float) (mPointA.x - Math.sin(mDegreeA) * mRadiusA);
        mA1.y = (float) (mPointA.y + Math.cos(mDegreeA) * mRadiusA);

        mB1.x = (float) (mPointB.x - Math.sin(mDegreeA) * mRadiusB);
        mB1.y = (float) (mPointB.y + Math.cos(mDegreeA) * mRadiusB);

        mPath.reset();
        mPath.moveTo(mA0.x, mA0.y);
        mPath.quadTo((mPointA.x + mPointB.x) / 2, (mPointA.y + mPointB.y) / 2,
                mB0.x, mB0.y);
        mPath.moveTo(mB1.x, mB1.y);
        mPath.quadTo((mPointA.x + mPointB.x) / 2, (mPointA.y + mPointB.y) / 2,
                mA1.x, mA1.y);
        invalidate();
        return true;
    }

    private Path mPath;
    private double mDegreeA, mDegreeB;
    private PointF mPointA, mPointB;
    private PointF mA0, mB0;
    private PointF mA1, mB1;
    private float mRadiusA, mRadiusB;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mPointA.x, mPointA.y, mRadiusA, mPaint);
        canvas.drawCircle(mA0.x, mA0.y, 5, mPaint);
        canvas.drawCircle(mPointB.x, mPointB.y, mRadiusB, mPaint);
        canvas.drawCircle(mB0.x, mB0.y, 5, mPaint);
        canvas.drawLine(mPointA.x, mPointA.y, mPointB.x, mPointB.y, mPaint);

        canvas.drawLine(mPointA.x, mPointA.y, mA0.x, mA0.y, mPaint);
        canvas.drawLine(mPointB.x, mPointB.y, mB0.x, mB0.y, mPaint);

        canvas.drawPath(mPath, mPaint);
    }
}
