package yy.doctor.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import lib.yy.util.CountDown;
import lib.yy.util.CountDown.OnCountDownListener;
import yy.doctor.R;

/**
 * 带计时双提示的提示框
 *
 * @author : GuoXuan
 * @since : 2017/5/22
 */
public class HintDialogSec extends BaseHintDialog implements OnCountDownListener {

    private TextView mTvMainHint; // 主提示
    private TextView mTvSecHint; // 副提示
    private CountDown mCountDown;
    private String mCountHint;

    public HintDialogSec(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.dialog_hint_sec;
    }

    @Override
    public void setViews() {
        super.setViews();

        mTvMainHint = findView(R.id.dialog_sec_tv_main);
        mTvSecHint = findView(R.id.dialog_sec_tv_sec);
    }

    public void setMainHint(String hintMain) {
        mTvMainHint.setText(hintMain);
    }

    public void setSecHint(String hintSec) {
        mTvSecHint.setText(hintSec);
    }

    public void setCountHint(String hint) {
        mCountHint = hint;
    }

    public void start(long time) {
        mCountDown = new CountDown(time);
        mCountDown.setListener(this);
        mCountDown.start();
    }

    @Override
    public void onCountDown(long remainCount) {
        mTvSecHint.setText(remainCount + mCountHint);
        if (remainCount == 0) {
            dismiss();
        }
    }

    @Override
    public void onCountDownErr() {

    }

    @Override
    public void dismiss() {
        super.dismiss();

        if (mCountDown != null) {
            mCountDown.stop();
        }
    }
}