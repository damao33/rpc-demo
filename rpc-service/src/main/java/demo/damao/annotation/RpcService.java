package demo.damao.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zhanggeng
 * @date 2021/8/13 16:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface RpcService {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";
}
