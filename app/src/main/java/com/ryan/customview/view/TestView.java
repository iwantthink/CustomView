package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
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
        init();
    }

    private Path mPath = new Path();
    private Paint.FontMetrics mFontMetrics;
    private Rect mRect = new Rect();

    private void init() {
        mTextPaint.setTextSize(50);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mFontMetrics = mTextPaint.getFontMetrics();
        mTextPaint.getTextBounds(mStr, 0, mStr.length(), mRect);
        mPaint.setStyle(Paint.Style.STROKE);

//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initBitmapAndMatrix();
    }

    private TextPaint mTextPaint = new TextPaint();
    private String mStr = "dgh达芙妮";
    Matrix mPolyMatrix = new Matrix();
    Bitmap mBitmap;

    private void initBitmapAndMatrix() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        mPolyMatrix = new Matrix();


        float[] src = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下

        float[] dst = {-50, -50,                                    // 左上
                mBitmap.getWidth(), 0,                        // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),  // 右下
                0, mBitmap.getHeight()};                        // 左下

        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, 1); // src.length >> 1 为位移运算 相当于处以2

        // 此处为了更好的显示对图片进行了等比缩放和平移(图片本身有点大)
//        mPolyMatrix.postScale(0.26f, 0.26f);
//        mPolyMatrix.postTranslate(0, 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAuxiliary(canvas);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);
        canvas.restore();
    }


}
