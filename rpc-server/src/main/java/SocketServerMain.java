import demo.damao.annotation.RpcScan;
import demo.damao.config.RpcServiceConfig;
import demo.damao.remoting.transport.netty.server.NettyRpcServer;
import demo.damao.remoting.transport.socket.SocketRpcServer;
import demo.damao.service.impl.HelloServiceImpl;
import demo.damao.service.impl.HelloServiceImpl2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.HelloService;

/**
 * @author zhanggeng
 * @date 2021/8/16 14:53
 */
@RpcScan(basePackage = {"demo.damao"})
public class SocketServerMain {
    public static void main(String[] args) {
        HelloService helloService1 = new HelloServiceImpl();
        HelloService helloService2 = new HelloServiceImpl2();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        /*AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketServerMain.class);
        SocketRpcServer socketRpcServer = (SocketRpcServer) applicationContext.getBean("socketRpcServer");*/
        RpcServiceConfig rpcServiceConfig1 = RpcServiceConfig.builder()
                .service(helloService1)
                .group("group1").version("version1").build();
        RpcServiceConfig rpcServiceConfig2 = RpcServiceConfig.builder()
                .service(helloService2)
                .group("group2").version("version2").build();
        socketRpcServer.registerService(rpcServiceConfig1);
        socketRpcServer.registerService(rpcServiceConfig2);
        socketRpcServer.start();

        /*AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketServerMain.class);
        SocketRpcServer socketRpcServer = (SocketRpcServer) applicationContext.getBean("socketRpcServer");
        socketRpcServer.start();*/
    }
}
