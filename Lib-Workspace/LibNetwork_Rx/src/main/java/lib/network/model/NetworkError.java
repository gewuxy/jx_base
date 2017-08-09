package lib.network.model;

import lib.processor.annotation.AutoBuilder;

/**
 * @author yuansui
 */
@AutoBuilder
public class NetworkError {

    // 错误提示文字
    String mMessage;
    // 错误码
    int mCode;
    // 实际错误
    Exception mException;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public Exception getException() {
        return mException;
    }

    @Override
    public String toString() {
        return "[message = " + mMessage + "], \r\n [code = " + mCode + "]";
    }
}