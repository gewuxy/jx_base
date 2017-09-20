package lib.ys.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import lib.ys.ConstantsEx;
import lib.ys.R;
import lib.ys.YSLog;
import lib.ys.util.XmlAttrUtil;
import lib.ys.util.view.LayoutUtil;


public class RatingView extends LinearLayout implements OnClickListener {

    private static final int KDefaultStartGapDp = 1;
    private static final int KDefaultStartSizeDp = 15;

    private int mRating = 0;
    private int mDrawableLightResId;
    private int mDrawableHalfResId;
    private int mDrawableDarkResId;
    private float mStarNumber;
    private int mStarGap;
    private int mStarSize;

    private boolean mTouchable;

    private OnRatingViewSelectListener mListener;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
        mStarNumber = typeArray.getFloat(R.styleable.RatingView_rating_starNum, 0f);
        mStarGap = typeArray.getDimensionPixelOffset(R.styleable.RatingView_rating_starGap, ConstantsEx.KInvalidValue);
        mStarSize = typeArray.getDimensionPixelOffset(R.styleable.RatingView_rating_starSize, ConstantsEx.KInvalidValue);

        mDrawableLightResId = typeArray.getResourceId(R.styleable.RatingView_rating_drawableLight, 0);
        mDrawableDarkResId = typeArray.getResourceId(R.styleable.RatingView_rating_drawableDark, 0);
        mDrawableHalfResId = typeArray.getResourceId(R.styleable.RatingView_rating_drawableHalf, 0);
        mRating = typeArray.getInt(R.styleable.RatingView_rating_num, 0);

        typeArray.recycle();
        // 做适配
        mStarGap = XmlAttrUtil.convert(mStarGap, KDefaultStartGapDp);
        mStarSize = XmlAttrUtil.convert(mStarSize, KDefaultStartSizeDp);

        init();
    }

    private void init() {
        createStarViewByNumStar();
        freshStarViewByRating();
    }

    private void createStarViewByNumStar() {
        ImageView iv = null;
        LayoutParams params = null;
        for (int i = 0; i < mRating; i++) {
            iv = new ImageView(getContext());
            if (mStarSize != 0) {
                params = LayoutUtil.getLinearParams(mStarSize, mStarSize);
            } else {
                params = LayoutUtil.getLinearParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            if (i < mRating - 1) {
                params.rightMargin = mStarGap;
            }

            iv.setOnClickListener(this);
            addView(iv, params);
        }
    }

    private void freshStarViewByRating() {
        ImageView view = null;
        int ratingInt = Math.round(mStarNumber);
        YSLog.d("www", "ratingInt = " + mStarNumber);
        boolean half = ratingInt > mStarNumber;
        YSLog.d("www", "half = " + half);
        if (half) {
            ratingInt -= 1;
        }

        for (int i = 0; i < mRating; i++) {
            view = (ImageView) getChildAt(i);
            if (i < ratingInt) {
                view.setImageResource(mDrawableLightResId);
            } else if (half && i == ratingInt) {
                view.setImageResource(mDrawableHalfResId);
            } else {
                view.setImageResource(mDrawableDarkResId);
            }
        }
    }

    public void setRating(int rating) {
        if (rating < 0) {
            rating = 0;
        }
        if (rating == mStarNumber) {
            return;
        }

        mStarNumber = rating;
        freshStarViewByRating();
    }

    /**
     * 设置是否可以通过点击来设置rating
     *
     * @param touchable
     */
    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    @Override
    public void onClick(View v) {
        if (!mTouchable) {
            return;
        }

        for (int i = 0; i < getChildCount(); ++i) {
            if (v.equals(getChildAt(i))) {
                int rating = i + 1;
                setRating(rating);
                if (mListener != null) {
                    mListener.onRatingSelected(rating);
                }
                break;
            }
        }
    }

    public int getRating() {
        return mRating;
    }

    public void setListener(OnRatingViewSelectListener listener) {
        mListener = listener;
    }

    public interface OnRatingViewSelectListener {
        void onRatingSelected(int rating);
    }
}
