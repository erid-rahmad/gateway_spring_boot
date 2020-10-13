package com.multipolar.sumsel.kasda.kasdagateway.mapper;

import com.github.kpavlov.jreactive8583.iso.J8583MessageFactory;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.codecs.CompositeField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.multipolar.sumsel.kasda.kasdagateway.dto.ValidateSourceAccountDto.Request;
import static com.multipolar.sumsel.kasda.kasdagateway.dto.ValidateSourceAccountDto.Response;

@Slf4j
@Component
public class IsoMessageMapper {

    @Autowired
    private J8583MessageFactory<IsoMessage> messageFactory;

    public IsoMessage validationSourceAccount(Request request) {
        IsoMessage isoMessage = this.messageFactory.newMessage(0x200);
        isoMessage.setValue(2, "9999999999999999", IsoType.NUMERIC, 16);
        isoMessage.setValue(3, Constants.BIT3_VALIDATE_ACCOUNT_SOURCE, IsoType.NUMERIC, 6);
        isoMessage.setValue(18, Constants.CHANNEL_ID, IsoType.NUMERIC, 4);
        isoMessage.setValue(32, Constants.AQUIERER_ID, IsoType.LLVAR, 11);
        isoMessage.setValue(37, "111111111111", IsoType.NUMERIC, 12);
        isoMessage.setValue(41, "22222222", IsoType.NUMERIC, 8);

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

        isoMessage.setValue(48, bit48, bit48, IsoType.LLLVAR, 0);
        isoMessage.setField(49, IsoType.NUMERIC.value("100", 3));
        log.info("iso message: [{}]", isoMessage.debugString());
        return isoMessage;
    }

    public Response validationSourceAccount(IsoMessage message) {
        CompositeField field48 = message.getObjectValue(48);

        IsoValue<String> fieldTahunAnggaran = field48.getField(0);
        IsoValue<String> fieldNomorSpm = field48.getField(1);
        IsoValue<String> fieldNamaPenerima = field48.getField(2);
        IsoValue<String> fieldBankPenerima = field48.getField(3);
        IsoValue<String> fieldRekeningPenerima = field48.getField(4);
        IsoValue<String> fieldDateCreated = field48.getField(5);
        IsoValue<String> fieldUraian = field48.getField(6);
        IsoValue<String> fieldNamaUnit = field48.getField(7);
        IsoValue<String> fieldNamaSubUnit = field48.getField(8);

        return Response.builder()
                .tahunAnggaran(fieldTahunAnggaran.getValue())
                .nomorSpm(fieldNomorSpm.getValue())
                .namaPenerima(fieldNamaPenerima.getValue())
                .bankPenerima(fieldBankPenerima.getValue())
                .rekeningPenerima(fieldRekeningPenerima.getValue())
                .dateCreated(fieldDateCreated.getValue())
                .uraian(fieldUraian.getValue())
                .namaUnit(fieldNamaUnit.getValue())
                .namaSubUnit(fieldNamaSubUnit.getValue())
                .build();
    }
}
