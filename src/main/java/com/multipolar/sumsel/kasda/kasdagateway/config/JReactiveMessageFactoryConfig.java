package com.multipolar.sumsel.kasda.kasdagateway.config;

import com.github.kpavlov.jreactive8583.client.ClientConfiguration;
import com.github.kpavlov.jreactive8583.iso.ISO8583Version;
import com.github.kpavlov.jreactive8583.iso.J8583MessageFactory;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import com.solab.iso8583.parse.ConfigParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class JReactiveMessageFactoryConfig {

    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration
                .newBuilder()
                .replyOnError(false)
                .addEchoMessageListener(false)
                .addLoggingHandler(true)
                .encodeFrameLengthAsString(true)
                .frameLengthFieldLength(4)
//                .describeFieldsInLog(true)
                .logSensitiveData(true)
                .reconnectInterval(5 * 10000)
                .build();
    }

    @Bean
    public J8583MessageFactory<IsoMessage> messageFactory() throws IOException {
        MessageFactory<IsoMessage> messageFactory = ConfigParser.createFromClasspathConfig("iso-8583/j8583.xml");
        messageFactory.setCharacterEncoding(StandardCharsets.US_ASCII.name());
//        messageFactory.setEtx(-1);
        messageFactory.setForceStringEncoding(true);
        messageFactory.setForceSecondaryBitmap(true);
        messageFactory.setIgnoreLastMissingField(true);
        messageFactory.setVariableLengthFieldsInHex(false);
        messageFactory.setAssignDate(true);
        messageFactory.setUseBinaryBitmap(false);
        messageFactory.setUseBinaryMessages(false);
        messageFactory.setBinaryHeader(false);
        messageFactory.setTraceNumberGenerator(
                new SimpleTraceGenerator((int) (System.currentTimeMillis() % 1000000))
        );
        return new J8583MessageFactory<>(messageFactory, ISO8583Version.V1987);
    }

}
