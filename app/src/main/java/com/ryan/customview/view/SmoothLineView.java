package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by renbo on 2017/8/31.
 */

public class SmoothLineView extends BaseView {
    public SmoothLineView(Context context) {
        super(context);
    }

    public SmoothLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private Path mPath = new Path();
    private float mLastX = 0;
    private float mLastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (mLastX + x) / 2;
                float endY = (mLastY + y) / 2;
                mPath.quadTo(mLastX, mLastY, endX, endY);
                mLastX = x;
                mLastY = y;

                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
