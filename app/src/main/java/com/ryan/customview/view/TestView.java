package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

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

    private Path mPath = new Path();
    private Region mRegion = new Region();
    private Region gloabalRegion = new Region();

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.addCircle(w / 2, h / 2, 500, Path.Direction.CW);
        Log.d("TestView", "w:" + w);
        Log.d("TestView", "h:" + h);
        RectF rectF = new RectF();
        mPath.computeBounds(rectF, true);
        gloabalRegion = new Region(0, 0,
                w, h);
        mRegion.setPath(mPath, gloabalRegion);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        return super.dispatchTouchEvent(event);
    }

    private float mX;
    private float mY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = event.getX();
        mY = event.getY();

        Log.d("TestView", "event.getX():" + event.getX());
        Log.d("TestView", "event.getRawX():" + event.getRawX());
        Log.d("TestView", "event.getY():" + event.getY());
        Log.d("TestView", "event.getRawY():" + event.getRawY());

        invalidate();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d("TestView", "mRegion.contains(x,y):" +
//                        mRegion.contains((int) mX, (int) mY));
                Log.d("TestView", "actionDown");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d("TestView", "actionMove");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TestView", "actionUp");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("TestView", "actionPointerDown");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("TestView", "actionPointerUp");
                break;
        }


        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAuxiliary(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        Matrix matrix = new Matrix();
        canvas.getMatrix().invert(matrix);
        float[] arrary = new float[]{mX, mY};
        matrix.mapPoints(arrary);
        canvas.drawCircle(arrary[0], arrary[1], 10, mPaint);
        canvas.restore();
    }


}
