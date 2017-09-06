package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.ryan.customview.R;

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
    private Bitmap mBitmap;
    private Bitmap mBitmapB;

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mCirclePaint.setColor(Color.BLUE);
        mPaint.setColor(Color.RED);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mBitmapB = mBitmap.extractAlpha();

    }

    private Path mPath = new Path();
    private float[] mPoint = new float[2];
    private Matrix mMatrix;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setColor(Color.BLUE);
        mPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER));
        canvas.drawBitmap(mBitmapB, 0, 0, mPaint);
        mPaint.setMaskFilter(null);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.restore();

    }

}
