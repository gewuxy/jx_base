package lib.network.model;


import android.support.annotation.Nullable;

import lib.network.Network;
import lib.network.NetworkUtil;
import lib.network.model.pair.BytePairs;
import lib.network.model.pair.FilePairs;
import lib.network.model.pair.Pairs;

/**
 * Network任务实例
 *
 * @author yuansui
 */
public class NetworkReq {

    private Pairs mParams;
    private BytePairs mByteParams;
    private FilePairs mFileParams;

    private Pairs mHeaders;

    // 默认为get方式
    @NetworkMethod
    private int mMethod = NetworkMethod.get;

    private String mUrl;

    private String mDir;
    private String mFileName;

    private NetworkRetry mRetry;

    private NetworkReq() {
    }

    public NetworkRetry getRetry() {
        return mRetry;
    }

    public String getDir() {
        return mDir;
    }

    public String getFileName() {
        return mFileName;
    }

    public BytePairs getByteParams() {
        return mByteParams;
    }

    public FilePairs getFileParams() {
        return mFileParams;
    }

    public Pairs getHeaders() {
        return mHeaders;
    }

    public Pairs getParams() {
        return mParams;
    }

    @NetworkMethod
    public int getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public static Builder newBuilder(String baseUrl) {
        return new Builder(baseUrl);
    }

    /**
     * 内部builder
     */
    public static class Builder {
        private Pairs mParams;
        private BytePairs mByteParams;
        private FilePairs mFileParams;

        private Pairs mHeaders;

        private String mUrl;

        private String mDir;
        private String mFileName;

        @NetworkMethod
        private int mMethod = NetworkMethod.get;

        private NetworkRetry mRetry;


        protected Builder(String baseUrl) {
            mUrl = baseUrl;
            mParams = new Pairs();
        }

        public <T extends Builder> T get() {
            mMethod = NetworkMethod.get;
            return (T) this;
        }

        public <T extends Builder> T post() {
            mMethod = NetworkMethod.post;
            return (T) this;
        }

        public <T extends Builder> T upload() {
            mMethod = NetworkMethod.upload;
            return (T) this;
        }

        /**
         * 下载任务需要其他参数
         *
         * @param dir
         * @param fileName
         * @return
         */
        public <T extends Builder> T download(String dir, String fileName) {
            mMethod = NetworkMethod.download_file;
            mDir = dir;
            mFileName = fileName;
            return (T) this;
        }

        /**
         * 下载到内存中
         *
         * @param <T>
         * @return
         */
        public <T extends Builder> T download() {
            mMethod = NetworkMethod.download;
            return (T) this;
        }

        public <T extends Builder> T param(String name, String val) {
            mParams.add(name, val);
            return (T) this;
        }

        public <T extends Builder> T param(String name, boolean val) {
            mParams.add(name, String.valueOf(val));
            return (T) this;
        }

        public <T extends Builder> T param(String name, int val) {
            param(name, String.valueOf(val));
            return (T) this;
        }

        public <T extends Builder> T param(String name, long val) {
            param(name, String.valueOf(val));
            return (T) this;
        }

        public <T extends Builder> T param(@Nullable Pairs pairs) {
            if (pairs == null) {
                return (T) this;
            }
            mParams.add(pairs);
            return (T) this;
        }

        /**
         * 添加二进制param
         *
         * @param name
         * @param value
         */
        public <T extends Builder> T param(String name, byte[] value) {
            if (value == null) {
                return (T) this;
            }
            return param(name, value, NetworkUtil.KTextEmpty);
        }

        /**
         * 添加二进制param
         *
         * @param name
         * @param val
         * @param extend
         * @return
         */
        public <T extends Builder> T param(String name, byte[] val, String extend) {
            if (mByteParams == null) {
                mByteParams = new BytePairs();
            }
            mByteParams.add(name + extend, val);
            return (T) this;
        }

        /**
         * 添加文件param
         *
         * @param pair
         */
        public <T extends Builder> T param(FilePairs pair) {
            if (mFileParams == null) {
                mFileParams = new FilePairs();
            }
            mFileParams.add(pair);
            return (T) this;
        }

        /**
         * 添加header数据
         *
         * @param name
         * @param val
         */
        public <T extends Builder> T header(String name, String val) {
            if (mHeaders == null) {
                mHeaders = new Pairs();
            }
            mHeaders.add(name, val);
            return (T) this;
        }

        public <T extends Builder> T header(String name, int value) {
            header(name, String.valueOf(value));
            return (T) this;
        }

        public <T extends Builder> T header(@Nullable Pairs pairs) {
            if (mHeaders == null) {
                mHeaders = new Pairs();
            }
            mHeaders.add(pairs);
            return (T) this;
        }

        public <T extends Builder> T retry(int count, long delay) {
            mRetry = new NetworkRetry(count, delay);
            return (T) this;
        }

        public Pairs getParams() {
            return mParams;
        }

        public NetworkReq build() {
            // 先进行加密操作
            Network.getConfig().encrypt(this);

            NetworkReq r = new NetworkReq();

            r.mMethod = mMethod;
            r.mUrl = mUrl;

            r.mRetry = mRetry;

            r.mDir = mDir;
            r.mFileName = mFileName;

            r.mHeaders = mHeaders;
            r.mParams = mParams;
            r.mByteParams = mByteParams;
            r.mFileParams = mFileParams;

            return r;
        }

    }
}
