import demo.damao.annotation.RpcScan;
import demo.damao.config.RpcServiceConfig;
import demo.damao.controller.HelloController;
import demo.damao.proxy.RpcClientProxy;
import demo.damao.remoting.transport.RpcRequestTransport;
import demo.damao.remoting.transport.netty.client.NettyRpcClient;
import dto.Hello;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/18 13:22
 */
@RpcScan(basePackage = {"demo.damao"})
public class NettyClientMain {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
//        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
//        helloController.test1();
//        helloController.test2();

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        RpcRequestTransport rpcRequestTransport = new NettyRpcClient();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("group1").version("version1").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport,rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        System.out.println(helloService.hello(new Hello("2","2")));
    }
}
