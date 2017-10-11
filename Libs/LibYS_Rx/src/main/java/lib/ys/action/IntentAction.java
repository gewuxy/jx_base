package lib.ys.action;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.CallSuper;
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

    abstract static public class BaseAction {
        protected boolean mCreateChooser;
        protected String mChooserTitle;
        protected String mAlert;

        abstract public void launch();

        /**
         * 要求子类重写, 方便外部链式调用
         *
         * @param title
         * @param <T>
         * @return
         */
        @CallSuper
        public <T extends BaseAction> T createChooser(String title) {
            mCreateChooser = true;
            mChooserTitle = title;
            return (T) this;
        }

        /**
         * 要求子类重写, 方便外部链式调用
         *
         * @param a
         * @param <T>
         * @return
         */
        @CallSuper
        public <T extends BaseAction> T alert(String a) {
            mAlert = a;
            return (T) this;
        }

        /**
         * 以选择器的方式打开
         *
         * @param intent
         */
        protected void chooserLaunch(Intent intent) {
            LaunchUtil.startActivity(AppEx.ct(), Intent.createChooser(intent, mChooserTitle));
        }

        /**
         * 正常方式打开, 有可能没有安装相关应用
         *
         * @param intent
         */
        protected void normalLaunch(Intent intent) {
            try {
                LaunchUtil.startActivity(AppEx.ct(), intent);
            } catch (Exception e) {
                YSLog.e(TAG, "normalLaunch", e);
                if (mAlert != null) {
                    AppEx.showToast(mAlert);
                }
            }
        }
    }

    /**
     * 邮件
     */
    public static class MailAction extends BaseAction {

        private String mAddress;
        private String mSubject;
        private int mSubjectId;
        private String mText;

        private MailAction() {
        }

        public MailAction address(String a) {
            mAddress = a;
            return this;
        }

        public MailAction subject(String s) {
            mSubject = s;
            return this;
        }

        public MailAction subject(@StringRes int id) {
            mSubjectId = id;
            return this;
        }

        public MailAction text(String t) {
            mText = t;
            return this;
        }

        @Override
        public MailAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public MailAction alert(String a) {
            return super.alert(a);
        }

        @Override
        public void launch() {
            Intent intent = new Intent(Intent.ACTION_SEND)
                    .setType("plain/text") // don't use final define
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{mAddress})
                    .putExtra(Intent.EXTRA_TEXT, mText);
            if (TextUtil.isEmpty(mSubject)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, mSubjectId);
            } else {
                intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
            }

            if (mCreateChooser) {
                chooserLaunch(intent);
            } else {
                normalLaunch(intent);
            }
        }
    }

    /**
     * 外部浏览器
     */
    public static class BrowserAction extends BaseAction {

        private String mUrl;

        private BrowserAction() {
        }

        @Override
        public BrowserAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public BrowserAction alert(String a) {
            return super.alert(a);
        }

        public BrowserAction url(String url) {
            mUrl = url;
            return this;
        }

        @Override
        public void launch() {
            if (mUrl == null) {
                YSLog.e(TAG, "launch", new IllegalStateException("url can not be null"));
                return;
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(mUrl);
            intent.setData(uri);
            normalLaunch(intent);
        }
    }

    public static class MarketAction extends BaseAction {

        private MarketAction() {
        }

        @Override
        public MarketAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public MarketAction alert(String a) {
            return super.alert(a);
        }

        @Override
        public void launch() {
            Intent intent = getIntent();
            // 检测intent是否可用
            PackageManager packageManager = AppEx.ct().getPackageManager();
            List<ResolveInfo> shareAppList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (shareAppList == null || shareAppList.size() == 0) {
                AppEx.showToast(mAlert);
            } else {
                normalLaunch(intent);
            }
        }

        public static Intent getIntent() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + AppEx.ct().getPackageName()));
            return intent;
        }
    }

    public static class MapAction extends BaseAction {

        private double mLatitude;
        private double mLongitude;
        private String mName;

        private MapAction() {
        }

        @Override
        public MapAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public MapAction alert(String a) {
            return super.alert(a);
        }

        /**
         * 纬度
         *
         * @param latitude
         * @return
         */
        public MapAction latitude(double latitude) {
            mLatitude = latitude;
            return this;
        }

        /**
         * 经度
         */
        public MapAction longitude(double longitude) {
            mLongitude = longitude;
            return this;
        }

        /**
         * 地点名称
         *
         * @param name
         * @return
         */
        public MapAction name(String name) {
            mName = name;
            return this;
        }

        @Override
        public void launch() {
            StringBuffer buffer = new StringBuffer()
                    .append("geo:")
                    .append(mLatitude)
                    .append(",")
                    .append(mLongitude);

            if (TextUtil.isNotEmpty(mName)) {
                buffer.append("?q=")
                        .append(mName);
            }

            Uri mUri = Uri.parse(buffer.toString());
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            normalLaunch(mIntent);
        }
    }

    /**
     * 指定打开App的场景, 如支付宝, 微信
     */
    public static class AppAction extends BaseAction {

        private String mUrl;

        private AppAction() {
        }

        @Override
        public AppAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public AppAction alert(String a) {
            return super.alert(a);
        }

        public AppAction url(String url) {
            mUrl = url;
            return this;
        }

        @Override
        public void launch() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            normalLaunch(intent);
        }
    }

    public static class AnyAction extends BaseAction {

        private Intent mIntent;

        private AnyAction() {
        }

        @Override
        public AnyAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public AnyAction alert(String a) {
            return super.alert(a);
        }

        public AnyAction intent(Intent i) {
            mIntent = i;
            return this;
        }

        @Override
        public void launch() {
            normalLaunch(mIntent);
        }
    }

    /**
     * 调用第三方打开Word文件
     */
    public static class WordAction extends BaseAction {

        private String mFilePath;

        private WordAction() {
        }

        @Override
        public WordAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public WordAction alert(String a) {
            return super.alert(a);
        }

        public WordAction filePath(String filePath) {
            mFilePath = filePath;
            return this;
        }

        @Override
        public void launch() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/msword");
            normalLaunch(intent);
        }
    }

    /**
     * 调用第三方打开PPT文件
     */
    public static class PptAction extends BaseAction {

        private String mFilePath;

        private PptAction() {
        }

        @Override
        public PptAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public PptAction alert(String a) {
            return super.alert(a);
        }

        public PptAction filePath(String filePath) {
            mFilePath = filePath;
            return this;
        }

        @Override
        public void launch() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/vnd.ms-powerpoint");
            normalLaunch(intent);
        }
    }

    /**
     * 调用第三方打开excel文件
     */
    public static class ExcelAction extends BaseAction {

        private String mFilePath;

        public ExcelAction() {
        }

        @Override
        public ExcelAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public ExcelAction alert(String a) {
            return super.alert(a);
        }

        public ExcelAction filePath(String filePath) {
            mFilePath = filePath;
            return this;
        }

        @Override
        public void launch() {
            Uri uri = Uri.fromFile(new File(mFilePath));
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setDataAndType(uri, "application/vnd.ms-excel");
            normalLaunch(intent);
        }
    }

    public static class AppSetupAction extends BaseAction {

        @Override
        public AppSetupAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public AppSetupAction alert(String a) {
            return super.alert(a);
        }

        @Override
        public void launch() {
            Uri uri = Uri.parse("package:" + AppEx.ct().getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
            normalLaunch(intent);
        }
    }

    public static class PhoneCallAction extends BaseAction {

        private String mTellNum;

        public PhoneCallAction tellNum(String tellNum) {
            mTellNum = tellNum;
            return this;
        }

        @Override
        public PhoneCallAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public PhoneCallAction alert(String a) {
            return super.alert(a);
        }

        @Override
        public void launch() {
            Uri data = Uri.parse("tel:" + mTellNum);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(data);
            normalLaunch(intent);
        }
    }

    public static class PhotoAction extends BaseAction {
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
            return this;
        }

        public PhotoAction host(Object host) {
            mHost = host;
            return this;
        }

        public PhotoAction code(int code) {
            mCode = code;
            return this;
        }

        public PhotoAction source(@PhotoSource int s) {
            mSource = s;
            return this;
        }

        @Override
        public PhotoAction createChooser(String title) {
            return super.createChooser(title);
        }

        @Override
        public PhotoAction alert(String a) {
            return super.alert(a);
        }


        @Override
        public void launch() {
            if (mHost == null || mCode == -1 || mSource == PhotoSource.unknown) {
                AppEx.showToast("配置不正确");
                return;
            }

            if (mSource == PhotoSource.camera) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPath)));
                LaunchUtil.startActivityForResult(mHost, intent, mCode);
            } else {
                // 从图库里选择照片 返回的数据在 intent.getData()里, 是Uri的形式
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                LaunchUtil.startActivityForResult(mHost, intent, mCode);
            }
        }
    }
}
