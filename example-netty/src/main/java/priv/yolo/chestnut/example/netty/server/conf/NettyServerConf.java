package priv.yolo.chestnut.example.netty.server.conf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Netty Server Conf
 * netty服务端启动流程（bootstrap 7步骤中可变动部分）中可变动部分
 */
// lombok相关注解放在@ConfigurationProperties前面。
// 有个有趣的现象，若lombok相关注解放在后面，重新编译yml会提示没有定义，spring-configuration-metadata.json文件生成不及时（需要多编译几次）。
// 推测跟annotation processor加载顺序有关？
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "netty-server.conf")
@Configuration
public class NettyServerConf {

    private int port = 6666;

    private int bossThreads;

    private int workerThreads;

    @Bean
    public EventLoopGroup boss() {
        // PS：与通道类型相关
        return new NioEventLoopGroup(bossThreads);
    }

    @Bean
    public EventLoopGroup worker() {
        // PS：与通道类型相关
        return new NioEventLoopGroup(workerThreads);
    }

    @Bean
    public ServerBootstrap serverBootstrap() {
        log.info("Netty Server 绑定的端口为：{}", port);
        return new ServerBootstrap()
                // 2. 设置通道类型
                // PS：通道类型与boss、worker相关，需要同步修改
                .channel(NioServerSocketChannel.class)
                // 3. 设置监听端口
                .localAddress(port)
                // 4. 设置通道参数
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                ;
    }

}
