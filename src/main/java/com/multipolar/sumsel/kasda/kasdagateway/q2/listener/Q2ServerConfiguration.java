package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class Q2ServerConfiguration implements ISORequestListener {

    @Autowired
    private MessageConverterFactory converterFactory;

    @Autowired
    Q2ServerConfiguration isoServer;

    @Value("${q2.server-port}")
    private Integer q2Port;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception {
        ServerChannel channel = new ASCIIChannel(new ISO87APackager());
        ISOServer isoServer = new ISOServer(q2Port, channel, null);
        isoServer.addISORequestListener(this::process);
        new Thread(isoServer).start();
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        MessageConverterHandler converter = null;
        String messageFactoryProcessing = null;
        try {
            log.info("request mti: {}, field3: {}", isoMsg.getMTI(), isoMsg.getString(3));
            ISOMsg requestMsg = (ISOMsg) isoMsg.clone();
            String pCode = requestMsg.getString(3);
            // DEVELOPER Notes: after received set value bit 39 = '00' when success or if failed = '01'
            requestMsg.set(39, "00");

            switch (pCode) {
                case "330054":
                    messageFactoryProcessing = "inquirySP2D";
                    break;
                case "330055":
                    messageFactoryProcessing = "inquirySPJ";
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
            }

            FeatureContextHolder.getContext().setFeatureName(messageFactoryProcessing);
            converter = converterFactory.get(messageFactoryProcessing);
            Map<String, Object> jsonRequest = converter.doConvertToJSon(requestMsg, true);
            log.info("q2 server request -> {}", jsonRequest);
            String kodeCabang = (String) jsonRequest.getOrDefault("Kd_Cabang", null);
            String kodeWilayah = (String) jsonRequest.getOrDefault("Kd_Wilayah", null);

            return true;
        } catch (ISOException e) {
            log.error("iso exception ==>", e);
            return false;
        }
    }
}
