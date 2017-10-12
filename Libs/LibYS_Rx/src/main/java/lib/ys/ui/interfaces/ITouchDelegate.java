package lib.ys.ui.interfaces;

import android.view.MotionEvent;

/**
 * @auther yuansui
 * @since 2017/10/12
 */

public interface ITouchDelegate {

    boolean onInterceptTouchEvent(MotionEvent ev);
}
