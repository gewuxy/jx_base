package lib.ys.network.result;

import java.util.ArrayList;
import java.util.List;

import lib.network.model.NetworkError;
import lib.network.model.interfaces.IResult;

abstract public class ResultEx<T> implements IResult<T> {

    private int mCode;
    private String mMessage;
    private NetworkError mError;

    private T mT;
    private List<T> mTs;
    private String mLastId;


    public ResultEx() {
    }

    public ResultEx(T data) {
        mT = data;
    }

    @Override
    public void setData(T data) {
        mT = data;
    }

    @Override
    public void setData(List<T> data) {
        mTs = data;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            return;
        }

        if (mTs == null) {
            mTs = new ArrayList<>();
        }
        mTs.add(item);
    }

    @Override
    public T getData() {
        return mT;
    }

    @Override
    public List<T> getList() {
        return mTs;
    }

    @Override
    public String getLastId() {
        return mLastId;
    }

    @Override
    public void setLastId(String id) {
        mLastId = id;
    }

    @Override
    public void setCode(int code) {
        mCode = code;
    }

    @Override
    public boolean isSucceed() {
        return mCode == getCodeOk();
    }

    @Override
    public int getCode() {
        return mCode;
    }

    @Override
    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    @Override
    public NetworkError getError() {
        if (mError == null) {
            mError = NetworkError.newBuilder()
                    .code(mCode)
                    .message(mMessage)
                    .build();
        }
        return mError;
    }

    @Override
    public void setError(NetworkError err) {
        mError = err;
    }

    @Override
    abstract public int getCodeOk();
}
