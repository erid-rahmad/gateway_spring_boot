package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class IncomingMessageListener implements ISORequestListener {

    @Autowired
    private MessageConverterFactory converterFactory;

    @Autowired
    IncomingMessageListener isoServer;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception{
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
//        ServerChannel channel = new ASCIIChannel(new ISO87APackager());
        ServerChannel channel = new XMLChannel(new ISO87APackager());
        ((LogSource) channel).setLogger(logger, "channel");
        ISOServer isoServer = new ISOServer(7894, channel, null);
        isoServer.setLogger(logger, "vlink-incoming-value");
        isoServer.addISORequestListener(this::process);
        new Thread(isoServer).start();
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        try {
            log.info("request mti: {}, field3: {}", isoMsg.getMTI(), isoMsg.getString(3));
            String pCode = isoMsg.getString(3);
            MessageConverterHandler converter = null;
            Map<String, Object> jsonRequest = new HashMap<>();
            switch (pCode) {
                case "330002":
                    converter = converterFactory.get("validationAccountSource");
                    jsonRequest = converter.doConvertToJSon(isoMsg, true);
                    log.info("json 330002: {}", jsonRequest);
                    break;
            }

            return true;
        } catch (ISOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
