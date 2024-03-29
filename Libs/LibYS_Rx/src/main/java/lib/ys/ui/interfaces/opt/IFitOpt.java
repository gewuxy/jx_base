package lib.ys.ui.interfaces.opt;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 适配操作
 *
 * @author yuansui
 */
public interface IFitOpt {
    int MATCH_PARENT = LayoutParams.MATCH_PARENT;
    int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;

    int CENTER_HORIZONTAL = -11;
    int CENTER_VERTICAL = -12;

    /**
     * 自动设置所有的适配属性, 适用于以下view:<p>
     * {@link TextView}
     * {@link RelativeLayout}
     * {@link LinearLayout}
     * </p>
     *
     * @param v
     */
    void fit(View v);

    int fit(float dp);
}
