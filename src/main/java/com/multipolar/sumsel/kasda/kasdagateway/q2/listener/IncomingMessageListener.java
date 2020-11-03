package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

@Slf4j
public class IncomingMessageListener implements ISORequestListener {

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        System.out.println("===call iso request listener");
        try {
            log.info("request listener mti: {}, raw: {}", isoMsg.getMTI(), isoMsg.toString());
            return true;
        } catch (ISOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
