package com.ryan.customview.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ryan.customview.R;

/**
 * Created by renbo on 2017/12/14.
 */

public class RyanProgressbar extends View {

    private float mProgress = 0f;//当前进度
    private float mTotalProgress = 100f;//进度总大小
    private float mProgressRadius;//圆形半径 指的是圆心到圆边中心点位置
    private float mForegroundWidth = 1f, mBackgroundWidth = 1f;//进度条大小和背景大小
    private int mForegroundColor = 0xffff0000, mBackgroundColor = 0xffcccccc;//进度条颜色和背景颜色
    private float mProgressWidth, mProgressHeight;//控件的长宽
    private Paint.Cap mForegroundCap = Paint.Cap.BUTT;//进度条的头部形状
    private boolean mTouchable = true;//是否可以拖动进度条
    private int mProgressPercent = 0;//当前进度百分比
    private int mPercentTextSize = 5;//单位是 sp
    private int mPercentTextColor = 0xffff0000;
    private boolean mIsShowPercent = true;

    private Context mContext;
    private Paint mInnerPaint;
    private Paint mOuterPaint;
    private TextPaint mTextPaint;
    private RectF mArcRectf = new RectF();//控制弧线的矩形
    private float mStartAngle = 0, mSweepAngle = 180;//弧线开始角度，顺时针弧线辐射角度
    private float mTextX, mTextY;//文字的坐标

    public RyanProgressbar(Context context) {
        super(context);
        init(context, null);
    }

    public RyanProgressbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mForegroundWidth = dp2px(mContext, 5);//默认进度条宽度
        mBackgroundWidth = dp2px(mContext, 5);//默认背景进度条宽度
        mPercentTextSize = sp2px(context, 5);//默认字体大小

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.RyanProgressbar, 0, 0);
            mForegroundWidth = dp2px(context,
                    (int) typedArray.getFloat(R.styleable.RyanProgressbar_rpb_foreground_width, 5));
            mBackgroundWidth = dp2px(context,
                    (int) typedArray.getFloat(R.styleable.RyanProgressbar_rpb_background_width, 5));
            mTouchOffset = dp2px(context,
                    (int) typedArray.getFloat(R.styleable.RyanProgressbar_rpb_touch_offset, 5));
            mProgress = typedArray.getFloat(R.styleable.RyanProgressbar_rpb_progress, 0);
            mTotalProgress = typedArray.getFloat(R.styleable.RyanProgressbar_rpb_total_progress, 100);
            mForegroundColor = typedArray.getColor(R.styleable.RyanProgressbar_rpb_foreground_color, 0xffff0000);
            mBackgroundColor = typedArray.getColor(R.styleable.RyanProgressbar_rpb_background_color, 0xffcccccc);
            mForegroundCap = typedArray.getBoolean(R.styleable.RyanProgressbar_rpb_round_cap, true) ? Paint.Cap.ROUND : Paint.Cap.BUTT;
            mTouchable = typedArray.getBoolean(R.styleable.RyanProgressbar_rpb_touchable, true);
            mPercentTextSize = sp2px(context,
                    (int) typedArray.getFloat(R.styleable.RyanProgressbar_rpb_text_size, 5));
            mPercentTextColor = typedArray.getColor(R.styleable.RyanProgressbar_rpb_text_color, 0xffff0000);
            mIsShowPercent = typedArray.getBoolean(R.styleable.RyanProgressbar_rpb_show_percent, true);
            typedArray.recycle();
        }

        mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeWidth(mForegroundWidth);
        mOuterPaint.setStrokeWidth(mBackgroundWidth);
        mInnerPaint.setColor(mForegroundColor);
        mOuterPaint.setColor(mBackgroundColor);
        mInnerPaint.setStrokeCap(mForegroundCap);
        mTextPaint.setColor(mPercentTextColor);
        mTextPaint.setTextSize(mPercentTextSize);
        calculateProgressPercent();


        pppp.setStyle(Paint.Style.STROKE);
    }

    private void calculateProgressPercent() {
        mProgressPercent = (int) (mProgress * 100 / mTotalProgress);
        calculateText();//百分比变了  文字大小也变了 需要重新计算
    }


    public static int dp2px(Context context, int dpVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }

    public static int sp2px(Context context, int spVal) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    /**
     * 取较小的那个长度为 进度条的长宽
     * 同时计算进度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(w, h);
        mProgressWidth = min;
        mProgressHeight = min;
        setMeasuredDimension(min, min);
        initProgress();
        initRaidus();
        initArcRect();
        calculateText();
    }

    /**
     * 计算进度条角度
     */
    private void initProgress() {
        mSweepAngle = mProgress / mTotalProgress * 360;
    }

    /**
     * 计算进度条所占范围
     */
    private void initArcRect() {
        //根据rectf 画出来的弧线， 弧线半径指的是 圆心到rectf边中心点位置
        mArcRectf.left = mProgressWidth / 2 - mProgressRadius;
        mArcRectf.right = mProgressWidth / 2 + mProgressRadius;
        mArcRectf.top = mProgressHeight / 2 - mProgressRadius;
        mArcRectf.bottom = mProgressHeight / 2 + mProgressRadius;
    }

    /**
     * 计算进度条所占半径
     */
    private void initRaidus() {
        float max = mBackgroundWidth > mForegroundWidth ? mBackgroundWidth : mForegroundWidth;
        //进度条的半径
        mProgressRadius = (mProgressWidth - max) / 2;
    }

    Paint pppp = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 计算文字显示在中心的位置
     */
    private void calculateText() {
        float strLength = mTextPaint.measureText(mProgressPercent + "%");
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textOffset = Math.abs(fontMetrics.ascent) - (fontMetrics.descent - fontMetrics.ascent) / 2;
        mTextX = mProgressWidth / 2 - strLength / 2;
        mTextY = mProgressHeight / 2 + textOffset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mProgressWidth / 2, mProgressWidth / 2,
                mProgressRadius, mOuterPaint);
        canvas.drawArc(mArcRectf, mStartAngle, mSweepAngle, false, mInnerPaint);
        if (mIsShowPercent) {
            canvas.drawText(mProgressPercent + "%",
                    mTextX,
                    mTextY,
                    mTextPaint);
        }


//        canvas.drawRect(0, 0, mProgressWidth, mProgressHeight, pppp);
//        canvas.drawLine(0, mProgressHeight / 2,
//                mProgressWidth, mProgressHeight / 2, pppp);
//        canvas.drawLine(mProgressWidth / 2, 0, mProgressWidth / 2,
//                mProgressHeight, pppp);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mTouchable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkIsInCircle(event)) {
                    if (mIProgressChangedListener != null) {
                        mIProgressChangedListener.startChanged();
                    }
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                float touchY = event.getY();

                double atan2 = Math.atan2(touchY - mProgressWidth / 2, touchX - mProgressHeight / 2);
                mSweepAngle = (float) Math.toDegrees(atan2);
                if (mSweepAngle < 0) {
                    mSweepAngle += 360;
                }
                mProgress = mSweepAngle / 360 * mTotalProgress;
                calculateProgressPercent();
                if (mIProgressChangedListener != null) {
                    mIProgressChangedListener.progressChanged(mProgress);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mIProgressChangedListener != null) {
                    mIProgressChangedListener.endChanged();
                }
                break;
        }
        return true;
    }

    private float mTouchOffset = 20;

    private boolean checkIsInCircle(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        Region region = new Region(0, 0, (int) mProgressWidth, (int) mProgressHeight);
        Path path = new Path();
        Path pathB = new Path();
        path.addCircle(mProgressWidth / 2, mProgressHeight / 2, mProgressRadius + mBackgroundWidth / 2 + mTouchOffset, Path.Direction.CW);
        pathB.addCircle(mProgressWidth / 2, mProgressHeight / 2, mProgressRadius - mBackgroundWidth / 2 - mTouchOffset, Path.Direction.CW);
        path.op(pathB, Path.Op.DIFFERENCE);
        region.setPath(path, region);
        if (region.contains((int) touchX, (int) touchY)) {
            return true;
        } else {
            return false;
        }
    }

    public interface IProgressChangedListener {
        void startChanged();

        void endChanged();

        void progressChanged(float progress);
    }

    private IProgressChangedListener mIProgressChangedListener;

    public void setIProgressChangedListener(IProgressChangedListener listener) {
        mIProgressChangedListener = listener;
    }


    public float getProgress() {
        return mProgress;
    }

    /**
     * 设置当前进度大小
     *
     * @param progress
     */
    public void setProgress(float progress) {
        if (progress > mTotalProgress) {
            progress = mTotalProgress;
        } else if (progress < 0) {
            progress = 0;
        }
        mProgress = progress;
        initProgress();
        calculateProgressPercent();
        invalidate();
    }

    /**
     * 设置总进度大小
     *
     * @param progress
     */
    public void setTotalProgress(float progress) {
        mTotalProgress = progress;
        initProgress();
        calculateProgressPercent();
        invalidate();
    }

    /**
     * 设置进度条宽度 单位是dp
     *
     * @param foregroundWidth
     */
    public void setForegroundWidth(float foregroundWidth) {
        mForegroundWidth = dp2px(mContext, (int) foregroundWidth);
        mInnerPaint.setStrokeWidth(mForegroundWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 设置背景进度条宽度 单位是dp
     *
     * @param backgroundWidth
     */
    public void setBackgroundWidth(float backgroundWidth) {
        mBackgroundWidth = dp2px(mContext, (int) backgroundWidth);
        mOuterPaint.setStrokeWidth(mBackgroundWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param foregroundColor
     */
    public void setForegroundProgressColor(int foregroundColor) {
        mForegroundColor = foregroundColor;
        mInnerPaint.setColor(mForegroundColor);
        invalidate();
    }

    /**
     * 设置背景进度条颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundProgressColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mOuterPaint.setColor(mBackgroundColor);
        invalidate();
    }

    /**
     * 设置进度条的头部形状
     *
     * @param foregroundCap
     */
    public void setForegroundCap(Paint.Cap foregroundCap) {
        mForegroundCap = foregroundCap;
        mInnerPaint.setStrokeCap(mForegroundCap);
        invalidate();
    }

    /**
     * 设置百分度字体大小 单位:sp
     *
     * @param textSize
     */
    public void setPercentTextSize(int textSize) {
        mPercentTextSize = textSize;
        mTextPaint.setTextSize(sp2px(mContext, mPercentTextSize));
        calculateText();
        invalidate();
    }

    /**
     * 设置百分比字体颜色
     *
     * @param color
     */
    public void setPercentTextColor(int color) {
        mPercentTextColor = color;
        mTextPaint.setColor(mPercentTextColor);
        invalidate();
    }


    /**
     * 是否显示百分比
     *
     * @param isShowPercent
     */
    public void setShowPercent(boolean isShowPercent) {
        mIsShowPercent = isShowPercent;
        invalidate();
    }


}
