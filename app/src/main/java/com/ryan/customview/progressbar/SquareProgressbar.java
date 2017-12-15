package com.ryan.customview.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by renbo on 2017/12/15.
 */

public class SquareProgressbar extends View {

    private Square mSquare;
    private Paint mPaint;

    public SquareProgressbar(Context context) {
        super(context);
        init();
    }

    public SquareProgressbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSquare = new Square(0, w / 4,
                h / 4 * 3, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mSquare.mLeft, mSquare.mTop,
                mSquare.mRight, mSquare.mBottom, mPaint);
    }

    public void startRoll() {
        
    }
}
