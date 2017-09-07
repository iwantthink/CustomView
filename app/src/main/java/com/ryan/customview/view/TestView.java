package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        mPaint.setColor(Color.RED);
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
    private Paint mPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        drawNormal(canvas);
//        drawCollapse(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = 100;
        mTop = 100;
        mRight = mWidth - 100;
        mBottom = mHeight / 2;
    }

    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;

    private CollapseOrientation mCurrentCollapse = CollapseOrientation.BOTTOM;

    enum CollapseOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private void drawNormal(Canvas canvas) {
        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
       
    }

    private void drawCollapse(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, mHeight / 2, mWidth, mHeight);
        Camera camera = new Camera();
        camera.setLocation(0, 0, -15);
        canvas.translate(mWidth / 2, mHeight / 2);
        camera.save();
        camera.rotateX(DEFAULT_ROTATION * mFraction);
        camera.applyToCanvas(canvas);
        canvas.translate(-mWidth / 2, -mHeight / 2);
        camera.restore();
        canvas.drawRect(100, mHeight / 2, mWidth - 100, mHeight - 100, mPaint);
        canvas.restore();
    }

}
