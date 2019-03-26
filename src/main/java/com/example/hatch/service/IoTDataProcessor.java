package com.example.hatch.service;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
public class IoTDataProcessor implements DataProcessor<MqttMessage> {

    @Override
    public void process(MqttMessage message) {
        // TODO: Save data into DB
        MqttPublishVariableHeader header = (MqttPublishVariableHeader) message.variableHeader();
        String topicName = header.topicName();
        ByteBuf payload = (ByteBuf) message.payload();
        String data = payload.toString(Charset.defaultCharset());
        System.out.println("Data processed");
        System.out.println(String.format("Topic [%s], message [%s]", topicName, data));
    }
}
