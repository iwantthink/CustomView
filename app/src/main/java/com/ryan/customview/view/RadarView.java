package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by renbo on 2017/8/21.
 */

public class RadarView extends BaseView {


    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Path mRadarPath = new Path();
    private String[] mArray = new String[]{"a", "b", "c", "d", "e", "f"};
    private float mBorderLth;
    private float mGap = 50.0f;
    private float mMaxGap = 0f;
    private int CIRCLE_COUNT = 5;
    private int[] mModel = null;
    private Paint mRegionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void init() {
        Log.d("RadarView", "init");
        mPaint.setStyle(Paint.Style.STROKE);
        float temp = 0;

        for (int i = 0; i < CIRCLE_COUNT; i++) {
            temp = mGap * i + mGap;
            mBorderLth = (float) (temp * 2.0f * Math.tan(30 * Math.PI / 180));

            mRadarPath.moveTo(-mBorderLth / 2, -temp);
            mRadarPath.lineTo(mBorderLth / 2, -temp);
            mRadarPath.lineTo(mBorderLth, 0);
            mRadarPath.lineTo(mBorderLth / 2, temp);
            mRadarPath.lineTo(-mBorderLth / 2, temp);
            mRadarPath.lineTo(-mBorderLth, 0);
            mRadarPath.close();
            mMaxGap = temp;
        }

        mModel = new int[]{65, 99, 23, 4, 56, 77};
        mRegionPaint.setStyle(Paint.Style.FILL);
        mRegionPaint.setAlpha(155);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("RadarView", "onDraw");
        drawAuxiliary(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawPath(mRadarPath, mPaint);
        drawRadarPath(canvas);
        drawRegion(canvas);
        canvas.restore();
    }


    private void drawRegion(Canvas canvas) {
        mRadarPath.reset();
        canvas.save();

        for (int i = 0; i < 6; i++) {
            float percent = mModel[i] / 100f;
            Log.d("RadarView", "mModel[i]:" + mModel[i]);
            Log.d("RadarView", "percent:" + percent);
            float x = (float) (mBorderLth * percent * Math.cos(i * Math.toRadians(60)));
            float y = (float) (mBorderLth * percent * Math.sin(i * Math.toRadians(60)));
            canvas.drawCircle(x, y, 5, mPaint);
            canvas.drawText("" + i, x, y, mPaint);
            if (i == 0) {
                mRadarPath.moveTo(x, y);
            } else {
                mRadarPath.lineTo(x, y);
            }
        }
        mRadarPath.close();
        canvas.drawPath(mRadarPath, mRegionPaint);
        canvas.restore();
    }

    private void drawRadarPath(Canvas canvas) {
        canvas.rotate(-30);
        for (int i = 0; i < 6; i++) {
            canvas.save();
            canvas.rotate(i * 60);
            canvas.drawLine(0, 0, 0, -mBorderLth, mPaint);
            canvas.translate(0, -mBorderLth - 20);
            canvas.rotate(-i * 60 + 30);
            canvas.drawText(mArray[i], 0, 0, mPaint);
            canvas.restore();
        }
        canvas.rotate(30);
    }

}
