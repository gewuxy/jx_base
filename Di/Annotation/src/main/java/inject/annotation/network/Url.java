package inject.annotation.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明为全路径
 *
 * @auther yuansui
 * @since 2017/8/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Url {
    /**
     * 指定为固定值
     *
     * @return
     */
    String assign() default "";
}
