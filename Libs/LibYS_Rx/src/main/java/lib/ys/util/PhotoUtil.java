package lib.ys.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

import java.util.ArrayList;
import java.util.List;

import lib.ys.ConstantsEx;
import lib.ys.action.IntentAction;
import lib.ys.action.IntentAction.PhotoAction.PhotoSource;

public class PhotoUtil {

    /**
     * 通过拍照选择照片
     *
     * @param host
     * @param path        照片保存地址
     * @param requestCode
     */
    public static void fromCamera(Object host, String path, int requestCode) {
        IntentAction.photo()
                .host(host)
                .path(path)
                .source(PhotoSource.camera)
                .code(requestCode)
                .launch();
    }

    /**
     * 从图库里选择照片 返回的数据在 intent.getData()里, 是Uri的形式
     *
     * @param host
     * @param requestCode
     */
    public static void fromAlbum(Object host, int requestCode) {
        IntentAction.photo()
                .host(host)
                .code(requestCode)
                .source(PhotoSource.album)
                .launch();
    }

    /**
     * <pre>
     * 从图库返回的图片uri里解析出正确的图片路径
     * 4.4(含)之前和之后, 方式会不一样
     * TODO: 网络代码暂时没有整理
     * 暂时用不着这么复杂的处理, 先保留代码
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = MediaColumns._ID + "=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaColumns.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static List<String> getPhotos(Context context) {
        List<String> images = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        //获取jpeg和jpg和png格式的文件，并且按照时间进行倒序
        Cursor cursor = contentResolver.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=\"image/jpeg\" or " +
                        MediaStore.Images.Media.MIME_TYPE + "=\"image/jpg\" or " + MediaStore.Images.Media.MIME_TYPE + "=\"image/png\"",
                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            String path = ConstantsEx.KEmpty;
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                images.add(path);
            }
            cursor.close();
        }
        return images;
    }
}
