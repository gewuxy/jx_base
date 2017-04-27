package yy.doctor.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.Arrays;

import lib.ys.adapter.interfaces.OnRecyclerItemClickListener;
import lib.ys.ex.PopupWindowEx;
import lib.ys.fitter.DpFitter;
import lib.yy.view.GridDivider;
import yy.doctor.R;
import yy.doctor.adapter.SectionAdapter;

/**
 * @auther yuansui
 * @since 2017/4/26
 */

public class SectionPopup extends PopupWindowEx {
    private static final int KRowCount = 3; // 列数
    private static final int KDividerHeight = 14; // 分割线高度

    private String[] mSectionNames;


    private RecyclerView mRv;

    private OnSectionListener mLsn;

    public SectionPopup(@NonNull Context context, @Nullable OnSectionListener l) {
        super(context);
        mLsn = l;
    }

    @Override
    public void initData() {
        mSectionNames = getContext().getResources().getStringArray(R.array.sections);
        setTouchOutsideDismissEnabled(true);
        setDimEnabled(true);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.layout_meeting_select_section;
    }

    @Override
    public void findViews() {
        mRv = findView(R.id.meeting_layout_recyclerview);
    }

    @Override
    public void setViews() {

        mRv.setLayoutManager(new StaggeredGridLayoutManager(KRowCount, StaggeredGridLayoutManager.VERTICAL));

        mRv.addItemDecoration(new GridDivider(
                DpFitter.dp(KDividerHeight),
                R.drawable.section_divider_bg));

        final SectionAdapter adapter = new SectionAdapter();
        adapter.addAll(Arrays.asList(mSectionNames));
        mRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                if (mLsn != null) {
                    mLsn.onSectionSelected(adapter.getItem(position));
                }
                dismiss();
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        });

    }

    @Override
    public int getWindowWidth() {
        return MATCH_PARENT;
    }

    @Override
    public int getWindowHeight() {
        return WRAP_CONTENT;
    }

    public interface OnSectionListener {
        void onSectionSelected(String text);
    }
}
