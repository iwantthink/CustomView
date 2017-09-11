package com.ryan.customview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by renbo on 2017/9/8.
 */

public class CollapseView extends BaseView {
    public CollapseView(Context context) {
        super(context);
    }

    public CollapseView(Context context, AttributeSet attrs) {
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


    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initAnim();
        mPaint.setShader(new LinearGradient(0, 0, 500, 500, Color.BLUE, Color.YELLOW, Shader.TileMode.REPEAT));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAnimator && !mAnimator.isRunning()) {
                    mCurrentCollapse = CollapseOrientation.BOTTOM;
                    mCurrentCount = 0;
                    mAnimator.start();
                }
            }
        });
    }

    private ObjectAnimator mAnimator;
    private int mCurrentCount = 0;
    private static final float DEFAULT_ROTATION = 45;

    private void initAnim() {

        mAnimator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFraction = 0;
                mCurrentCollapse = DEFAULT_COLLAPSE_TYPE;
                mLastCollapse = DEFAULT_COLLAPSE_TYPE;
                postInvalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //默认从BOTTOM->RIGHT->TOP->LEFT
                //实际上是TOP->LEFT->BOTTOM->RIGHT
                switch (mCurrentCount) {
                    case 0:
                        mCurrentCollapse = CollapseOrientation.RIGHT;
                        mLastCollapse = CollapseOrientation.BOTTOM;
                        break;
                    case 1:
                        mCurrentCollapse = CollapseOrientation.TOP;
                        mLastCollapse = CollapseOrientation.RIGHT;
                        break;
                    case 2:
                        mCurrentCollapse = CollapseOrientation.LEFT;
                        mLastCollapse = CollapseOrientation.TOP;
                        break;
                    default:
                        mCurrentCollapse = CollapseOrientation.BOTTOM;
                        mLastCollapse = CollapseOrientation.BOTTOM;
                        break;
                }
                mCurrentCount++;
            }
        });
        mAnimator.setRepeatCount(3);
        mAnimator.setDuration(500);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAuxiliary(canvas);
        canvas.drawColor(Color.BLACK);
        drawNormal(canvas);
        switch (mCurrentCollapse) {
            //画右边 last is top
            case LEFT:
                //draw left
                drawCollapseOpen(canvas,
                        mWidth / 2, mWidth - 100, 100, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);

                break;
            //画底部
            case TOP:
                //draw top
                drawCollapseOpen(canvas,
                        100, mWidth - 100, mHeight / 2, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                break;
            //画顶部
            case BOTTOM:
                //draw bottom
                drawCollapseOpen(canvas,
                        100, mWidth - 100, 100, mHeight / 2,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                break;
            //画左边
            case RIGHT:
                //draw right
                drawCollapseOpen(canvas,
                        100, mWidth / 2, 100, mHeight - 100,
                        DEFAULT_ROTATION * mFraction, mCurrentCollapse);
                break;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = 100;
        mTop = 100;
        mRight = mWidth - 100;
        mBottom = mHeight / 2;
    }

    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;

    //实际绘制的部分相反 ...
    private CollapseOrientation DEFAULT_COLLAPSE_TYPE = CollapseOrientation.BOTTOM;
    private CollapseOrientation mCurrentCollapse = DEFAULT_COLLAPSE_TYPE;
    private CollapseOrientation mLastCollapse = DEFAULT_COLLAPSE_TYPE;

    enum CollapseOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private void drawNormal(Canvas canvas) {
        switch (mCurrentCollapse) {
            case LEFT:
                mLeft = 100;
                mTop = 100;
                mRight = mWidth / 2;
                mBottom = mHeight / 2;
                break;
            case RIGHT:
                mLeft = mWidth / 2;
                mTop = mHeight / 2;
                mRight = mWidth - 100;
                mBottom = mHeight - 100;
                break;
            case TOP:
                mLeft = mWidth / 2;
                mTop = 100;
                mRight = mWidth - 100;
                mBottom = mHeight / 2;
                break;
            case BOTTOM:
                if (mCurrentCollapse == DEFAULT_COLLAPSE_TYPE) {
                    mLeft = 100;
                    mTop = mHeight / 2;
                    mRight = mWidth - 100;
                    mBottom = mHeight - 100;
                } else {
                    mLeft = 100;
                    mTop = mHeight / 2;
                    mRight = mWidth / 2;
                    mBottom = mHeight - 100;
                }

                break;
        }

        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);

    }

    /*
        绘制的三个状态
        1. 只用绘制打开状态
        2. 只用绘制关闭状态
        3. 需要同时绘制打开和关闭状态
     */
    private void drawCollapseOpen(Canvas canvas,
                                  float drawLeft, float drawRight, float drawTop, float drawBottom,
                                  float rotateAngle, CollapseOrientation collapse) {
        //绘制打开状态
        canvas.save();
        switch (collapse) {
            case LEFT:
                canvas.clipRect(mWidth / 2, 0, mWidth, mHeight / 2);
                break;
            case TOP:
                canvas.clipRect(mWidth / 2, mHeight / 2, mWidth, mHeight);
                break;
            case RIGHT:
                canvas.clipRect(0, mHeight / 2, mWidth / 2, mHeight);
                break;
            case BOTTOM:
                canvas.clipRect(0, 0, mWidth, mHeight);
                break;
        }
        Camera camera = new Camera();
        camera.setLocation(0, 0, -15);
        canvas.translate(mWidth / 2, mHeight / 2);
        camera.save();
        switch (collapse) {
            case LEFT:
                camera.rotateY(-rotateAngle);
                break;
            case TOP:
                camera.rotateX(rotateAngle);
                break;
            case RIGHT:
                camera.rotateY(rotateAngle);
                break;
            case BOTTOM:
                camera.rotateX(-rotateAngle);
                break;
        }

        camera.applyToCanvas(canvas);
        canvas.translate(-mWidth / 2, -mHeight / 2);
        camera.restore();
        canvas.drawRect(drawLeft, drawTop, drawRight, drawBottom, mPaint);
        canvas.restore();


        drawCollapseClose(canvas, rotateAngle, camera, collapse);
        drawCollapseBoth(canvas, rotateAngle, camera, collapse);
    }

    private void drawCollapseClose(Canvas canvas, float rotateAngle, Camera camera, CollapseOrientation collapse) {
        canvas.save();
        switch (collapse) {
            case LEFT:
                canvas.clipRect(0, mHeight / 2, mWidth / 2, mHeight);
                break;
            case TOP:
                canvas.clipRect(0, 0, mWidth / 2, mHeight / 2);
                break;
            case RIGHT:
                canvas.clipRect(mWidth / 2, 0, mWidth, mHeight / 2);
                break;
            case BOTTOM:
                canvas.clipRect(mWidth / 2, mHeight / 2, mWidth, mHeight);
                break;
        }
        camera.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        switch (collapse) {
            case LEFT:
                camera.rotateX(DEFAULT_ROTATION - rotateAngle);
                break;
            case TOP:
                camera.rotateY(DEFAULT_ROTATION - rotateAngle);
                break;
            case RIGHT:
                camera.rotateX(-(DEFAULT_ROTATION - rotateAngle));
                break;
            case BOTTOM:
                camera.rotateY(-(DEFAULT_ROTATION - rotateAngle));
                break;
        }
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.translate(-mWidth / 2, -mHeight / 2);

        switch (collapse) {
            case LEFT:
                canvas.drawRect(100, mHeight / 2, mWidth / 2, mHeight - 100, mPaint);
                break;
            case TOP:
                canvas.drawRect(100, 100, mWidth / 2, mHeight / 2, mPaint);
                break;
            case RIGHT:
                canvas.drawRect(mWidth / 2, 100, mWidth - 100, mHeight / 2, mPaint);
                break;
            case BOTTOM:
                // 如果默认位置是bottom 则不需要绘制 默认位置之前的。。。因为没有啊
                if (collapse != DEFAULT_COLLAPSE_TYPE) {
                    canvas.drawRect(mWidth / 2, mHeight / 2, mWidth - 100, mHeight - 100, mPaint);
                }
                break;
        }

        canvas.restore();
    }

    private void drawCollapseBoth(Canvas canvas, float rotateAngle, Camera camera, CollapseOrientation collapse) {
        //绘制同时打开和关闭状态
        canvas.save();
        switch (collapse) {
            case LEFT:
                if (mLastCollapse == CollapseOrientation.TOP) {
                    canvas.clipRect(mWidth / 2, mHeight / 2, mWidth, mHeight);
                }
                break;
            case TOP:
                if (mLastCollapse == CollapseOrientation.RIGHT) {
                    canvas.clipRect(0, mHeight / 2, mWidth / 2, mHeight);
                }
                break;
            case RIGHT:
                if (mLastCollapse == CollapseOrientation.BOTTOM) {
                    canvas.clipRect(0, 0, mWidth / 2, mHeight / 2);
                }
                break;
            case BOTTOM:
                if (mLastCollapse == CollapseOrientation.LEFT) {
                    canvas.clipRect(mWidth / 2, 0, mWidth, mHeight / 2);
                }
                break;
        }

        canvas.translate(mWidth / 2, mHeight / 2);
        camera.save();
        switch (collapse) {
            case LEFT:
                if (mLastCollapse == CollapseOrientation.TOP) {
                    camera.rotateY(-rotateAngle);
                    camera.rotateX(DEFAULT_ROTATION - rotateAngle);
                }
                break;
            case TOP:
                if (mLastCollapse == CollapseOrientation.RIGHT) {
                    camera.rotateX(rotateAngle);
                    camera.rotateY(DEFAULT_ROTATION - rotateAngle);
                }
                break;
            case RIGHT:
                if (mLastCollapse == CollapseOrientation.BOTTOM) {
                    camera.rotateY(rotateAngle);
                    camera.rotateX(-(DEFAULT_ROTATION - rotateAngle));
                }
                break;
            case BOTTOM:
                if (mLastCollapse == CollapseOrientation.LEFT) {
                    camera.rotateX(-rotateAngle);
                    camera.rotateY(-(DEFAULT_ROTATION - rotateAngle));
                }
                break;
        }
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.translate(-mWidth / 2, -mHeight / 2);

        switch (collapse) {
            case LEFT:
                if (mLastCollapse == CollapseOrientation.TOP) {
                    canvas.drawRect(mWidth / 2, mHeight / 2, mWidth - 100, mHeight - 100, mPaint);
                }
                break;
            case TOP:
                if (mLastCollapse == CollapseOrientation.RIGHT) {
                    canvas.drawRect(100, mHeight / 2, mWidth / 2, mHeight - 100, mPaint);
                }
                break;
            case RIGHT:
                if (mLastCollapse == CollapseOrientation.BOTTOM) {
                    canvas.drawRect(100, 100, mWidth / 2, mHeight / 2, mPaint);
                }
                break;
            case BOTTOM:
                if (mLastCollapse == CollapseOrientation.LEFT) {
                    canvas.drawRect(mWidth / 2, 100, mWidth - 100, mHeight / 2, mPaint);
                }
                break;
        }

        canvas.restore();
    }

    private float mFraction;

    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float fraction) {
        mFraction = fraction;
        postInvalidate();
    }
}
