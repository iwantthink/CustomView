package com.ryan.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;

import com.ryan.customview.view.BaseView;

/**
 * Created by renbo on 2017/12/11.
 */

public class SampleView extends BaseView {
    public SampleView(Context context) {
        super(context);
    }

    public SampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private Rect mRectSrc, mRectDst;
    private Bitmap mBitmap;
    private Path mPath;
    private Path mPathDst;
    private PathMeasure mPathMeasure;
    private Region mRegion;
    private RegionIterator mRegionIterator;
    private Paint.FontMetrics mFontMetrics;

    private void init(Context context) {
        mPath = new Path();
        mPathDst = new Path();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.puppy);
        mRectSrc = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mRectDst = new Rect(0, 0, 500, 500);
//        mPath.setFillType(Path.FillType.EVEN_ODD);
        mPathMeasure = new PathMeasure();
        mRegion = new Region();
        mRegionIterator = new RegionIterator(mRegion);
//        mPaint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.OVERLAY));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(new CornerPathEffect(10));

        mTextPaint.setTextSize(100);
        mFontMetrics = mTextPaint.getFontMetrics();

        setLayerType(LAYER_TYPE_SOFTWARE, null);

    }

    float[] mPos = new float[2];
    float[] mTan = new float[2];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        mPath.addRect(mWidth / 2 - 100, mHeight / 2 - 100,
//                mWidth / 2 + 100, mHeight / 2 + 100, Path.Direction.CW);
        mPath.addCircle(mWidth / 2, mHeight / 2, 300, Path.Direction.CW);
        mPathMeasure.setPath(mPath, true);

        mPathMeasure.getSegment(0, mPathMeasure.getLength() / 2, mPathDst, true);
        mPathMeasure.getPosTan(mPathMeasure.getLength() / 4, mPos, mTan);

    }

    Matrix matrix = new Matrix();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        Camera camera = new Camera();
        canvas.save();
        camera.save();//保存camera的状态

//        camera.rotateX(30); // 旋转 Camera 的三维空间
        camera.rotateY(-30); // 旋转 Camera 的三维空间
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        camera.restore();
        canvas.drawRect(100, 100, 500, 500, mPaint);
        canvas.restore();

//        canvas.save();
//        camera.save();
//        canvas.translate(300, 300);
//        camera.rotateY(-30);
//        camera.applyToCanvas(canvas);
//        canvas.translate(-300, -300);
//        camera.restore();
//        canvas.drawRect(100, 100, 500, 500, mPaint);
//        canvas.restore();
        canvas.drawRect(100, 100, 500, 500, mPaint);

        mPaint.setColor(Color.RED);
        canvas.save();
        camera.save();
        camera.rotateY(-30);
        camera.setLocation(4, -4, -8);
        camera.getMatrix(matrix);
//        matrix.preTranslate(-300, -300);
//        matrix.postTranslate(300, 300);
        camera.restore();
        canvas.concat(matrix);
        canvas.drawRect(100, 100, 500, 500, mPaint);
        canvas.restore();


//        mPaint.setColor(Color.RED);
//        canvas.save();
//        camera.save();
//        camera.translate(0, 0, 100);
//        camera.getMatrix(matrix);
//        camera.restore();
//        canvas.concat(matrix);
//        canvas.drawRect(100, 100, 500, 500, mPaint);
//        canvas.restore();
//
//        canvas.drawLine(100, mHeight / 2, 200, mHeight / 2, mPaint);


//        canvas.drawPath(mPathDst, mPaint);
//        drawAuxiText(canvas, "mPost = " + mPos[0] + "," + mPos[1]);
//        canvas.drawCircle(mPos[0], mPos[1], 10, mPaint);
//        canvas.drawPath(mPath, mPaint);;
//        canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
//        canvas.drawRect(mWidth / 2 - 100, mHeight / 2 - 100, mWidth / 2 + 100, mHeight / 2 + 100, mPaint);
//        String str = "abcd$%^&梁朝伟";
//        canvas.drawText(str, 0, mHeight / 2, mTextPaint);
//        canvas.save();
//        canvas.translate(0, mHeight / 2);
//        mPaint.reset();
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.RED);
//        Log.d("SampleView", "mFontMetrics.top:" + mFontMetrics.top);
//        canvas.drawLine(0, mFontMetrics.top, mWidth, mFontMetrics.top, mPaint);
//        mPaint.setColor(Color.GREEN);
//        canvas.drawLine(0, mFontMetrics.bottom, mWidth, mFontMetrics.bottom, mPaint);
//        mPaint.setColor(Color.YELLOW);
//        canvas.drawLine(0, mFontMetrics.ascent, mWidth, mFontMetrics.ascent, mPaint);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawLine(0, mFontMetrics.descent, mWidth, mFontMetrics.descent, mPaint);
//        Rect rect = new Rect();
//        mTextPaint.getTextBounds(str, 0, str.length(), rect);
//        canvas.drawRect(rect, mPaint);
//        canvas.restore();
//        canvas.rotate(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
