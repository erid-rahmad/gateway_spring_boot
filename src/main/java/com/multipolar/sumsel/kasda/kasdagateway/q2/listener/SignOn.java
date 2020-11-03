package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOServerAcceptEvent;
import org.jpos.iso.ISOServerEventListener;

import java.util.EventObject;

@Slf4j
public class SignOn implements ISOServerEventListener {
    @Override
    public void handleISOServerEvent(EventObject event) {
        if (event instanceof ISOServerAcceptEvent) {
            ISOServer server = (ISOServer) event.getSource();
            ISOChannel channel = server.getLastConnectedISOChannel();
            log.info("server from: {}, channel: {}", server.getName(), channel.getName());
        }
    }
}
