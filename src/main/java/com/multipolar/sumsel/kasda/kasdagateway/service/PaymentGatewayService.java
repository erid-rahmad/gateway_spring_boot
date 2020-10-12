package com.multipolar.sumsel.kasda.kasdagateway.service;

import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.github.kpavlov.jreactive8583.iso.J8583MessageFactory;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

    @Autowired
    private Iso8583Client<IsoMessage> client;

    @Autowired
    private J8583MessageFactory<IsoMessage> messageFactory;

    public IsoMessage createMessage(int type){
        return this.messageFactory.newMessage(type);
    }

    public void send(IsoMessage message) {
        ChannelFuture channel = client.sendAsync(message);
    }
}
