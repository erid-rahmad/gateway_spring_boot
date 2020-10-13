package com.multipolar.sumsel.kasda.kasdagateway.dto;

import lombok.Builder;
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
    @Builder
    public static class Response {

        private String tahunAnggaran;
        private String nomorSpm;
        private String namaPenerima;
        private String bankPenerima;
        private String rekeningPenerima;
        private String dateCreated;
        private String uraian;
        private String namaUnit;
        private String namaSubUnit;
        private String status;
    }
}
