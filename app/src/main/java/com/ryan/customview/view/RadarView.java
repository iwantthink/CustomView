package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

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
    private int CIRCLE_COUNT = 5;
    private Model mModel;

    public void init() {
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
        }

        mModel = new Model(65, 99, 23, 4, 56, 77);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
        float percent = mModel.mA / 100;
        for (int i = 0; i < 6; i++) {
            float x = (float) (mBorderLth * Math.cos(30) * percent);
            float y = (float) (mBorderLth * Math.sin(30) * percent);
            canvas.drawCircle(x, y, 10, mPaint);
            if (i == 0) {
                mRadarPath.moveTo(x, y);
            } else {
                mRadarPath.lineTo(x, y);
            }
        }
        canvas.drawPath(mRadarPath, mPaint);
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

    class Model {
        float mA = 0;
        float mB = 0;
        float mC = 0;
        float mD = 0;
        float mE = 0;
        float mF = 0;

        public Model(float a, float b, float c, float d, float e, float f) {
            mA = a;
            mB = b;
            mC = c;
            mD = d;
            mE = e;
            mF = f;
        }
    }
}
