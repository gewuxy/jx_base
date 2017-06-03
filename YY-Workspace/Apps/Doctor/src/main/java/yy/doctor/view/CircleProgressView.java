package yy.doctor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import lib.ys.fitter.DpFitter;
import lib.ys.util.res.ResLoader;
import yy.doctor.R;

public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressBar";

    private int mMaxProgress = 100;

    private int mProgress = 30;

    private final int mCircleLineStrokeWidth = DpFitter.dp(5);

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(ResLoader.getColor(R.color.divider));
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = Math.min(width, height);

        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
        mRectF.right = min - mCircleLineStrokeWidth / 2; // 左下角x
        mRectF.bottom = min - mCircleLineStrokeWidth / 2; // 右下角y
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT);
        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        //绘制进度条
        mPaint.setColor(ResLoader.getColor(R.color.text_0882e7));
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

}