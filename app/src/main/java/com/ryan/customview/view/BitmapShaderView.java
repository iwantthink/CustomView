package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import com.ryan.customview.R;

/**
 * Created by renbo on 2017/9/6.
 */

public class BitmapShaderView extends BaseView {

    private BitmapShader mBitmapShader;
    private float mBitmapW, mBitmapH;

    public BitmapShaderView(Context context) {
        super(context);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.puppy);
        mBitmapW = bitmap.getWidth();
        mBitmapH = bitmap.getHeight();
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
//            setMeasuredDimension((int) (mBitmapW > mBitmapH ? mBitmapH : mBitmapW), (int) (mBitmapW > mBitmapH ? mBitmapH : mBitmapW));
            setMeasuredDimension((int) (mBitmapW), (int) (mBitmapH));
        } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (mBitmapW), MeasureSpec.getSize(heightMeasureSpec));
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) mBitmapH);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Matrix matrix = new Matrix();
        float scale = getWidth() / mBitmapW;
        Log.e("BitmapShaderView", "scale:" + scale);
        matrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(mBitmapShader);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = w > h ? h / 2 : w / 2;
    }

    private float mCenterX, mCenterY;
    private float mRadius;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
    }
}
