package com.multipolar.sumsel.kasda.kasdagateway.config;

import lombok.extern.slf4j.Slf4j;
import org.jpos.q2.Q2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

@Slf4j
@Component("Q2SERVICE")
public class Q2Configuration {

    private Q2 q2;
    private final String baseDir;

    public Q2Configuration(@Value("${q2.basepath}") String baseDir) {
        this.baseDir = baseDir;
    }

    @PostConstruct
    public void start() throws Exception {
        try {
            String[] xargs = new String[]{"-r", "-d", new File(baseDir, "deploy").getAbsolutePath()};
            q2 = new Q2(xargs);
            q2.start();
        } catch (Exception e) {
            log.error("q2 service exception", e);
            throw e;
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        try {
            if (q2 != null) {
                q2.shutdown(true);
            }
        } catch (Exception ignored) {
            log.error("q2 service shutdown", ignored);
        } finally {
            q2 = null;
        }
    }
}
