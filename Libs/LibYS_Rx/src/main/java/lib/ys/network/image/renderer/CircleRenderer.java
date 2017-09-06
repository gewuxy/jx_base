package lib.ys.network.image.renderer;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;

import static android.R.attr.width;

/**
 * 圆形渲染器
 */
public class CircleRenderer implements Renderer {

    @ColorInt
    private int mBorderColor = Color.TRANSPARENT;

    private int mBorderWidth;

    public CircleRenderer() {
    }

    public CircleRenderer(@ColorInt int color, @IntRange(from = 0) int width) {
        mBorderWidth = width;
        mBorderColor = color;
    }

    public void setBorderColor(@ColorInt int color) {
        mBorderWidth = width;
    }

    public void setBorderWidth(@IntRange(from = 0) int width) {
        mBorderWidth = width;
    }

    @ColorInt
    public int getBorderColor() {
        return mBorderColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CircleRenderer) {
            CircleRenderer r = (CircleRenderer) o;
            if (r.getBorderColor() == mBorderColor
                    && r.getBorderWidth() == mBorderWidth) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return mBorderColor & mBorderWidth;
    }
}
