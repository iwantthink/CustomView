package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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
    private GestureDetector mGestureDetector;

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("TestView", "onDown");
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.d("TestView", "showPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("TestView", "onSingleTapUp");
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("TestView", "onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("TestView", "onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("TestView", "onFling");
                return true;
            }
        });
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

//        setLayerType(LAYER_TYPE_SOFTWARE, null);

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



//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.drawCircle(0, 0, 100, mPaint);
    }


}
