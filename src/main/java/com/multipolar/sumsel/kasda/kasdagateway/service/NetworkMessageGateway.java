package com.multipolar.sumsel.kasda.kasdagateway.service;

import com.multipolar.sumsel.kasda.kasdagateway.converter.Chronometer;
import com.multipolar.sumsel.kasda.kasdagateway.model.ISOMsgLogEntry;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.MUX;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Service
public class NetworkMessageGateway {

    @Value("${q2.mux-name}")
    private String muxName;

    private static final long DEFAULT_TIMEOUT = 62000L;
    //    private static final long DEFAULT_REVERSAL_TIMEOUT = 62000L;
    private static final long DEFAULT_WAIT_TIMEOUT = 12000L;
//    private static final String defaultAcquirer = "117";

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddHHmmss");

    //    @Autowired
    //    private TraceNumberGenerator stan;

    public ISOMsg checkConnectivity() throws ConnectException, ISOException {
        Date date = new Date();

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(new ISO87APackager());
        isoMsg.setMTI(Constants.MTI_NETWORK);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE_GMT));
        isoMsg.set(7, DATE_FORMAT.format(date));
//        isoMsg.set(11, StringUtils.leftPad(stan.getTraceNumber(), 6, "0"));
        isoMsg.set(11, StringUtils.leftPad("1", 6, "0"));
        isoMsg.set(70, Constants.ECHO);
        return sendToHost(isoMsg, DEFAULT_TIMEOUT);
    }

    public ISOMsg signOn() throws ConnectException, ISOException {
        Date date = new Date();

        ISOMsg isoMsg = new ISOMsg();
//        String s1 = "";
        isoMsg.setPackager(new ISO87APackager());
        isoMsg.setMTI(Constants.MTI_NETWORK);
//        String acq = StringUtils.leftPad(defaultAcquirer, 11, Constants.SPACE);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE_GMT));
        isoMsg.set(7, DATE_FORMAT.format(date));
//        isoMsg.set(11, StringUtils.leftPad(stan.getTraceNumber(), 6, "0"));
        isoMsg.set(11, StringUtils.leftPad("1", 6, "0"));
//        s1 = isoMsg.getString(11);
        isoMsg.set(70, Constants.SIGN);
        return sendToHost(isoMsg, DEFAULT_TIMEOUT);
    }

    public ISOMsg sendToHost(final ISOMsg m, long timeout) throws ConnectException, ISOException {
        MUX mux = (MUX) NameRegistrar.getIfExists(muxName);
        if (mux == null)
            throw new ConfigurationException("mux is not configured");
        Chronometer chronometer = new Chronometer();
        if (isConnected(mux)) {
            long t = Math.max(timeout - chronometer.elapsed(), 1000L); // give at least a second to catch a response
            try {
                final ISOMsgLogEntry requestEntry = new ISOMsgLogEntry(m, new Date());
                ISOMsg resp = mux.request(m, t);
                final ISOMsgLogEntry responseEntry = new ISOMsgLogEntry(resp, new Date());

                if (resp == null) {
                    log.info("No response for message with trace number: {}.Log request for trace number: {}",
                            m.getString(11), m.getString(11));
                    logRequest(requestEntry);
                    return resp;
                }

                Thread thread = new Thread(() -> log(requestEntry, responseEntry));
                thread.start();

                return resp;
            } catch (ISOException e) {
                throw new ISOException(e);
            }
        } else {
            throw new ConnectException("not connected to host");
        }
    }

    protected boolean isConnected(MUX mux) {
        if (mux.isConnected())
            return true;
        long timeout = System.currentTimeMillis() + DEFAULT_WAIT_TIMEOUT;
        while (System.currentTimeMillis() < timeout) {
            if (mux.isConnected())
                return true;
            ISOUtil.sleep(500);
        }
        return false;
    }

    protected void log(ISOMsgLogEntry req, ISOMsgLogEntry resp) {
        try {
            log.info(dumpMessage(req));
            log.info(dumpMessage(resp));
        } catch (ISOException ex) {
            log.error("Something happen when log the message", ex);
        }
    }

    protected void logRequest(ISOMsgLogEntry req) {
        try {
            log.info(dumpMessage(req));
        } catch (ISOException ex) {
            log.error("Something happen when log the message", ex);
        }
    }

    private String dumpMessage(ISOMsgLogEntry entry) throws ISOException {
        ISOMsg msg = entry.getMessage();
        Date date = entry.getDate();
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("ISO Message MTI is " + msg.getMTI() + "\n");
        if (msg.isIncoming()) {
            builder.append("Incoming Message\n");
            builder.append("Message coming at " + format.format(date) + "\n");
        }
        if (msg.isOutgoing()) {
            builder.append("Outgoing Message\n");
            builder.append("Message going at " + format.format(date) + "\n");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps;
        try {
            ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name());
            msg.dump(ps, "");
            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            ps.close();

            builder.append(content);
        } catch (UnsupportedEncodingException e) {
            builder.append("Something happen when dump iso message");
            builder.append(msg + "\n");
            builder.append(e.getMessage());
        }

        return builder.toString();
    }
}
