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
        isoMessage.setValue(12, "104200", IsoType.TIME, 6);
        isoMessage.setValue(13, "1410", IsoType.DATE4, 4);
        isoMessage.setValue(18, Constants.CHANNEL_ID, IsoType.NUMERIC, 4);
        isoMessage.setValue(32, Constants.AQUIERER_ID, IsoType.LLVAR, 11);
        isoMessage.setValue(33, "33333333333", IsoType.LLVAR, 11);
        isoMessage.setValue(37, "111111111111", IsoType.NUMERIC, 12);
        isoMessage.setValue(41, "22222222", IsoType.NUMERIC, 8);
        isoMessage.setField(49, IsoType.NUMERIC.value(Constants.CURRENCY_CODE, 3));

        CompositeField bit120 = new CompositeField()
                .addValue(IsoType.NUMERIC.value(request.getTahunAnggaran(), 4))
                .addValue(IsoType.ALPHA.value(request.getNomorSpm(), 50))
                .addValue(IsoType.ALPHA.value(request.getNamaPenerima(), 100))
                .addValue(IsoType.ALPHA.value(request.getBankPenerima(), 50))
                .addValue(IsoType.NUMERIC.value(request.getRekeningPenerima(), 11))
                .addValue(IsoType.ALPHA.value(request.getDateCreated(), 20));
        isoMessage.setValue(120, bit120, bit120, IsoType.LLLVAR, 0);

        CompositeField bit121 = new CompositeField()
                .addValue(IsoType.ALPHA.value(request.getUraian(), 255))
                .addValue(IsoType.ALPHA.value(request.getNamaUnit(), 255))
                .addValue(IsoType.ALPHA.value(request.getNamaSubUnit(), 255));
        isoMessage.setValue(121, bit121, bit121, IsoType.LLLVAR, 0);
        log.debug("iso message: [{}]", isoMessage.debugString());
        return isoMessage;
    }

    public Response validationSourceAccount(IsoMessage message) {
        CompositeField field120 = message.getObjectValue(120);

        IsoValue<String> fieldTahunAnggaran = field120.getField(0);
        IsoValue<String> fieldNomorSpm = field120.getField(1);
        IsoValue<String> fieldNamaPenerima = field120.getField(2);
        IsoValue<String> fieldBankPenerima = field120.getField(3);
        IsoValue<String> fieldRekeningPenerima = field120.getField(4);
        IsoValue<String> fieldDateCreated = field120.getField(5);

        CompositeField field121 = message.getObjectValue(121);
        IsoValue<String> fieldUraian = field121.getField(6);
        IsoValue<String> fieldNamaUnit = field121.getField(7);
        IsoValue<String> fieldNamaSubUnit = field121.getField(8);
        IsoValue<String> fieldStatus = field121.getField(9);

        return Response.builder()
                .tahunAnggaran(fieldTahunAnggaran.getValue().trim())
                .nomorSpm(fieldNomorSpm.getValue().trim())
                .namaPenerima(fieldNamaPenerima.getValue().trim())
                .bankPenerima(fieldBankPenerima.getValue().trim())
                .rekeningPenerima(fieldRekeningPenerima.getValue().trim())
                .dateCreated(fieldDateCreated.getValue().trim())
                .uraian(fieldUraian.getValue().trim())
                .namaUnit(fieldNamaUnit.getValue().trim())
                .namaSubUnit(fieldNamaSubUnit.getValue().trim())
                .status(fieldStatus.getValue().trim())
                .build();
    }
}
