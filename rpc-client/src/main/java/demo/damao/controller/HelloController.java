package demo.damao.controller;

import demo.damao.annotation.RpcReference;
import demo.damao.service.impl.HelloServiceImpl;
import demo.damao.service.impl.HelloServiceImpl2;
import dto.Hello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/17 11:03
 */
@Slf4j
@Component
public class HelloController {

    @RpcReference(version = "version1", group = "group1")
    private HelloService helloService1;
    @RpcReference(version = "version2", group = "group2")
    private HelloService helloService2;

    public void test1(){
        System.out.println(helloService1.hello(new Hello("hello1","111")));
    }

    public void test2(){
        System.out.println(helloService2.hello(new Hello("hello2","222")));
    }
}
