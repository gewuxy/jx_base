package lib.network.provider.ok.callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import lib.network.NetworkLog;
import lib.network.model.NetworkError;
import lib.network.model.interfaces.OnNetworkListener;
import lib.network.provider.NativeListener;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 返回的bytes直接存为文件
 * FIXME: 暂不支持断点续传
 *
 * @auther yuansui
 * @since 2017/6/11
 */

public class DownloadFileCallback extends OkCallback {

    private String mFileName;
    private String mFileDir;

    public DownloadFileCallback(OnNetworkListener l, String dir, String fileName) {
        super(l);

        mFileDir = dir;
        mFileName = fileName;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Integer id = (Integer) call.request().tag();

        RandomAccessFile raf = null;
        try {
            File dir = new File(mFileDir);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    NativeListener.inst().onError(id,
                            NetworkError.newBuilder().code(id).message("创建文件失败, 请检查权限").build(),
                            getListener());
                }
            }

            File targetFile = new File(mFileDir, mFileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }

            InputStream stream = response.body().byteStream();
            raf = new RandomAccessFile(targetFile, "rw");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = stream.read(bytes)) != -1) {
                if (call.isCanceled()) {
                    return;
                }
                raf.write(bytes, 0, len);
            }

            NativeListener.inst().onSuccess(id, null, getListener());
        } catch (Exception e) {
            NetworkLog.e(e);
            NativeListener.inst().onError(id,
                    NetworkError.newBuilder().code(id).exception(e).message(e.getMessage()).build(),
                    getListener());
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }
}
