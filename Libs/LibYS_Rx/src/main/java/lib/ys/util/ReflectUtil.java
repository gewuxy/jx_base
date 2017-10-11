package lib.ys.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import lib.ys.YSLog;

/**
 * 反射类
 *
 * @author yuansui
 */
public class ReflectUtil {

    private static final String TAG = ReflectUtil.class.getSimpleName();

    /**
     * 通过反射获取类的对象, 只通过public的构造方法获取
     * <p>
     * 通过循环的方式构造对象, 因为构造函数的数量还是非常小的, 不会增加多少开销
     * 无法通过传统的方式封装此方法, 因为需要传两个多变参数
     * ({@link Class#getConstructor(Class...)} 和 {@link Constructor#newInstance(Object...)}都需要)
     * 而且不能通过Object的getClass获取到对应构造里需要的参数class而导致无法匹配
     * </p>
     *
     * @param clz  目标类
     * @param args 构造参数
     * @param <T>  任意类型
     * @return
     */
    public static <T> T newInst(Class<T> clz, Object... args) {
        if (clz == null) {
            return null;
        }

        T t = null;
        Constructor<?>[] cs = clz.getConstructors();
        for (int i = 0; i < cs.length; ++i) {
            try {
                t = (T) cs[i].newInstance(args);
                break;
            } catch (Exception e) {
                YSLog.e(TAG, "newInst", e);
                continue;
            }
        }

        return t;
    }

    /**
     * 通过反射获取类的对象, 任意构造方法获取, 包括private
     *
     * @param clz
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T newDeclaredInst(Class<T> clz, Object... args) {
        if (clz == null) {
            return null;
        }

        T t = null;
        Constructor<?>[] cs = clz.getDeclaredConstructors();
        for (int i = 0; i < cs.length; ++i) {
            try {
                cs[i].setAccessible(true);
                t = (T) cs[i].newInstance(args);
                break;
            } catch (Exception e) {
                YSLog.e(TAG, "newDeclaredInst", e);
                continue;
            }
        }
        return t;
    }

    public static Method getMethod(String className, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException, NoSuchMethodException {
        return Class.forName(className).getMethod(methodName, parameterTypes);
    }
}
