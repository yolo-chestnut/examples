package priv.yolo.chestnut.example.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import priv.yolo.chestnut.example.netty.server.NettyServer;

@EnableConfigurationProperties
@SpringBootApplication
public class ExampleNettyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExampleNettyApplication.class, args);
    }

    private NettyServer nettyServer;

    @Autowired
    public ExampleNettyApplication(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void run(String... args) {
        nettyServer.start();
    }

}
