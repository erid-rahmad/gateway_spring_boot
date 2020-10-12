package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.dto.ValidateSourceAccountDto;
import com.multipolar.sumsel.kasda.kasdagateway.service.PaymentGatewayService;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.codecs.CompositeField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PaymentGatewayService service;

    @PostMapping("/validate/account-source")
    public ResponseEntity<?> validateAccountSource(@RequestBody ValidateSourceAccountDto.Request request) {
        log.info("message: {}", request);
        IsoMessage isoRequestValidate = this.service.createMessage(0x200);
        isoRequestValidate.setValue(2, "9999999999999999", IsoType.NUMERIC, 16);
        isoRequestValidate.setValue(3, Constants.BIT3_VALIDATE_ACCOUNT_SOURCE, IsoType.NUMERIC, 6);
        isoRequestValidate.setValue(18, Constants.CHANNEL_ID, IsoType.NUMERIC, 4);
        isoRequestValidate.setValue(32, Constants.AQUIERER_ID, IsoType.LLVAR, 11);
        isoRequestValidate.setValue(37, "111111111111", IsoType.NUMERIC, 12);
        isoRequestValidate.setValue(41, "22222222", IsoType.NUMERIC, 8);

        CompositeField bit48 = new CompositeField()
                .addValue(IsoType.NUMERIC.value(request.getTahunAnggaran(), 4))
                .addValue(IsoType.ALPHA.value(request.getNomorSpm(), 50))
                .addValue(IsoType.ALPHA.value(request.getNamaPenerima(), 100))
                .addValue(IsoType.ALPHA.value(request.getBankPenerima(), 50))
                .addValue(IsoType.NUMERIC.value(request.getRekeningPenerima(), 11))
                .addValue(IsoType.ALPHA.value(request.getDateCreated(), 20))
                .addValue(IsoType.ALPHA.value(request.getUraian(), 255))
                .addValue(IsoType.ALPHA.value(request.getNamaUnit(), 255))
                .addValue(IsoType.ALPHA.value(request.getNamaSubUnit(), 200));

        isoRequestValidate.setValue(48, bit48, bit48, IsoType.LLLVAR, 0);
        isoRequestValidate.setField(49, IsoType.NUMERIC.value("100", 3));

        log.info("iso message: [{}]", isoRequestValidate.debugString());
        service.send(isoRequestValidate);
        return ok(isoRequestValidate.debugString());
    }
}
