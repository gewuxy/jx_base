package lib.ys.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import lib.ys.ConstantsEx;
import lib.ys.R;
import lib.ys.util.XmlAttrUtil;
import lib.ys.util.view.ViewUtil;


/**
 * <p>
 * 不能设置background,否则没有圆角效果
 * </p>
 */
public class CornerView extends FrameLayout {

    private static final int KDefaultRadiusDp = 5;
    private static final int KDefaultStrokeColor = Color.parseColor("#cccccc");

    private RectF mRoundRectContent;
    private RectF mRoundRectStroke;

    private Path mPathContent;
    private Path mPathStroke;

    private int mStrokeWidth;
    private float mRadius;
    private int mStrokeColor;

    private Drawable mForeground;


    public CornerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerView);
        mStrokeWidth = a.getDimensionPixelOffset(R.styleable.CornerView_corner_strokeWidth, 0);
        mRadius = a.getDimensionPixelOffset(R.styleable.CornerView_corner_radius, ConstantsEx.KInvalidValue);
        mStrokeColor = a.getColor(R.styleable.CornerView_corner_strokeColor, KDefaultStrokeColor);

        Drawable d = a.getDrawable(R.styleable.CornerView_corner_foreground);
        setForeground(d);

        a.recycle();

        // 宽默认就使用1px就好
        mStrokeWidth = XmlAttrUtil.convert(mStrokeWidth, 1);
        // increment corner radius to account for half pixels.
        mRadius = XmlAttrUtil.convert(mRadius, KDefaultRadiusDp) + .5f;

        ViewUtil.disableHardwareAcc(this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isInEditMode()) {
            return;
        }

        set(getMeasuredWidth(), getMeasuredHeight());
    }

    private void set(int w, int h) {
        if (w == 0 || h == 0) {
            return;
        }

        if (mPathContent == null) {
            mPathContent = new Path();
        } else {
            mPathContent.reset();
        }

        if (mStrokeWidth != 0) {
            mRoundRectContent = new RectF(mStrokeWidth, mStrokeWidth, w - mStrokeWidth, h - mStrokeWidth);

            mRoundRectStroke = new RectF(0, 0, w, h);
            mPathStroke = new Path();
            mPathStroke.addRoundRect(mRoundRectStroke, mRadius, mRadius, Direction.CW);
        } else {
            mRoundRectContent = new RectF(0, 0, w, h);
        }
        mPathContent.addRoundRect(mRoundRectContent, mRadius, mRadius, Direction.CW);

        if (mForeground != null) {
            mForeground.setBounds(new Rect(0, 0, w, h));
        }
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);

        set((int) (getWidth() * scaleX), getHeight());
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);

        set(getWidth(), (int) (getHeight() * scaleY));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isInEditMode()) {
            super.dispatchDraw(canvas);
            return;
        }

        int saveCount = 0;
        if (mStrokeWidth != 0) {
            saveCount = canvas.save();
            canvas.clipPath(mPathStroke);
            canvas.drawColor(mStrokeColor);
            canvas.restoreToCount(saveCount);
        }

        saveCount = canvas.save();
        canvas.clipPath(mPathContent);
        super.dispatchDraw(canvas);

        if (mForeground != null) {
            mForeground.draw(canvas);
        }

        canvas.restoreToCount(saveCount);
    }

    public void setForeground(Drawable drawable) {
        if (ViewUtil.setForeground(this, mForeground, drawable)) {
            mForeground = drawable;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mForeground != null && mForeground.isStateful()) {
            mForeground.setState(getDrawableState());
        }
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();

        if (mForeground != null) {
            mForeground.jumpToCurrentState();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || (who == mForeground);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (mForeground != null) {
                    mForeground.setHotspot(e.getX(), e.getY());
                }
            }
        }
        return super.onTouchEvent(e);
    }
}
