package lib.network.model.interfaces;

import java.util.List;

import lib.network.model.NetworkError;

/**
 * @author yuansui
 */
public interface IResult<T> {

    void setData(T data);

    void setData(List<T> data);

    T getData();

    List<T> getList();

    void add(T item);

    String getLastId();

    void setLastId(String id);

    boolean isSucceed();

    void setMessage(String message);

    String getMessage();

    void setCode(int code);

    int getCode();

    int getCodeOk();

    NetworkError getError();

    void setError(NetworkError err);
}
