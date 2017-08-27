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

    private Path mPath_top = new Path();
    private Path mPath_right = new Path();
    private Path mPath_bottom = new Path();
    private Path mPath_left = new Path();
    private Path mCirclePath = new Path();
    private Region mRegion_top = new Region();
    private Region mRegion_right = new Region();
    private Region mRegion_bottom = new Region();
    private Region mRegion_left = new Region();
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
        mPath_top.addArc(rectF, 225, 90);
        mPath_top.lineTo(centerX, centerY - mGap);
        mPath_top.close();
        mPath_top.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion_top.setPath(mPath_top, globalRegion);


        rectF = new RectF(centerX - mBigRadius + mGap, centerY - mBigRadius,
                centerX + mBigRadius + mGap, centerY + mBigRadius);
        mPath_right.arcTo(rectF, -45, 90);
        mPath_right.lineTo(centerX + mGap, centerY);
        mPath_right.close();
        mPath_right.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion_right.setPath(mPath_right, globalRegion);

        rectF = new RectF(centerX - mBigRadius, centerY - mBigRadius + mGap,
                centerX + mBigRadius, centerY + mBigRadius + mGap);
        mPath_bottom.arcTo(rectF, 45, 90);
        mPath_bottom.lineTo(centerX, centerY + mGap);
        mPath_bottom.close();
        mPath_bottom.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion_bottom.setPath(mPath_bottom, globalRegion);


        rectF = new RectF(centerX - mBigRadius - mGap, centerY - mBigRadius,
                centerX + mBigRadius - mGap, centerY + mBigRadius);
        mPath_left.arcTo(rectF, 135, 90);
        mPath_left.lineTo(centerX - mGap, centerY);
        mPath_left.close();
        mPath_left.op(mCirclePath, Path.Op.DIFFERENCE);
        mRegion_left.setPath(mPath_left, globalRegion);

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
                if (mRegion_top.contains(x, y)) {
                    mCurrentSelected = 1;

                } else if (mRegion_right.contains(x, y)) {
                    mCurrentSelected = 2;
                } else if (mRegion_bottom.contains(x, y)) {
                    mCurrentSelected = 3;
                } else if (mRegion_left.contains(x, y)) {
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
        drawArea(canvas, mPath_top, 1);
        drawArea(canvas, mPath_right, 2);
        drawArea(canvas, mPath_bottom, 3);
        drawArea(canvas, mPath_left, 4);
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
