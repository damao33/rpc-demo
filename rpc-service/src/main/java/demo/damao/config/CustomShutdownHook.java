package demo.damao.config;

import demo.damao.remoting.transport.netty.server.NettyRpcServer;
import demo.damao.utils.CuratorUtils;
import demo.damao.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * When the server is closed, do something such as unregister all services
 *
 * @author zhanggeng
 * @date 2021/8/16 10:24
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook(){
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll(){
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                log.info("shutdown hook");
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcServer.PORT);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(),inetSocketAddress);
            } catch (UnknownHostException ignore) {
                log.info(ignore.toString());
            }
            ThreadPoolFactoryUtils.shutdownAllThreadPool();
        }));
    }
}
