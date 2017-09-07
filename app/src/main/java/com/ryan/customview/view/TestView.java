package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by renbo on 2017/8/22.
 */

public class TestView extends BaseView {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, 100);
        } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, MeasureSpec.getSize(heightMeasureSpec));
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 100);
        }
    }


    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animate().translationX(100);
            }
        });
    }

    private Path mPath = new Path();
    private float mFraction;
    private static final float DEFAULT_ROTATION = 45;

    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float fraction) {
        mFraction = fraction;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.drawColor(Color.BLACK);
        mPaint.setColor(Color.RED);
        canvas.save();
        canvas.rotate(DEFAULT_ROTATION * mFraction, mWidth / 2, mHeight / 2);
        canvas.drawRect(100, 100, mWidth - 100, mHeight - 100, mPaint);
        canvas.restore();
    }

}
