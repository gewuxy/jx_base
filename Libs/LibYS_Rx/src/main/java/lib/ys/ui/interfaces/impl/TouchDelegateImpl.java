package lib.ys.ui.interfaces.impl;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.InputDevice;
import android.view.MotionEvent;

import lib.ys.ui.interfaces.ITouchDelegate;

/**
 * @auther yuansui
 * @since 2017/10/12
 */

public class TouchDelegateImpl implements ITouchDelegate {

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            if (ev.isFromSource(InputDevice.SOURCE_MOUSE)
                    && ev.getAction() == MotionEvent.ACTION_DOWN
                    && ev.isButtonPressed(MotionEvent.BUTTON_PRIMARY)) {
                return true;
            }
        } else if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
            if (ev.isFromSource(InputDevice.SOURCE_MOUSE)
                    && ev.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return false;
    }
}
