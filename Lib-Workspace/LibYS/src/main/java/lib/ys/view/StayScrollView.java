package lib.ys.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * 专用来里面嵌套gridView和listView
 * 因为notifyDataSetChanged()会导致scrollView不能停留在顶部, 直接滑动定位到gridView所在
 * <p>
 * 解决方案:
 * 1. view.requestFocus(); 让界面顶部的某一个View获取focus
 * 2. grid.setFocusable(false); 让Grid不能获得focus
 * 3. 手动scrollTo()
 * 4. 重载computeScrollDeltaToGetChildRectOnScreen让其返回0 会导致ScrollView内布局产生变化时,不能正确滚动到focus child位置
 * <p>
 * 目前采用的是方法4, 都说方法1 2好用简单, 目前遇到一些布局不好用(猜测是focus设置冲突的问题)
 *
 * @author yuansui
 */
public class StayScrollView extends ObservableScrollView {

    public StayScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
