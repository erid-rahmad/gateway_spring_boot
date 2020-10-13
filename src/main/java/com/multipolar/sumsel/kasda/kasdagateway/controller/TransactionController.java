package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.multipolar.sumsel.kasda.kasdagateway.dto.ValidateSourceAccountDto;
import com.multipolar.sumsel.kasda.kasdagateway.mapper.IsoMessageMapper;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/payment-gateway/v1.0/transaction")
public class TransactionController {

    @Autowired
    private Iso8583Client<IsoMessage> client;

    @Autowired
    private IsoMessageMapper messageMapper;

    @PostMapping("/validate/account-source")
    public ResponseEntity<?> validateAccountSource(@RequestBody ValidateSourceAccountDto.Request request)
            throws InterruptedException {
        IsoMessage isoMessage = this.messageMapper.validationSourceAccount(request);
        try {
            ChannelFuture channel = client.sendAsync(isoMessage).await();
            log.info("status: {isDone: {}, isSuccess: {}, isCancelled: {}, isCancellable: {}, isVoid: {}}",
                    channel.isDone(), channel.isSuccess(), channel.isCancelled(), channel.isCancellable(), channel.isVoid());
            return ok(isoMessage.debugString());
        } catch (java.lang.IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(ex.getLocalizedMessage());
        }
    }
}
