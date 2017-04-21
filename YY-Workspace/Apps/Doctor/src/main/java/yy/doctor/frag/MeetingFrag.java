package yy.doctor.frag;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import lib.ys.LogMgr;
import lib.ys.util.view.LayoutUtil;
import lib.yy.frag.base.BaseFrag;
import yy.doctor.R;

/**
 * 会议界面
 *
 * @author CaiXiang
 * @since 2017/4/6
 */
public class MeetingFrag extends BaseFrag {

    private LinearLayout mLayoutTab;

    private static final int KUnderWay = 0;//进行中
    private static final int KNotStarted = 1;//未开始
    private static final int KRetrospect = 2;//精彩回顾

    private int LastViewType = -1;

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.frag_meeting;
    }

    @Override
    public void initNavBar() {
        View view = inflate(R.layout.layout_meeting_nav_bar);
        getNavBar().addViewMid(view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogMgr.e(TAG,"onClick");
            }
        });
    }

    @Override
    public void findViews() {
        mLayoutTab = findView(R.id.vp_header);
    }

    @Override
    public void setViewsValue() {
        addIndicators();
    }

    private void addIndicators() {

        addIndicator(KUnderWay, "进行中");
        addIndicator(KNotStarted, "未开始");
        addIndicator(KRetrospect, "精彩回顾");

    }

    private void addIndicator(final int index, CharSequence text) {

        View v = inflate(R.layout.layout_meeting_tab);

        final TextView tv = (TextView) v.findViewById(R.id.main_tab_tv);
        tv.setText(text);
        v.setTag(index);

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int type = (int) v.getTag();
                if (type != LastViewType) {
                    if (LastViewType != -1)
                        mLayoutTab.getChildAt(LastViewType).setSelected(false);
                    v.setSelected(true);
                    LastViewType = type;
                }
                LogMgr.e(TAG,tv.getText().toString().trim());
            }
        });

        if (index == KUnderWay) {
            v.setSelected(true);
            LastViewType = index;
        }

        fit(v);

        LinearLayout.LayoutParams p = LayoutUtil.getLinearParams(LayoutUtil.MATCH_PARENT, LayoutUtil.MATCH_PARENT);
        p.weight = 1;
        mLayoutTab.addView(v, p);
    }

}
