package demo.damao.remoting.transport.netty.client;

import demo.damao.constant.RpcConstant;
import demo.damao.enums.CompressTypeEnum;
import demo.damao.enums.SerializationTypeEnum;
import demo.damao.extension.ExtensionLoader;
import demo.damao.factory.SingletonFactory;
import demo.damao.registry.ServiceDiscovery;
import demo.damao.remoting.dto.RpcMessage;
import demo.damao.remoting.dto.RpcRequest;
import demo.damao.remoting.dto.RpcResponse;
import demo.damao.remoting.transport.RpcRequestTransport;
import demo.damao.remoting.transport.netty.codec.RpcMessageDecoder;
import demo.damao.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanggeng
 * @date 2021/8/18 13:28
 */
@Slf4j
public class NettyRpcClient implements RpcRequestTransport {
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        // 如果5s没有数据发到Server，在userEventTriggered发送心跳检测请求
                        channelPipeline.addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                        channelPipeline.addLast(new RpcMessageEncoder());
                        channelPipeline.addLast(new RpcMessageDecoder());
                        channelPipeline.addLast(new NettyRpcClientHandler());
                    }
                });
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 创建返回值
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 获得服务器地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        // 获得相关channel
        Channel channel = getChannel(inetSocketAddress);
        if(channel.isActive())
        {
            // 放入没处理请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                    .codec(SerializationTypeEnum.PROTOSTUFF.getCode())
                    .compress(CompressTypeEnum.GZIP.getCode())
                    .messageType(RpcConstant.REQUEST_TYPE).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if(future.isSuccess())
                {
                    log.info("client send message: [{}]", rpcMessage);
                }else
                {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        }
        else
        {
            throw new IllegalStateException();
        }
        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if(null == channel)
        {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    /**
     * connect server and get the channel ,so that you can send rpc message to server
     *
     * @param inetSocketAddress server address
     * @return the channel
     */
    @SneakyThrows
    private Channel doConnect(InetSocketAddress inetSocketAddress){
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
           if(future.isSuccess())
           {
               log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
               completableFuture.complete(future.channel());
           }else
           {
               throw new IllegalStateException();
           }
        });
        return completableFuture.get();
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
