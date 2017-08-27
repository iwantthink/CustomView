package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
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
        mContext = context.getApplicationContext();
    }

    private Context mContext;

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

    private float mGap = 20;//扇形之间的间隙
    private float mBigRadius = 250;//扇形的半径
    private float mSmallRaidus = 150;//用来切割扇形的小圆半径
    private IselectedListener mSelectedListener;

    public void setSelectedListener(@NonNull IselectedListener listener) {
        mSelectedListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = w / 2;
        int centerY = h / 2;

        mCirclePath.addCircle(centerX, centerY,
                mSmallRaidus, Path.Direction.CW);
        Region globalRegion = new Region(0, 0, w, h);


        RectF rectF = new RectF(centerX - mBigRadius, centerY - mBigRadius - mGap,
                centerX + mBigRadius, centerY + mBigRadius - mGap);
        mPath1.addArc(rectF, 225, 90);
        mPath1.lineTo(centerX, centerY - mGap);
        mPath1.close();
        mPath1.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion1.setPath(mPath1, globalRegion);


        rectF = new RectF(centerX - mBigRadius + mGap, centerY - mBigRadius,
                centerX + mBigRadius + mGap, centerY + mBigRadius);
        mPath2.arcTo(rectF, -45, 90);
        mPath2.lineTo(centerX + mGap, centerY);
        mPath2.close();
        mPath2.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion2.setPath(mPath2, globalRegion);

        rectF = new RectF(centerX - mBigRadius, centerY - mBigRadius + mGap,
                centerX + mBigRadius, centerY + mBigRadius + mGap);
        mPath3.arcTo(rectF, 45, 90);
        mPath3.lineTo(centerX, centerY + mGap);
        mPath3.close();
        mPath3.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion3.setPath(mPath3, globalRegion);


        rectF = new RectF(centerX - mBigRadius - mGap, centerY - mBigRadius,
                centerX + mBigRadius - mGap, centerY + mBigRadius);
        mPath4.arcTo(rectF, 135, 90);
        mPath4.lineTo(centerX - mGap, centerY);
        mPath4.close();
        mPath4.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion4.setPath(mPath4, globalRegion);

        mCirclePath.reset();
        mCirclePath.addCircle(centerX, centerY, mSmallRaidus - mGap * (float) Math.sqrt(2), Path.Direction.CW);
        mCircleRegion.setPath(mCirclePath, globalRegion);

    }

    private int mCurrentSelected = 0;//选中区域 0为未选中

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                } else if (mCircleRegion.contains(x, y)) {
                    mCurrentSelected = 5;
                } else {
                    mCurrentSelected = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentSelected = 0;
                break;
        }
        if (null != mSelectedListener) {
            mSelectedListener.selected(mCurrentSelected);
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArea(canvas, mPath1, 1);
        drawArea(canvas, mPath2, 2);
        drawArea(canvas, mPath3, 3);
        drawArea(canvas, mPath4, 4);
        drawArea(canvas, mCirclePath, 5);
    }

    private void drawArea(Canvas canvas, Path path, int currentIndex) {
        mPaint.setStyle(Paint.Style.STROKE);
        if (mCurrentSelected == currentIndex) {
            mPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawPath(path, mPaint);
    }

    public interface IselectedListener {
        void selected(int selectedIndex);
    }

    ;
}
