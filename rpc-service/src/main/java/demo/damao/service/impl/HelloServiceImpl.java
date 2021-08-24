package demo.damao.service.impl;

import demo.damao.annotation.RpcService;
import dto.Hello;
import lombok.extern.slf4j.Slf4j;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/13 15:48
 */
@Slf4j
@RpcService(group = "group1", version = "version1")
public class HelloServiceImpl implements HelloService {
    static {
        log.info("helloServiceImpl created");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello1 description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
