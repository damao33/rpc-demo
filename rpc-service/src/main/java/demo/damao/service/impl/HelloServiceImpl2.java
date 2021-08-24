package demo.damao.service.impl;

import dto.Hello;
import lombok.extern.slf4j.Slf4j;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/13 15:48
 */
@Slf4j

public class HelloServiceImpl2 implements HelloService {
    static {
        log.info("helloServiceImpl2 created");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl2收到: {}.", hello.getMessage());
        String result = "Hello2 description is " + hello.getDescription();
        log.info("HelloServiceImpl2返回: {}.", result);
        return result;
    }
}
