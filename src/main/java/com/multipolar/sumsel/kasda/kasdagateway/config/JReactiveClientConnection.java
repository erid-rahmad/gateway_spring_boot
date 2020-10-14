package com.multipolar.sumsel.kasda.kasdagateway.config;

import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.github.kpavlov.jreactive8583.client.ClientConfiguration;
import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.github.kpavlov.jreactive8583.iso.J8583MessageFactory;
import com.github.kpavlov.jreactive8583.iso.MTI;
import com.multipolar.sumsel.kasda.kasdagateway.dto.ValidateSourceAccountDto;
import com.multipolar.sumsel.kasda.kasdagateway.mapper.IsoMessageMapper;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoValue;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

import static com.github.kpavlov.jreactive8583.iso.ISO8583Version.V1987;
import static com.github.kpavlov.jreactive8583.iso.MessageClass.FINANCIAL;
import static com.github.kpavlov.jreactive8583.iso.MessageFunction.REQUEST_RESPONSE;
import static com.github.kpavlov.jreactive8583.iso.MessageOrigin.ACQUIRER;

@Slf4j
@Configuration
public class JReactiveClientConnection implements InitializingBean, DisposableBean {

    private final String socketConnectionHost;
    private final Integer socketConnectionPort;
    private final ClientConfiguration clientConfiguration;
    private final J8583MessageFactory<IsoMessage> messageFactory;
    Iso8583Client<IsoMessage> client;

    @Autowired
    private IsoMessageMapper mapper;

    @Autowired
    public JReactiveClientConnection(
            @Value("${socket.hostname}") String socketConnectionHost,
            @Value("${socket.port}") Integer socketConnectionPort,
            @Qualifier("clientConfiguration") ClientConfiguration clientConfiguration,
            @Qualifier("messageFactory") J8583MessageFactory<IsoMessage> messageFactory) {
        this.socketConnectionHost = socketConnectionHost;
        this.socketConnectionPort = socketConnectionPort;
        this.clientConfiguration = clientConfiguration;
        this.messageFactory = messageFactory;
    }

    @Override
    public void destroy() throws Exception {
        this.client.disconnectAsync();
        log.info("socket client: disconnected");
    }

    @Bean
    public Iso8583Client<IsoMessage> clientConnection() {
        return this.client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InetSocketAddress socket = new InetSocketAddress(this.socketConnectionHost, this.socketConnectionPort);
        this.client = new Iso8583Client<>(socket, clientConfiguration, messageFactory);
        this.client.addMessageListener(new IsoMessageListener<IsoMessage>() {
            @Override
            public boolean applies(@NotNull IsoMessage message) {
                log.info("iso message response ==> {messageType: {}, field3: {}}",
                        String.format("%04x", message.getType()), message.getObjectValue(3));
                if (message.getType() == MTI.mtiValue(V1987, FINANCIAL, REQUEST_RESPONSE, ACQUIRER)) {
                    IsoValue<String> field3 = message.getField(3);
                    switch (field3.getValue()) {
                        case Constants.BIT3_VALIDATE_ACCOUNT_SOURCE:
                            ValidateSourceAccountDto.Response responseValidateRekening =
                                    mapper.validationSourceAccount(message);
                            log.info("response validate: {}", responseValidateRekening);
                            break;
                        default:
                            log.info("message apply: {}", message.debugString());
                    }
                }
                return false;
            }

            @Override
            public boolean onMessage(@NotNull ChannelHandlerContext channel, @NotNull IsoMessage message) {
                log.info("message decode: {}", message.debugString());
                return false;
            }
        });
        this.client.init();
        ChannelFuture channel = this.client.connectAsync();
        log.info("socket client: [connected: {}]", channel.await().isSuccess());
    }
}
