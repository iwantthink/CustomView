package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by renbo on 2017/8/22.
 */

public class TestView extends BaseView implements View.OnClickListener {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
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


    private void init(Context context) {
        setOnClickListener(this);
        mScroller = new Scroller(context);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private float mX;
    private float mY;
    private Scroller mScroller;

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        //1000ms内平滑的滑向destX,
        mScroller.startScroll(scrollX, scrollY, delta, deltaY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAuxiliary(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawCircle(0, 0, 50, mPaint);
        canvas.restore();
    }


    @Override
    public void onClick(View v) {

    }
}
