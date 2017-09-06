package com.ryan.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.IdRes;
import android.util.AttributeSet;

import com.ryan.customview.R;

/**
 * Created by renbo on 2017/9/6.
 */

public class ShadowBitmapView extends BaseView {

    private Bitmap mSrcBitmap;
    private Bitmap mCounterPart;
    @IdRes
    private int mSrcReference;
    private int mShadowDx;
    private int mShadowDy;
    private int mShadowColor;
    private float mShadowRaidus;

    public ShadowBitmapView(Context context) {
        super(context);
    }

    public ShadowBitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //maskFIlter 不支持硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShadowBitmapView);
        if (null != array) {
            mSrcReference = array.getInt(R.styleable.ShadowBitmapView_shadowSrc, R.mipmap.ic_launcher);
            mShadowDx = array.getInt(R.styleable.ShadowBitmapView_shadowDx, 10);
            mShadowDy = array.getInt(R.styleable.ShadowBitmapView_shadowDy, 10);
            mShadowColor = array.getColor(R.styleable.ShadowBitmapView_shadowColor, Color.BLACK);
            mShadowRaidus = array.getFloat(R.styleable.ShadowBitmapView_shadowRadius, 1);
            array.recycle();
        }
        //TODO 对传入的bitmap的大小进行判断..防止过大OOM
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), mSrcReference);
        mCounterPart = mSrcBitmap.extractAlpha();
        mPaint.setColor(mShadowColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mSrcBitmap.getWidth(), mSrcBitmap.getHeight());
        } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mSrcBitmap.getWidth(), MeasureSpec.getSize(heightMeasureSpec));
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mSrcBitmap.getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);
        mPaint.setMaskFilter(new BlurMaskFilter(mShadowRaidus, BlurMaskFilter.Blur.NORMAL));
        //预留空间给阴影
        int width = getWidth() - mShadowDx;
        //按宽度比例设置高度
        int height = width * mCounterPart.getHeight() / mCounterPart.getWidth();
        //阴影空间的位移
        canvas.drawBitmap(mCounterPart, null, new RectF(mShadowDx, mShadowDy, width, height), mPaint);
        mPaint.setMaskFilter(null);
        canvas.drawBitmap(mSrcBitmap, null, new RectF(0, 0, width, height), mPaint)
        ;
    }
}
