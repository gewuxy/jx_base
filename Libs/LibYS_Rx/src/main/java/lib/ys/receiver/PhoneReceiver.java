package lib.ys.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneReceiver extends BaseReceiver {

    private PhoneListener mPhoneListener;

    public PhoneReceiver(Context context) {
        super(context);
    }

    public void setPhoneListener(PhoneListener l) {
        mPhoneListener = l;
    }

    @Override
    public void register() {
        IntentFilter intentFilterPhone = new IntentFilter();
        intentFilterPhone.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        mContext.registerReceiver(this, intentFilterPhone);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是去电
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            // 设置一个监听器
            tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mPhoneListener != null) {
                        mPhoneListener.callStateRinging();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mPhoneListener != null) {
                        mPhoneListener.callStateIdle();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mPhoneListener != null) {
                        mPhoneListener.callStateOffHook();
                    }
                    break;
            }
        }
    };

    public interface PhoneListener {
        void callStateRinging();

        void callStateIdle();

        void callStateOffHook();
    }
} 