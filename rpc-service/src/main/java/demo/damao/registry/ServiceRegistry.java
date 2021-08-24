package demo.damao.registry;

import demo.damao.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author zhanggeng
 * @date 2021/8/13 17:18
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
