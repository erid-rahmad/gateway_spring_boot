package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class IncomingMessageListener implements ISORequestListener {

    @Autowired
    private MessageConverterFactory converterFactory;

    @Autowired
    IncomingMessageListener isoServer;

    @Value("${q2.server-port}")
    private Integer q2Port;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
//        ServerChannel channel = new ASCIIChannel(new ISO87APackager());
        ServerChannel channel = new XMLChannel(new ISO87APackager());
        ((LogSource) channel).setLogger(logger, "channel");
        ISOServer isoServer = new ISOServer(q2Port, channel, null);
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
            String messageFactoryProcessing = null;

            switch (pCode) {
                case "330054":
                    messageFactoryProcessing = "inquirySP2D";
                    break;
                case "330055":
                    messageFactoryProcessing = "inquirySPJ";
                    break;
                case "330003":
                    messageFactoryProcessing = "loggingSP2D";
                    break;
                case "330004":
                    messageFactoryProcessing = "loggingSPJ";
                    break;
                case "330005":
                    messageFactoryProcessing = "notificationSP2D";
                    break;
                case "330006":
                    messageFactoryProcessing = "notificationSPJ";
                    break;
                case "010054":
                    messageFactoryProcessing = "transactionSP2D";
                    break;
                case "010055":
                    messageFactoryProcessing = "transactionSPJ";
                    break;
                case "330002":
                    messageFactoryProcessing = "validationAccountSource";
                    break;
            }
            converter = converterFactory.get(messageFactoryProcessing);
            Map<String, Object> jsonRequest = converter.doConvertToJSon(isoMsg, true);
            log.info("json properties: {}", jsonRequest);
            return true;
        } catch (ISOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
