package com.multipolar.sumsel.kasda.kasdagateway.service;

import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

    @Autowired
    private Iso8583Client<IsoMessage> client;

    public void send(IsoMessage message) {
        ChannelFuture channel = client.sendAsync(message);
    }
}
