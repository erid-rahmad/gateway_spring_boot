package com.multipolar.sumsel.kasda.kasdagateway.dto;

import lombok.Data;

public class ValidateSourceAccountDto {

    @Data
    public static class Request {

        private String tahunAnggaran;
        private String nomorSpm;
        private String namaPenerima;
        private String bankPenerima;
        private String rekeningPenerima;
        private String dateCreated;
        private String uraian;
        private String namaUnit;
        private String namaSubUnit;
    }

    @Data
    public static class Response extends Request {

        private String status;
    }
}
