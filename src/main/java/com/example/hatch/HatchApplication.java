package com.example.hatch;

import com.example.hatch.config.ServerProperties;
import com.example.hatch.netty.MqttServer;
import com.example.hatch.netty.handler.MqttChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
@EnableConfigurationProperties(ServerProperties.class)
public class HatchApplication {

    @Autowired
    private MqttChannelInitializer mqttChannelInitializer;
    @Autowired
    private ServerProperties nettyProperties;

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(HatchApplication.class, args);
        MqttServer mqttServer = context.getBean(MqttServer.class);
        mqttServer.start();
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        // TODO: Add SSL encryption
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(mqttChannelInitializer);

        return serverBootstrap;
    }

    @Bean
    public InetSocketAddress serverSocketAddress() {
        return new InetSocketAddress(nettyProperties.getServerPort());
    }

}
