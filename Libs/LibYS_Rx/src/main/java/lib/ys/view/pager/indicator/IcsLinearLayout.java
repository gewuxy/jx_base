package lib.ys.view.pager.indicator;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * A simple extension of a regular linear layout that supports the divider API
 * of Android 4.0+. The dividers are added adjacent to the children by changing
 * their layout params. If you need to rely on the margins which fall in the
 * same orientation as the layout you should wrap the child in a simple
 * {@link android.widget.FrameLayout} so it can receive the margin.
 */

/**
 * unuse
 */
class IcsLinearLayout extends LinearLayout {
    public IcsLinearLayout(Context context, int attr) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }
}
