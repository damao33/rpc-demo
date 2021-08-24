package demo.damao.loadbalance;

import demo.damao.extension.SPI;
import demo.damao.remoting.dto.RpcRequest;

import java.util.List;

/**
 * Interface to the load balancing policy
 * 负载均衡接口
 *
 * @author shuang.kou
 * @createTime 2020年06月21日 07:44:00
 */
@SPI
public interface LoadBalance {
    /**
     * Choose one from the list of existing service addresses list
     *
     * @param serviceAddresses Service address list
     * @return target service address
     */
    String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);
}
