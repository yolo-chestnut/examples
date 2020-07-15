package priv.yolo.chestnut.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Netty Server
 * netty服务端启动流程（bootstrap 7步骤中固定部分）中固定部分
 */
@Slf4j
@Component
public class NettyServer {
    // 启动器
    private ServerBootstrap serverBootstrap;
    // 反应器线程组-boss组
    private EventLoopGroup boss;
    // 反应器线程组-worker组
    private EventLoopGroup worker;
    // 业务处理器
    private ChannelHandler channelHandler;

    @Autowired
    public NettyServer(ServerBootstrap serverBootstrap, EventLoopGroup boss, EventLoopGroup worker, ChannelHandler channelHandler) {
        this.serverBootstrap = serverBootstrap;
        this.boss = boss;
        this.worker = worker;
        this.channelHandler = channelHandler;
    }

    public void start() {
        serverBootstrap
                // 1. 设置反应器线程组
                .group(boss, worker)
                // 2. 设置通道类型，详见NettyServerConf
                // 3. 设置监听端口，详见NettyServerConf
                // 4. 设置通道参数，详见NettyServerConf
                // 5. 装配子通道流水线
                .childHandler(channelHandler)
        ;
        // 6. 绑定服务器（异步回调）
        ChannelFuture channelFuture = serverBootstrap.bind().addListener(future -> {
            if (future.isSuccess())
                log.info("Netty Server Start Successful.");
            else
                log.info("Netty Server Start Failed.", future.cause());
        });
        // 7. 通道关闭异步任务结束（异步回调）
        channelFuture.channel().closeFuture().addListener(future -> {
            if (future.isSuccess())
                log.info("Netty Server Stop Successful.");
            else
                log.info("Netty Server Stop Failed.", future.cause());
        });

        // 测试用，往JVM生命周期添加操作
        beforeClosedJVM();
    }

    // Spring钩子函数注解，Spring会把此方法添加到JVM钩子函数里，JVM多个钩子函数是并行执行的（顺序不保证！）
    @PreDestroy
    public void stopGracefully() throws InterruptedException {
        // PS：必须调用sync()同步方法，否则这两个异步任务还没执行完，有可能jvm都销毁了！
        Future<?> bossFuture = boss.shutdownGracefully().sync();
        Future<?> workerFuture = worker.shutdownGracefully().sync();
        if (bossFuture.isSuccess()) {
            log.info("Boss finished all tasks!!!");
        }
        if (workerFuture.isSuccess()) {
            log.info("Worker finished all tasks!!!");
        }
    }

    // 测试用，JVM钩子函数
    private void beforeClosedJVM() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                stopGracefully();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            log.info("JVM 钩子函数哎！");
        }, "jvm-test"));
    }

}
