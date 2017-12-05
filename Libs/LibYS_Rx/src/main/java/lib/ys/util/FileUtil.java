package lib.ys.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import lib.ys.YSLog;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    private final static int KBufferSize = 4096;

    /**
     * 删除文件夹下所有内容包括文件夹本身
     *
     * @param folderPath
     */
    public static boolean delFolder(String folderPath) {
        try {
            delAllFiles(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File file = new File(filePath);
            return file.delete(); // 删除空文件夹
        } catch (Exception e) {
            YSLog.e(TAG, e);
            return false;
        }
    }

    /**
     * 删除指定File，支持目录和文件
     *
     * @param file
     */
    public static boolean delFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delFile(f);// 递归删除每一个文件
                }
            }

            return file.delete();// 删除该文件夹
        }
    }

    /**
     * 保留空文件夹, 只删除文件夹下的内容
     *
     * @param folderPath
     */
    public static boolean delOnlyFolderContained(String folderPath) {
        try {
            return delAllFiles(folderPath); // 删除完里面所有内容
        } catch (Exception e) {
            YSLog.e(TAG, e);
        }
        return false;
    }

    private static boolean delAllFiles(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        if (!file.isDirectory()) {
            return false;
        }

        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            } else if (temp.isDirectory()) {
                delAllFiles(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
            }
        }

        return true;
    }

    public static String inputStreamToString(InputStream in) throws Exception {

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[KBufferSize];
        int n;
        while ((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n));
        }
        // Log.i("String的长度", new Integer(out.length()).toString());
        return out.toString();
    }

    public static boolean saveFile(File file, String content) {

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(content.getBytes());
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static boolean ensureFileExist(String filePath) {
        File dirFile = new File(filePath);
        return ensureFileExist(dirFile);
    }

    public static boolean ensureFileExist(File file) {
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean copySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;

        } catch (Exception ex) {
            YSLog.d(TAG, ex);
            return false;
        }
    }

    public static void closeInStream(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            YSLog.d(TAG, e);
        }
    }

    public static void closeOutStream(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            YSLog.e(TAG, e);
        }
    }

    public static void closeReader(Reader reader) {
        try {

            if (reader != null) {
                reader.close();
            }

        } catch (IOException e) {
            YSLog.e(TAG, e);
        }
    }

    public static void closeWriter(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }

        } catch (IOException e) {
            YSLog.e(TAG, e);
        }
    }

    public static void writeObj(Object obj, File f) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(obj);

        } catch (Exception e) {
            YSLog.e(TAG, e);
        } finally {
            closeOutStream(oos);
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 完整的文件路径, 包括Sdcard路径
     * @return 文本txt
     * @throws Exception
     */
    public static String readFile(String filePath) throws Exception {
        File file = new File(filePath);
        return readFile(file);
    }

    public static String readFile(File file) throws Exception {
        FileInputStream stream = new FileInputStream(file);
        String data = inputStreamToString(stream);
        stream.close();
        return data;
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return 文件大小(字节)
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size += getFolderSize(fileList[i]);
            } else {
                size += fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(KBufferSize);
            byte[] b = new byte[KBufferSize];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            YSLog.e(TAG, "fileToBytes", e);
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile
     * @param filePath
     * @param fileName
     */
    public static void bytesToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            YSLog.e(TAG, "bytesToFile", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    YSLog.e(TAG, "bytesToFile", e1);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    YSLog.e(TAG, "bytesToFile", e1);
                }
            }
        }
    }
}