import demo.damao.annotation.RpcScan;
import demo.damao.config.RpcServiceConfig;
import demo.damao.remoting.transport.netty.server.NettyRpcServer;
import demo.damao.service.impl.HelloServiceImpl2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.HelloService;

/**
 * 通过Netty发布服务
 *
 * @author zhanggeng
 * @date 2021/8/13 16:08
 */
@RpcScan(basePackage = {"demo.damao"})
public class NettyServerMain {

    public static void main(String[] args) {
        // 通过注解注册服务
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        // 人工注册服务
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("group2").version("version2").service(helloService2).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
