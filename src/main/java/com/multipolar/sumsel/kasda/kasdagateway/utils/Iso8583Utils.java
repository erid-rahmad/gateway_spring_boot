package com.multipolar.sumsel.kasda.kasdagateway.utils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Iso8583Utils {

    public static Optional<String> decode(ISOMsg message) throws ISOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps;
        try {
            ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name());
            message.dump(ps, "");
            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            ps.close();

            return Optional.of(content);
        } catch (UnsupportedEncodingException e) {
            return Optional.empty();
        }
    }
}
