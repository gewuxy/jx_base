package lib.ys.action;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import java.io.File;
import java.util.List;

import lib.ys.AppEx;
import lib.ys.YSLog;
import lib.ys.util.LaunchUtil;
import lib.ys.util.TextUtil;

/**
 * @auther yuansui
 * @since 2017/6/30
 */

public class IntentAction {

    private static final String TAG = IntentAction.class.getSimpleName();

    /**
     * 邮件
     *
     * @return
     */
    public static MailAction mail() {
        return new MailAction();
    }

    /**
     * 外部浏览器
     *
     * @return
     */
    public static BrowserAction browser() {
        return new BrowserAction();
    }

    /**
     * 安卓市场
     *
     * @return
     */
    public static MarketAction market() {
        return new MarketAction();
    }

    /**
     * 地图
     *
     * @return
     */
    public static MapAction map() {
        return new MapAction();
    }

    /**
     * 指定App
     *
     * @return
     */
    public static AppAction app() {
        return new AppAction();
    }

    /**
     * 任意
     *
     * @return
     */
    public static AnyAction any() {
        return new AnyAction();
    }

    /**
     * word文档
     *
     * @return
     */
    public static WordAction word() {
        return new WordAction();
    }

    /**
     * ppt文档
     *
     * @return
     */
    public static PptAction ppt() {
        return new PptAction();
    }

    /**
     * excel表格
     *
     * @return
     */
    public static ExcelAction excel() {
        return new ExcelAction();
    }

    /**
     * app的设置
     *
     * @return
     */
    public static AppSetupAction appSetup() {
        return new AppSetupAction();
    }

    /**
     * 打电话
     *
     * @return
     */
    public static PhoneCallAction phoneCall() {
        return new PhoneCallAction();
    }

    /**
     * 选择照片
     *
     * @return
     */
    public static PhotoAction photo() {
        return new PhotoAction();
    }

    abstract static public class BaseAction<ACTION extends BaseAction<ACTION>> {
        private boolean mCreateChooser;
        private String mChooserTitle;
        private String mAlert;

        abstract public void launch();

        abstract public Intent getIntent();

        protected ACTION getThis() {
            return (ACTION) this;
        }

        public ACTION createChooser(String title) {
            mCreateChooser = true;
            mChooserTitle = title;
            return getThis();
        }

        public ACTION alert(String a) {
            mAlert = a;
            return getThis();
        }

        protected String getAlert() {
            return mAlert;
        }

        /**
         * 以选择器的方式打开
         *
         * @param intent
         */
        protected void chooserLaunch(Intent intent) {
            LaunchUtil.startActivity(Intent.createChooser(intent, mChooserTitle));
        }

        /**
         * 正常方式打开, 有可能没有安装相关应用
         *
         * @param intent
         */
        protected void normalLaunch(Intent intent) {
            try {
                LaunchUtil.startActivity(intent);
            } catch (Exception e) {
                YSLog.e(TAG, "normalLaunch", e);
                if (mAlert != null) {
                    AppEx.showToast(mAlert);
                }
            }
        }

        protected void autoLaunch(Intent intent) {
            if (mCreateChooser) {
                chooserLaunch(intent);
            } else {
                normalLaunch(intent);
            }
        }
    }

    /**
     * 邮件
     */
    public static class MailAction extends BaseAction<MailAction> {

        private String mAddress;
        private String mSubject;
        private int mSubjectId;
        private String mText;

        private MailAction() {
        }

        public MailAction address(String a) {
            mAddress = a;
            return getThis();
        }

        public MailAction subject(String s) {
            mSubject = s;
            return getThis();
        }

        public MailAction subject(@StringRes int id) {
            mSubjectId = id;
            return getThis();
        }

        public MailAction text(String t) {
            mText = t;
            return getThis();
        }

        @Override
        public void launch() {
            autoLaunch(getIntent());
        }

        @Override
        public Intent getIntent() {
            Intent intent = new Intent(Intent.ACTION_SEND)
                    .setType("plain/text") // don't use final define
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{mAddress})
                    .putExtra(Intent.EXTRA_TEXT, mText);
            if (TextUtil.isEmpty(mSubject)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, mSubjectId);
            } else {
                intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
            }
            return intent;
        }
    }

    /**
     * 外部浏览器
     */
    public static class BrowserAction extends BaseAction<BrowserAction> {

        private String mUrl;

        private BrowserAction() {
        }

        public BrowserAction url(String url) {
            mUrl = url;
            return getThis();
        }

        @Override
        public void launch() {
            if (mUrl == null) {
                YSLog.e(TAG, "launch", new IllegalStateException("url can not be null"));
                return;
            }
            normalLaunch(getIntent());
        }

        @Override
        public Intent getIntent() {
            return new Intent()
                    .setAction(Intent.ACTION_VIEW)
                    .setData(Uri.parse(mUrl));
        }
    }

    public static class MarketAction extends BaseAction<MarketAction> {

        private MarketAction() {
        }

        @Override
        public void launch() {
            Intent intent = getIntent();
            // 检测intent是否可用
            PackageManager packageManager = AppEx.ct().getPackageManager();
            List<ResolveInfo> shareAppList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (shareAppList == null || shareAppList.size() == 0) {
                AppEx.showToast(getAlert());
            } else {
                normalLaunch(intent);
            }
        }

        @Override
        public Intent getIntent() {
            return new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("market://details?id=" + AppEx.ct().getPackageName()));
        }
    }

    public static class MapAction extends BaseAction<MapAction> {

        private double mLatitude;
        private double mLongitude;
        private String mName;

        private MapAction() {
        }

        /**
         * 纬度
         *
         * @param latitude
         * @return
         */
        public MapAction latitude(double latitude) {
            mLatitude = latitude;
            return getThis();
        }

        /**
         * 经度
         */
        public MapAction longitude(double longitude) {
            mLongitude = longitude;
            return getThis();
        }

        /**
         * 地点名称
         *
         * @param name
         * @return
         */
        public MapAction name(String name) {
            mName = name;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            StringBuffer buffer = new StringBuffer()
                    .append("geo:")
                    .append(mLatitude)
                    .append(",")
                    .append(mLongitude);

            if (TextUtil.isNotEmpty(mName)) {
                buffer.append("?q=")
                        .append(mName);
            }

            Uri uri = Uri.parse(buffer.toString());
            return new Intent(Intent.ACTION_VIEW, uri);
        }

        @Override
        public void launch() {
            normalLaunch(getIntent());
        }
    }

    /**
     * 指定打开App的场景, 如支付宝, 微信
     */
    public static class AppAction extends BaseAction<AppAction> {

        private String mUrl;

        private AppAction() {
        }

        public AppAction url(String url) {
            mUrl = url;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        }

        @Override
        public void launch() {
            if (mUrl == null) {
                YSLog.e(TAG, "launch", new IllegalStateException("url can not be null"));
                AppEx.showToast("url不能为空");
                return;
            }
            normalLaunch(getIntent());
        }
    }

    public static class AnyAction extends BaseAction<AnyAction> {

        private Intent mIntent;

        private AnyAction() {
        }

        public AnyAction intent(Intent i) {
            mIntent = i;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            return mIntent;
        }

        @Override
        public void launch() {
            normalLaunch(mIntent);
        }
    }

    /**
     * 调用第三方打开Word文件
     */
    public static class WordAction extends BaseAction<WordAction> {

        private String mFilePath;

        private WordAction() {
        }

        public WordAction filePath(String filePath) {
            mFilePath = filePath;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            return new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/msword");
        }

        @Override
        public void launch() {
            if (mFilePath == null) {
                YSLog.d(TAG, "launch: file path can not be null");
                AppEx.showToast("文件路径不能为空");
                return;
            }
            normalLaunch(getIntent());
        }
    }

    /**
     * 调用第三方打开PPT文件
     */
    public static class PptAction extends BaseAction<PptAction> {

        private String mFilePath;

        private PptAction() {
        }

        public PptAction filePath(String filePath) {
            mFilePath = filePath;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            return new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/vnd.ms-powerpoint");
        }

        @Override
        public void launch() {
            if (mFilePath == null) {
                YSLog.d(TAG, "launch: file path can not be null");
                AppEx.showToast("文件路径不能为空");
                return;
            }
            normalLaunch(getIntent());
        }
    }

    /**
     * 调用第三方打开excel文件
     */
    public static class ExcelAction extends BaseAction<ExcelAction> {

        private String mFilePath;

        public ExcelAction() {
        }

        public ExcelAction filePath(String filePath) {
            mFilePath = filePath;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            return new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/vnd.ms-excel");
        }

        @Override
        public void launch() {
            if (mFilePath == null) {
                YSLog.d(TAG, "launch: file path can not be null");
                AppEx.showToast("文件路径不能为空");
                return;
            }
            normalLaunch(getIntent());
        }
    }

    public static class AppSetupAction extends BaseAction<AppSetupAction> {

        @Override
        public Intent getIntent() {
            Uri uri = Uri.parse("package:" + AppEx.ct().getPackageName());
            return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
        }

        @Override
        public void launch() {
            normalLaunch(getIntent());
        }
    }

    public static class PhoneCallAction extends BaseAction<PhoneCallAction> {

        private String mTellNum;

        public PhoneCallAction tellNum(String tellNum) {
            mTellNum = tellNum;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            Uri data = Uri.parse("tel:" + mTellNum);
            return new Intent(Intent.ACTION_DIAL)
                    .setData(data);
        }

        @Override
        public void launch() {
            normalLaunch(getIntent());
        }
    }

    public static class PhotoAction extends BaseAction<PhotoAction> {
        /**
         * 图片来源
         */
        @IntDef({
                PhotoSource.unknown,
                PhotoSource.camera,
                PhotoSource.album,
        })
        public @interface PhotoSource {
            int unknown = -1; // 未知
            int camera = 0; // 拍照
            int album = 1; // 相册
        }

        @PhotoSource
        private int mSource = PhotoSource.unknown;
        private String mPath;
        private Object mHost;
        private int mCode = -1;

        /**
         * 照片保存地址
         *
         * @param p
         * @return
         */
        public PhotoAction path(String p) {
            mPath = p;
            return getThis();
        }

        public PhotoAction host(Object host) {
            mHost = host;
            return getThis();
        }

        public PhotoAction code(int code) {
            mCode = code;
            return getThis();
        }

        public PhotoAction source(@PhotoSource int s) {
            mSource = s;
            return getThis();
        }

        @Override
        public Intent getIntent() {
            Intent intent = null;
            if (mSource == PhotoSource.camera) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPath)));
            } else {
                // 从图库里选择照片 返回的数据在 intent.getData()里, 是Uri的形式
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            return intent;
        }

        @Override
        public void launch() {
            if (mHost == null || mCode == -1 || mSource == PhotoSource.unknown) {
                AppEx.showToast("配置不正确");
                return;
            }
            LaunchUtil.startActivityForResult(mHost, getIntent(), mCode);
        }
    }
}
