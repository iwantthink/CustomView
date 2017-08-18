package com.ryan.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by renbo on 2017/8/17.
 */

public class TestView extends View implements View.OnClickListener {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();
    private float[] pos;
    private float[] tan;
    private float mDis = 0;
    private PathMeasure mPathMeasure;
    private Matrix mMatrix = new Matrix();

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.addCircle(0, 0, 100, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mPath, true);
        pos = new float[2];
        tan = new float[2];
        mPathMeasure.getPosTan(mDis, pos, tan);
        setOnClickListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 111, mPaint);

        Log.d("TestView", "pos:" + pos[0] + "-" + pos[1]);
        Log.d("TestView", "tan:" + tan[0] + "-" + pos[1]);
        Log.d("TestView", "Math.atan2(tan[1],tan[0]):" + Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        //获取到角度..
        double tangle = Math.atan2(tan[1], tan[0]) * 180 / Math.PI;
        Log.d("TestView", "tangle:" + Math.toDegrees(tangle));

        Log.d("mMatrix", mMatrix.toShortString());

        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(pos[0], pos[1], 5, mPaint);
        mPaint1.setColor(Color.RED);
        canvas.drawLine(0, 0, pos[0], pos[1], mPaint1);

        canvas.save();
        //旋转指定角度
        canvas.rotate((float) tangle - 90);
        canvas.drawLine(100, -100, 100, 100, mPaint);
        canvas.restore();


        canvas.restore();

    }

    @Override
    public void onClick(View v) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) (200 * Math.PI));
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDis = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(mDis, pos, tan);
                mPathMeasure.getMatrix(mDis, mMatrix, PathMeasure.POSITION_MATRIX_FLAG);
                invalidate();
            }
        });

        valueAnimator.start();
    }
}
