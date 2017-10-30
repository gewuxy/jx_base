package lib.ys.view.pager.transformer;

import android.view.View;

/**
 * @author CaiXiang
 * @since 2017/10/24
 */

public class ScaleTransformer extends BaseTransformer {

    private float mMinScale;
    private float mScale;

    public ScaleTransformer(float scale) {
        mScale = scale;
        mMinScale = 1 - scale;
    }

    @Override
    protected void onLeft(View v, float position) {
        v.setScaleX(mMinScale);
        v.setScaleY(mMinScale);
    }

    @Override
    protected void onTurn(View v, float position) {
        if (position < 0) {
            float scale = 1 + mScale * position;
            v.setScaleX(scale);
            v.setScaleY(scale);
        } else {
            float scale = 1 - mScale * position;
            v.setScaleX(scale);
            v.setScaleY(scale);
        }
    }

    @Override
    protected void onRight(View v, float position) {
        v.setScaleX(mMinScale);
        v.setScaleY(mMinScale);
    }
}
