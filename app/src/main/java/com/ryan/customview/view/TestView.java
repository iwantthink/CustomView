package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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

    private Bitmap dst, src;
    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mCirclePaint.setColor(Color.BLUE);
        mPaint.setColor(Color.RED);

    }

    private Path mPath = new Path();
    private float[] mPoint = new float[2];
    private Matrix mMatrix;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawAuxiliary(canvas);
//        canvas.drawColor(Color.BLACK);
        Log.d("TestView", "canvas.getSaveCount():" + canvas.getSaveCount());

        int id1 = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.clipRect(0, 0, 800, 800);
        canvas.drawColor(Color.RED);
        Log.d("TestView", "canvas.getSaveCount():" + (canvas.getSaveCount() + ",id1 = " + id1));

        int id2 = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.clipRect(0, 0, 600, 600);
        canvas.drawColor(Color.GREEN);
        Log.d("TestView", "canvas.getSaveCount():" + (canvas.getSaveCount() + ",id2 = " + id2));


        int id3 = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.clipRect(0, 0, 400, 400);
        canvas.drawColor(Color.BLUE);
        Log.d("TestView", "canvas.getSaveCount():" + (canvas.getSaveCount() + ",id3 = " + id3));

        canvas.restoreToCount(id2);
        canvas.drawColor(Color.GRAY);
        Log.d("TestView", "canvas.getSaveCount():" + canvas.getSaveCount());

    }

}
