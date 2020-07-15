package priv.yolo.chestnut.example.netty.server.conf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 子通道流水线配置
 */
@Configuration
public class ChildHandlerConf {

    @Bean
    public ChannelHandler channelHandler() {
        // PS：ChannelInitializer中泛型参数与ServerBootstrap配置中步骤2设置通道类型对应，需要同步修改
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        // 装配流水线，可添加自定义
                        .addLast()
                ;
            }
        };
    }

}
