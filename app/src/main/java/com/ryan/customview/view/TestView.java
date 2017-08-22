package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by renbo on 2017/8/22.
 */

public class TestView extends BaseView {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Path mPath = new Path();


    private void init() {
        mTextPaint.setTextSize(150);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        Log.d("TestView", fontMetrics.toString());


    }

    private TextPaint mTextPaint = new TextPaint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        drawAuxiliary(canvas);
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawText("dh达芙妮", 0, 0, mTextPaint);
        canvas.restore();
    }


}
