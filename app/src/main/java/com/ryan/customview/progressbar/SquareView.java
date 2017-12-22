package com.ryan.customview.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by renbo on 2017/12/15.
 */

public class SquareView extends View {

    private Square mSquare;
    private Paint mPaint;
    private int DEFAULT_WIDTH = 40;

    public SquareView(Context context) {
        super(context);
        init();
    }

    public SquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setPivot();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mSquare = new Square(0, DEFAULT_WIDTH, 0, DEFAULT_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_WIDTH);
    }

    //    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        mSquare = new Square(0, w, 0, h);
//        setPivot();
//    }

    private void setPivot() {
        setPivotX(mSquare.mRight);
        setPivotY(mSquare.mBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mSquare.mLeft, mSquare.mTop,
                mSquare.mRight, mSquare.mBottom, mPaint);
    }
}
