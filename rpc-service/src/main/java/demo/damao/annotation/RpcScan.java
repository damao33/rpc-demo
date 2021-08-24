package demo.damao.annotation;

import org.springframework.context.annotation.Import;
import demo.damao.spring.CustomScannerRegistrar;

import java.lang.annotation.*;

/**
 * @author zhanggeng
 * @date 2021/8/13 16:21
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();
}
