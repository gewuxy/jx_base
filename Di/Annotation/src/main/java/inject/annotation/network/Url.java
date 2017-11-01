package inject.annotation.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 地址声明
 * 会根据是否使用了{@link Url#value()}来决定使用动态的url地址还是指定的url地址作为host
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
    String value() default "";
}
