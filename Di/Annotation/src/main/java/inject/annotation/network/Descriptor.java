package inject.annotation.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明网络任务的描述类
 *
 * @auther yuansui
 * @since 2017/8/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Descriptor {
    /**
     * 正式线
     *
     * @return
     */
    String host();

    /**
     * 测试线
     *
     * @return
     */
    String hostDebuggable() default "";
}
