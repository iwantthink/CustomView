package com.ryan.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ryan.customview.R;

/**
 * Created by renbo on 2017/8/28.
 */

public class DragView extends View {
    String TAG = "Gcs";

    Bitmap mBitmap;         // 图片
    RectF mBitmapRectF;     // 图片所在区域
    Matrix mBitmapMatrix;   // 控制图片的 matrix

    boolean canDrag = false;
    PointF lastPoint = new PointF(0, 0);

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 调整图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 960 / 2;
        options.outHeight = 800 / 2;

        mBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.test, options);
        mBitmapRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mBitmapMatrix = new Matrix();
    }

    private int first_down_id;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DragView", "action_down");
                first_down_id = event.getPointerId(index);
                // 判断按下位置是否包含在图片区域内
                if (mBitmapRectF.contains((int) event.getX(), (int) event.getY())) {
                    canDrag = true;
                    lastPoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("DragView", "action_up");
                canDrag = false;
            case MotionEvent.ACTION_MOVE:
                Log.d("DragView", "action_move");
                if (canDrag && (event.getPointerId(index) == first_down_id)) {
                    // 移动图片
                    mBitmapMatrix.postTranslate(event.getX() - lastPoint.x, event.getY() - lastPoint.y);
                    // 更新上一次点位置
                    lastPoint.set(event.getX(), event.getY());

                    // 更新图片区域
                    mBitmapRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
                    mBitmapMatrix.mapRect(mBitmapRectF);

                    invalidate();
                }
                break;
        }

        return true;
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mBitmapMatrix, mPaint);
    }
}
