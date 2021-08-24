import demo.damao.annotation.RpcScan;
import demo.damao.config.RpcServiceConfig;
import demo.damao.controller.HelloController;
import demo.damao.proxy.RpcClientProxy;
import demo.damao.remoting.transport.RpcRequestTransport;
import demo.damao.remoting.transport.socket.SocketRpcClient;
import dto.Hello;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/16 16:35
 */
@RpcScan(basePackage = {"demo.damao"})
public class SocketClientMain {
    public static void main(String[] args) {
        /*RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);*/

        /*AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test1();
        helloController.test2();*/

        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("group1").version("version1").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);

        /*RpcServiceConfig rpcServiceConfig2 = RpcServiceConfig.builder()
                .group("group2").version("version2").build();
        RpcClientProxy rpcClientProxy2 = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig2);
        HelloService helloService2 = rpcClientProxy2.getProxy(HelloService.class);
        String hello2 = helloService2.hello(new Hello("111", "222"));
        System.out.println(hello2);*/
    }
}
