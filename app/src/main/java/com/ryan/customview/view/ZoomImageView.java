package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by renbo on 2017/8/21.
 */

public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener {

    private Context mContext;
    private static final String TAG = ZoomImageView.class.getSimpleName();

    public ZoomImageView(Context context) {
        super(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context.getApplicationContext();
        init(context);
    }

    private ScaleGestureDetector mScaleGestureDetector;
    private Matrix mMatrix = new Matrix();
    private float[] mMatrixValues = new float[9];
    private float mScaleFactor = 1.0f;

    private void init(Context context) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //当前触摸点>=2时 ,说明在 进行缩放 等操作。请求父类不要去拦截事件
        int count = event.getPointerCount();
        if (count >= 2) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (getDrawable() == null) {
            return true;
        }
        float scaleFactor = detector.getScaleFactor();
        float currentScale = getScale();
        //控制最大值和 最小值
        if (currentScale < 4 && scaleFactor > 1.0f ||
                currentScale > 0.5 && scaleFactor < 1.0f) {
            if (currentScale * scaleFactor > 4) {
                scaleFactor = 4 / currentScale;
            } else if (currentScale * scaleFactor < 0.5f) {
                scaleFactor = 0.5f / currentScale;
            }

            mMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
        }
        return true;
    }

    public float getScale() {
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    private void checkBorderAndCenterWhenScale() {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
//        Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

        mMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            Log.d(TAG, "rect  = " + rect.toShortString());
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            Log.d(TAG, "rect  = " + rect.toShortString());
            matrix.mapRect(rect);
            Log.d(TAG, "rect  = " + rect.toShortString());
        }
        return rect;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private boolean mIsFirstLoad = true;

    @Override
    public void onGlobalLayout() {
        if (mIsFirstLoad) {

            Drawable drawable = getDrawable();
            int iW = drawable.getIntrinsicWidth();
            int iH = drawable.getIntrinsicHeight();
            int sW = getWidth();
            int sH = getHeight();
            //TODO 在这里可以做一些对图片大小的判断，如果图片大小大于屏幕大小，如果屏幕大小大于图片大小

            //移动图片到中心
            mMatrix.postTranslate((sW - iW) / 2, (sH - iH) / 2);
            setImageMatrix(mMatrix);
            mIsFirstLoad = false;
        }
    }
}
