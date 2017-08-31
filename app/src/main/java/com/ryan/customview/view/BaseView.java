package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by renbo on 2017/8/21.
 */

public abstract class BaseView extends View {

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    protected float mWidth;
    protected float mHeight;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private Path mAuxiliaryPath = new Path();
    private Paint mAuxiliaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected void drawAuxiliary(Canvas canvas) {
        mAuxiliaryPath.moveTo(mWidth / 2, 0);
        mAuxiliaryPath.lineTo(mWidth / 2, mHeight);
        mAuxiliaryPath.moveTo(0, mHeight / 2);
        mAuxiliaryPath.lineTo(mWidth, mHeight / 2);
        mAuxiliaryPaint.setPathEffect(new DashPathEffect(new float[]{20, 5}, 0));
        mAuxiliaryPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mAuxiliaryPath, mAuxiliaryPaint);
        mAuxiliaryPaint.reset();


    }
}
