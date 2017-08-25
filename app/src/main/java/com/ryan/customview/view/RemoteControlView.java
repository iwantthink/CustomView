package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by renbo on 2017/8/25.
 */

public class RemoteControlView extends BaseView {
    public RemoteControlView(Context context) {
        super(context);
    }

    public RemoteControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private Path mPath1 = new Path();
    private Path mPath2 = new Path();
    private Path mPath3 = new Path();
    private Path mPath4 = new Path();
    private Path mCirclePath = new Path();
    private Region mRegion1 = new Region();
    private Region mRegion2 = new Region();
    private Region mRegion3 = new Region();
    private Region mRegion4 = new Region();
    private Region mCircleRegion = new Region();

    private float mGap = 20;
    private float mBigRadius = 250;
    private float mSmallRaidus = 150;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

//        mPath.addArc(100, 100, 300, 300, 225, 80);
        int centerX = w / 2;
        int centerY = h / 2;

        mCirclePath.addCircle(centerX, centerY,
                mSmallRaidus, Path.Direction.CW);
        Region globalRegion = new Region(0, 0, w, h);

        mPath1.arcTo(centerX - mBigRadius, centerY - mBigRadius - mGap,
                centerX + mBigRadius, centerY + mBigRadius - mGap, 225, 90, true);
        mPath1.lineTo(centerX, centerY - mGap);
        mPath1.close();
        mPath1.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion1.setPath(mPath1, globalRegion);

        mPath2.arcTo(centerX - mBigRadius + mGap, centerY - mBigRadius,
                centerX + mBigRadius + mGap, centerY + mBigRadius, -45, 90, true);
        mPath2.lineTo(centerX + mGap, centerY);
        mPath2.close();
        mPath2.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion2.setPath(mPath1, globalRegion);


        mPath3.arcTo(centerX - mBigRadius, centerY - mBigRadius + mGap,
                centerX + mBigRadius, centerY + mBigRadius + mGap, 45, 90, true);
        mPath3.lineTo(centerX, centerY + mGap);
        mPath3.close();
        mPath3.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion3.setPath(mPath1, globalRegion);

        mPath4.arcTo(centerX - mBigRadius - mGap, centerY - mBigRadius,
                centerX + mBigRadius - mGap, centerY + mBigRadius, 135, 90, true);
        mPath4.lineTo(centerX - mGap, centerY);
        mPath4.close();
        mPath4.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion4.setPath(mPath1, globalRegion);

        mCirclePath.reset();
        mCirclePath.addCircle(centerX, centerY, mSmallRaidus - mGap * (float) Math.sqrt(2), Path.Direction.CW);
        mCircleRegion.setPath(mCirclePath, globalRegion);

    }

    private int mCurrentSelected = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mRegion1.contains(x, y)) {
                    mCurrentSelected = 1;
                } else if (mRegion2.contains(x, y)) {
                    mCurrentSelected = 2;
                } else if (mRegion3.contains(x, y)) {
                    mCurrentSelected = 3;
                } else if (mRegion4.contains(x, y)) {
                    mCurrentSelected = 4;
                }
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawPath(mPath1, mPaint);
        canvas.drawPath(mPath2, mPaint);
        canvas.drawPath(mPath3, mPaint);
        canvas.drawPath(mPath4, mPaint);
        canvas.drawPath(mCirclePath, mPaint);

        canvas.restore();
    }
}
