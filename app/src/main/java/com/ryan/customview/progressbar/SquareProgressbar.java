package com.ryan.customview.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by renbo on 2017/12/18.
 */

public class SquareProgressbar extends FrameLayout {

    private SquareView[][] mSquareViews;
    private int DEFAULT_ROW = 3;
    private int DEFAULT_COLUMN = 4;
    private int mSide = 40;
    private int mPadding = 8;


    public SquareProgressbar(@NonNull Context context) {
        super(context);
    }

    public SquareProgressbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mContext = context;
    }
    Context mContext;

    private void init(Context context) {
        mSquareViews = new SquareView[DEFAULT_ROW][DEFAULT_COLUMN];
        for (int i = 0; i < DEFAULT_ROW; i++) {
            for (int j = 0; j < DEFAULT_COLUMN; j++) {
                mSquareViews[i][j] = new SquareView(context);
            }
        }
//        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int w = mSide * 4 + mPadding * 3;
        int h = mSide * 3 + mPadding * 2;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(500, 500);
//        setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        addView(mSquareViews[0][0]);
        addView(new SquareView(mContext));
    }
}
