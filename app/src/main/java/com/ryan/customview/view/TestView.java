package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

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

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(30);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPath.lineTo(500, 500);
        mPath.lineTo(700, 200);
        Canvas canvas = new Canvas();
        dst = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(dst);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, mPaint);
        src = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(src);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, 200, 200, mPaint);

    }

    private Path mPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);


        int layerID = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.translate(mWidth / 2, mHeight / 2);

        canvas.drawBitmap(dst, -100, -100, mPaint);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawCircle(0, 0, 200, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawBitmap(src, 0, -100, mPaint);

//        mPaint.setColor(Color.YELLOW);
//        canvas.drawRect(0, 100, 200, -100, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);


    }

}
