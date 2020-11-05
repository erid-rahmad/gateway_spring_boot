package com.multipolar.sumsel.kasda.kasdagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCircuitBreaker
@SpringBootApplication
@EnableTransactionManagement
public class KasdaGatewayApplication {

    public static void main(String[] args) {
        System.setProperty("nfs.rpc.tcp.nodelay", "true");
        SpringApplication.run(KasdaGatewayApplication.class, args);
    }

}
