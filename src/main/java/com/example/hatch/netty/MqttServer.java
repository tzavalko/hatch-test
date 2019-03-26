package com.example.hatch.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class MqttServer {

    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress socketAddress;
    private Channel serverChannel;

    public MqttServer(ServerBootstrap serverBootstrap, InetSocketAddress socketAddress) {
        this.serverBootstrap = serverBootstrap;
        this.socketAddress = socketAddress;
    }

    public void start() throws Exception {
        serverChannel = serverBootstrap.bind(socketAddress).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }
}
