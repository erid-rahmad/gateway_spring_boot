package com.multipolar.sumsel.kasda.kasdagateway.service;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.ConnectException;

@Slf4j
@Service
@EnableScheduling
public class NetworkManagementService implements CommandLineRunner {

    @Autowired
    private NetworkMessageGateway networkMessageGateway;

    @Transactional
    @Scheduled(cron = "0 0/60 * * * ?")
    public void echoUser() {
        try {
            log.info("scheduler run at: {}", System.currentTimeMillis());
            networkMessageGateway.checkConnectivity();
        } catch (ConnectException e) {
            log.error("connection exception", e);
        } catch (ISOException e) {
            log.error("iso exception", e);
        }
    }


    @Transactional
    public void signOn() {
        try {
            log.info("application started at: {}", System.currentTimeMillis());
            networkMessageGateway.signOn();
        } catch (ConnectException e) {
            log.info("connection exception", e);
        } catch (ISOException e) {
            log.info("iso exception", e);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        signOn();
    }
}
