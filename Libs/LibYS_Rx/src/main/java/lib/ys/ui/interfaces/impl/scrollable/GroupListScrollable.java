package lib.ys.ui.interfaces.impl.scrollable;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import lib.ys.adapter.MultiGroupAdapterEx.OnChildAdapterClickListener;
import lib.ys.adapter.MultiGroupAdapterEx.OnGroupAdapterClickListener;
import lib.ys.adapter.interfaces.IGroupAdapter;
import lib.ys.ui.interfaces.listener.scrollable.OnGroupListScrollableListener;
import lib.ys.util.UIUtil;
import lib.ys.view.GroupListView;

/**
 * group list操作
 *
 * @author yuansui
 */
public class GroupListScrollable<GROUP, CHILD, A extends IGroupAdapter<GROUP, CHILD>>
        extends ListScrollable<GROUP, GroupListView, A>
        implements OnGroupClickListener, OnChildClickListener {

    private OnGroupListScrollableListener<GROUP, CHILD, A> mListener;

    public GroupListScrollable(@NonNull OnGroupListScrollableListener<GROUP, CHILD, A> l) {
        super(l);
        mListener = l;
    }

    @Override
    public void setViews() {
        createAdapter();

        // 不能调用super, 因为adapter类型不同无法进行相同的设置
        UIUtil.setOverScrollNever(getScrollableView());

        getScrollableView().setAdapter((ExpandableListAdapter) getAdapter());
        getScrollableView().setOnItemClickListener(this);

        if (mListener.enableLongClick()) {
            getScrollableView().setOnItemLongClickListener(this);
        }

        if (!mListener.needDelayAddEmptyView()) {
            addEmptyViewIfNonNull();
        }

        getScrollableView().setOnGroupClickListener(this);
        getScrollableView().setOnChildClickListener(this);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        getScrollableView().setOnScrollListener(listener);
    }

    public void setSelectedGroup(int groupPosition) {
        getScrollableView().setSelectedGroup(groupPosition);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return mListener.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return mListener.onGroupClick(parent, v, groupPosition, id);
    }

    public GROUP getGroup(int groupPosition) {
        return getAdapter().getGroup(groupPosition);
    }

    public CHILD getChild(int groupPosition, int childPosition) {
        return getAdapter().getChild(groupPosition, childPosition);
    }

    public boolean isGroupExpanded(int groupPosition) {
        return getScrollableView().isGroupExpanded(groupPosition);
    }

    public void expandAllGroup() {
        for (int i = 0; i < getAdapter().getGroupCount(); ++i) {
            getScrollableView().expandGroup(i);
        }
    }

    public void collapseAllGroup() {
        for (int i = 0; i < getAdapter().getGroupCount(); ++i) {
            getScrollableView().collapseGroup(i);
        }
    }

    public void collapseGroup(int groupPos) {
        getScrollableView().collapseGroup(groupPos);
    }

    public void setExpandSingle() {
        getScrollableView().setOnGroupExpandListener(position -> {
            for (int i = 0; i < getAdapter().getGroupCount(); ++i) {
                if (i != position) {
                    if (isGroupExpanded(i)) {
                        collapseGroup(i);
                    }
                }
            }
        });
    }

    public int getGroupCount() {
        return getAdapter().getGroupCount();
    }

    public int getChildrenCount(int groupPosition) {
        return getAdapter().getChildrenCount(groupPosition);
    }

    public void expandGroup(int groupPos) {
        getScrollableView().expandGroup(groupPos);
    }

    public void setOnGroupAdapterClickListener(OnGroupAdapterClickListener listener) {
        getAdapter().setOnGroupAdapterClickListener(listener);
    }

    public void setOnChildAdapterClickListener(OnChildAdapterClickListener listener) {
        getAdapter().setOnChildAdapterClickListener(listener);
    }

    public void setFloatingGroupEnabled(boolean enable) {
        getScrollableView().setFloatingGroupEnabled(enable);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        long pos = getScrollableView().getExpandableListPosition(position);
        int type = ExpandableListView.getPackedPositionType(pos);

        int groupPos = ExpandableListView.getPackedPositionGroup(pos);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int childPos = ExpandableListView.getPackedPositionChild(pos);
            mListener.onChildLongClick(groupPos, childPos);
        } else {
            mListener.onGroupLongClick(groupPos);
        }

        return true;
    }
}
