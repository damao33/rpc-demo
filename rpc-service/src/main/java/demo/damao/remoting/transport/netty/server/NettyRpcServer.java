package demo.damao.remoting.transport.netty.server;


import demo.damao.config.CustomShutdownHook;
import demo.damao.config.RpcServiceConfig;
import demo.damao.factory.SingletonFactory;
import demo.damao.provider.ServiceProvider;
import demo.damao.provider.impl.ZkServiceProviderImpl;
import demo.damao.remoting.transport.netty.codec.RpcMessageDecoder;
import demo.damao.remoting.transport.netty.codec.RpcMessageEncoder;
import demo.damao.utils.RuntimeUtil;
import demo.damao.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanggeng
 * @date 2021/8/16 9:22
 */
@Slf4j
@Component
public class NettyRpcServer {
    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServerConfig){
        serviceProvider.publishService(rpcServerConfig);
    }

    @SneakyThrows
    public void start(){
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventLoopGroup serviceHandlerGroup = new DefaultEventLoopGroup(
                RuntimeUtil.cpus()*2,
                ThreadPoolFactoryUtils.createThreadFactory("service-handler-group",false)
        );
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            // 每30s一次心跳检测
                            channelPipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                            // TODO 处理传输的字节码(编码器、解码器)
                            channelPipeline.addLast(new RpcMessageEncoder());
                            channelPipeline.addLast(new RpcMessageDecoder());
                            channelPipeline.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                        }
                    });
            // 绑定端口，等待绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(host, PORT).sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }
}
