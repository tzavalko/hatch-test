package com.example.hatch.netty.handler;

import com.example.hatch.service.DataProcessor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.mqtt.MqttMessageType.*;

@Component
@ChannelHandler.Sharable
public class MqttHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttHandler.class);

    private DataProcessor<MqttMessage> dataProcessor;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MqttMessage message = (MqttMessage) msg;
        MqttMessageType messageType = message.fixedHeader().messageType();

        // TODO: Implement whole MQTT protocol.
        switch (messageType) {
            case CONNECT:
                // TODO: Authenticate client
                sendConnackResponse(ctx);
                break;
            case SUBSCRIBE:
                LOGGER.warn("Unexpected message type [{}]", messageType);
                ctx.close();
                break;
            case UNSUBSCRIBE:
                LOGGER.warn("Unexpected message type [{}]", messageType);
                ctx.close();
                break;
            case PUBLISH:
                dataProcessor.process(message);
                sendPubrecResponse(ctx);
                break;
            case DISCONNECT:
                ctx.close();
                break;
            default:
                LOGGER.warn("Unknown message type [{}]", messageType);
                break;
        }
    }

    private void sendConnackResponse(ChannelHandlerContext ctx) {
        // Just a mock to make it work.
        MqttFixedHeader header = new MqttFixedHeader(
                CONNACK,
                false,
                MqttQoS.EXACTLY_ONCE,
                false,
                0
        );
        MqttConnAckVariableHeader vh = new MqttConnAckVariableHeader(
                MqttConnectReturnCode.CONNECTION_ACCEPTED,
                true
        );
        MqttMessage m = MqttMessageFactory.newMessage(header, vh, null);
        ctx.write(m);
        ctx.flush();
    }

    private void sendPubrecResponse(ChannelHandlerContext ctx) {
        // Just a mock to make it work.
        MqttFixedHeader header = new MqttFixedHeader(
                PUBREC,
                false,
                MqttQoS.EXACTLY_ONCE,
                false,
                0
        );
        MqttMessageIdVariableHeader vh = MqttMessageIdVariableHeader.from(1);
        MqttMessage m = new MqttPubAckMessage(header, vh);
        ctx.write(m);
        ctx.flush();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage());

        ctx.close();
    }

    @Autowired
    public void setDataProcessor(DataProcessor<MqttMessage> dataProcessor) {
        this.dataProcessor = dataProcessor;
    }
}
