package lib.ys.form;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

import lib.ys.AppEx;
import lib.ys.ConstantsEx;
import lib.ys.YSLog;
import lib.ys.adapter.interfaces.IViewHolder;
import lib.ys.util.LaunchUtil;
import lib.ys.util.TextUtil;
import lib.ys.util.res.ResLoader;
import lib.ys.util.view.ViewUtil;

abstract public class FormEx<T extends FormEx<T, VH>, VH extends IViewHolder>
        implements OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    private String mName;
    private String mText;
    private String[] mTexts;

    private String mHint;
    private String mTips;

    private boolean mEnabled = true;
    private int mLimit = ConstantsEx.KInvalidValue;
    private Object mRelated;
    private Object mData;
    private Object mOption;
    private Object mHost;
    private int mColumn;
    private int mWidth;
    private int mHeight;
    private int mMode;
    private Intent mIntent;
    private List mChildren;
    private Object mDepend;
    private String mRegex;
    private boolean mCheck;
    private int mIndex;
    private int mId;
    private boolean mVisible = true;
    private OnFormObserver mObserver;

    @ColorInt
    private int mBgColor = ConstantsEx.KInvalidValue;

    @DrawableRes
    private int mDrawableId;

    @LayoutRes
    private int mLayoutId = ConstantsEx.KInvalidValue;

    private ColorStateList mTextColor;
    private ColorStateList mNameColor;

    private String mToast;

    /**
     * 和服务器通信的主要字段
     */
    private String mKey;
    private String mVal;


    private int mPosition;
    private OnFormViewClickListener mListener;
    private VH mHolder;


    public <FORM extends T> FORM name(String name) {
        mName = name;
        return (FORM) getThis();
    }

    public <FORM extends T> FORM key(String key) {
        mKey = key;
        return getThis();
    }

    public <FORM extends T> FORM val(String val) {
        mVal = val;
        return getThis();
    }

    public <FORM extends T> FORM name(@StringRes int id) {
        mName = ResLoader.getString(id);
        return getThis();
    }

    public <FORM extends T> FORM text(String text) {
        mText = text;
        return getThis();
    }

    public <FORM extends T> FORM texts(String... texts) {
        mTexts = texts;
        return getThis();
    }

    public <FORM extends T> FORM texts(@StringRes int... ids) {
        int len = ids.length;
        if (len == 0) {
            return getThis();
        }

        mTexts = new String[len];
        for (int i = 0; i < len; ++i) {
            mTexts[i] = ResLoader.getString(ids[i]);
        }

        return getThis();
    }

    public <FORM extends T> FORM text(@StringRes int id) {
        mText = ResLoader.getString(id);
        return getThis();
    }

    public <FORM extends T> FORM hint(String hint) {
        mHint = hint;
        return getThis();
    }

    public <FORM extends T> FORM hint(@StringRes int id) {
        mHint = ResLoader.getString(id);
        return getThis();
    }

    public <FORM extends T> FORM tips(String tips) {
        mTips = tips;
        return getThis();
    }

    public <FORM extends T> FORM tips(@StringRes int id) {
        mTips = ResLoader.getString(id);
        return getThis();
    }

    public <FORM extends T> FORM enable(boolean able) {
        mEnabled = able;
        return getThis();
    }

    public <FORM extends T> FORM related(Object related) {
        mRelated = related;
        return getThis();
    }

    public <FORM extends T> FORM data(Object data) {
        mData = data;
        return getThis();
    }

    public <FORM extends T> FORM option(Object option) {
        mOption = option;
        return getThis();
    }

    public <FORM extends T> FORM host(Object host) {
        mHost = host;
        return getThis();
    }

    public <FORM extends T> FORM width(int w) {
        mWidth = w;
        return getThis();
    }

    public <FORM extends T> FORM height(int h) {
        mHeight = h;
        return getThis();
    }

    public <FORM extends T> FORM column(int c) {
        mColumn = c;
        return getThis();
    }

    public <FORM extends T> FORM limit(int limit) {
        mLimit = limit;
        return getThis();
    }

    public <FORM extends T> FORM background(@ColorInt int color) {
        mBgColor = color;
        return getThis();
    }

    public <FORM extends T> FORM backgroundRes(@ColorRes int id) {
        mBgColor = ResLoader.getColor(id);
        return getThis();
    }

    public <FORM extends T> FORM mode(int mode) {
        mMode = mode;
        return getThis();
    }

    public <FORM extends T> FORM drawable(@DrawableRes int id) {
        mDrawableId = id;
        return getThis();
    }

    public <FORM extends T> FORM layout(@LayoutRes int id) {
        mLayoutId = id;
        return getThis();
    }

    public <FORM extends T> FORM textColorRes(@ColorRes int id) {
        mTextColor = ResLoader.getColorStateList(id);
        return getThis();
    }

    public <FORM extends T> FORM textColor(@ColorInt int color) {
        mTextColor = ColorStateList.valueOf(color);
        return getThis();
    }

    public <FORM extends T> FORM nameColor(@ColorInt int color) {
        mNameColor = ColorStateList.valueOf(color);
        return getThis();
    }

    public <FORM extends T> FORM toast(String toast) {
        mToast = toast;
        return getThis();
    }

    public <FORM extends T> FORM intent(Intent i) {
        mIntent = i;
        return getThis();
    }

    public <FORM extends T> FORM children(List children) {
        mChildren = children;
        return getThis();
    }

    public <FORM extends T> FORM depend(Object depend) {
        mDepend = depend;
        return getThis();
    }

    public <FORM extends T> FORM regex(String regex) {
        mRegex = regex;
        return getThis();
    }

    public <FORM extends T> FORM check(boolean check) {
        mCheck = check;
        return getThis();
    }

    public <FORM extends T> FORM index(int index) {
        mIndex = index;
        return getThis();
    }

    public <FORM extends T> FORM id(int id) {
        mId = id;
        return getThis();
    }

    public <FORM extends T> FORM visible(boolean visible) {
        mVisible = visible;
        return getThis();
    }

    public <FORM extends T> FORM observer(OnFormObserver observer) {
        mObserver = observer;
        return getThis();
    }

    public void setAttrs(VH holder, int position, OnFormViewClickListener listener) {
        mPosition = position;
        mListener = listener;
        mHolder = holder;

        init(holder);
        refresh();
    }

    public String getName() {
        return mName;
    }

    public String getText() {
        return mText;
    }

    public String[] getTexts() {
        return mTexts;
    }

    public String getHint() {
        return mHint;
    }

    public String getTips() {
        return mTips;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public int getLimit() {
        return mLimit;
    }

    public <T> T getRelated() {
        return (T) mRelated;
    }

    public <T> T getData() {
        return (T) mData;
    }

    public <T> T getOption() {
        return (T) mOption;
    }

    public Object getHost() {
        return mHost;
    }

    public int getColumn() {
        return mColumn;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getMode() {
        return mMode;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public List getChildren() {
        return mChildren;
    }

    public Object getDepend() {
        return mDepend;
    }

    public String getRegex() {
        return mRegex;
    }

    public boolean isCheck() {
        return mCheck;
    }

    public int getIndex() {
        return mIndex;
    }

    public int getId() {
        return mId;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public OnFormObserver getObserver() {
        return mObserver;
    }

    /**
     * 背景色
     *
     * @return otherwise return {@link ConstantsEx#KInvalidValue}
     */
    public int getBgColor() {
        return mBgColor;
    }

    public int getDrawable() {
        return mDrawableId;
    }

    /**
     * @return otherwise return {@link ConstantsEx#KInvalidValue}
     */
    public int getLayoutId() {
        return mLayoutId;
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public ColorStateList getNameColor() {
        return mNameColor;
    }

    public String getToast() {
        return mToast;
    }

    public String getKey() {
        return mKey;
    }

    public String getVal() {
        return mVal;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 刷新数据
     *
     * @param holder
     */
    abstract protected void refresh(VH holder);

    /**
     * 初始化设定
     *
     * @param holder
     */
    protected void init(VH holder) {
    }

    public final void refresh() {
        if (mHolder != null) {
            if (mVisible) {
                ViewUtil.showView(mHolder.getConvertView());
                refresh(mHolder);
            } else {
                ViewUtil.goneView(mHolder.getConvertView());
            }
        }
    }

    public final void show() {
        mVisible = true;
        refresh();
    }

    public final void hide() {
        mVisible = false;
        refresh();
    }

    public VH getHolder() {
        return mHolder;
    }

    abstract public boolean check();

    public boolean onItemClick(Object host, View v) {
        return false;
    }

    /**
     * 响应Activity的返回消息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, data);
    }

    protected void onActivityResult(int position, Intent data) {
        // 部分item无此消息响应
    }

    protected void startActivity(Intent intent) {
        LaunchUtil.startActivity(mHost, intent);
    }

    protected void startActivityForResult(Intent intent, int position) {
        LaunchUtil.startActivityForResult(mHost, intent, position);
    }

    protected void startActivityForResult(Class<?> clz, int position) {
        startActivityForResult(new Intent(getContext(), clz), position);
    }

    @Override
    public final void onClick(View v) {
        if (!onViewClick(v)) {
            if (mListener != null) {
                mListener.onViewClick(v, mPosition, mRelated);
            }
        }
    }

    /**
     * 内部处理view的点击事件
     *
     * @param v
     * @return true的话表示处理了, false表示外部处理
     */
    protected boolean onViewClick(View v) {
        return false;
    }

    protected void setOnClickListener(@NonNull View v) {
        if (v == null) {
            YSLog.d(TAG, "setOnClickListener()" + "v is null");
            return;
        }
        v.setOnClickListener(this);
    }

    protected void removeOnClickListener(@NonNull View v) {
        if (v == null) {
            YSLog.d(TAG, "removeOnClickListener()" + "v is null");
            return;
        }
        v.setClickable(false);
        v.setOnClickListener(null);
    }

    @LayoutRes
    abstract public int getContentViewResId();

    protected Context getContext() {
        return AppEx.getContext();
    }

    public void showToast(String content) {
        AppEx.showToast(content);
    }

    public void showToast(@StringRes int resId) {
        AppEx.showToast(resId);
    }

    @CallSuper
    public void save(String key, String val, String text) {
        mKey = key;
        mText = text;
        mVal = val;
    }

    @CallSuper
    public void save(String val, String text) {
        mText = text;
        mVal = val;
    }

    /**
     * 重设之前的值
     */
    public void reset() {
        save(ConstantsEx.KEmpty, ConstantsEx.KEmpty, ConstantsEx.KEmpty);
    }

    protected boolean isEmpty(String text) {
        return TextUtil.isEmpty(text);
    }

    protected <FORM extends T> FORM getThis() {
        return (FORM) this;
    }
}
