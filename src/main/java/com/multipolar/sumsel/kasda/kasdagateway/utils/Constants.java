package com.multipolar.sumsel.kasda.kasdagateway.utils;

import java.text.SimpleDateFormat;

public interface Constants {

    String AQUIERER_ID = "100800";
    String TIME_ZONE_GMT = "GMT";
    String CURRENCY_CODE = "360";
    String TERMINAL_ID = "KASDA";
    String CHANNEL_ID = "6010";
    String MTI_NETWORK = "0800";
    String ECHO = "301";
    String SIGN = "001";
    String DEFAULT_FORWARD_ID = "100800";
    String TRANSACTION_MTI = "0200";
    String BIT2_DEFAULT_VALUE = "627452";
    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddHHmmss");
    SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss");
    SimpleDateFormat LOCAL_DATE_FORMAT = new SimpleDateFormat("MMdd");
    SimpleDateFormat LOCAL_DATE = new SimpleDateFormat("MMdd");
    SimpleDateFormat LOCAL_TIME = new SimpleDateFormat("HHmmss");
    SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");

}
