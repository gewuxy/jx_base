package lib.ys.fitter;

import android.support.annotation.DimenRes;
import android.view.View;

/**
 * @auther yuansui
 * @since 2017/11/20
 */
public class Fitter {

    /**
     * 适配dp
     *
     * @param dp
     * @return px
     */
    public static int dp(float dp) {
        return DpFitter.dp(dp);
    }

    public static float densityPx(float px) {
        return DpFitter.densityPx(px);
    }

    public static int densityPx(int px) {
        return (int) densityPx((float) px);
    }

    /**
     * 适配dimen资源
     *
     * @param dimenResId
     * @return px
     */
    public static int dimen(@DimenRes int dimenResId) {
        return DpFitter.dimen(dimenResId);
    }

    /**
     * 适配view
     *
     * @param v
     */
    public static void view(View v) {
        LayoutFitter.fit(v);
    }

    public static float getDensity() {
        return DpFitter.getDensity();
    }

    public static void reset() {
        LayoutFitter.clearFitSet();
    }
}
