package com.example.hatch.netty.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mqttChannelInitializer")
public class MqttChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("mqttHandler")
    private ChannelInboundHandlerAdapter mqttHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new MqttDecoder());
        socketChannel.pipeline().addLast(MqttEncoder.INSTANCE);
        socketChannel.pipeline().addLast("MqttServerHandler", mqttHandler);
    }
}
