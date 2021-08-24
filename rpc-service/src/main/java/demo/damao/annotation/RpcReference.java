package demo.damao.annotation;

import java.lang.annotation.*;

/**
 * @author zhanggeng
 * @date 2021/8/17 11:01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";
}
