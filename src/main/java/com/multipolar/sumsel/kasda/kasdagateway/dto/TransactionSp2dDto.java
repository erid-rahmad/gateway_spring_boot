package com.multipolar.sumsel.kasda.kasdagateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionSp2dDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @JsonProperty("kode_urusan")
        private String kodeUrusan;
        @JsonProperty("kode_bidang")
        private String kodeBidang;
        @JsonProperty("kode_unit")
        private String kodeUnit;
        @JsonProperty("kode_sub")
        private String kodeSub;
        @NotNull
        @NotEmpty
        @JsonProperty("tahun")
        private String tahun;
        @NotNull
        @NotEmpty
        @JsonProperty("no_sp2d")
        private String noSp2d;
        @JsonProperty("tanggal_sp2d")
        private Timestamp tanggalSp2d;
        @JsonProperty("no_spm")
        private String noSpm;
        @JsonProperty("jn_spm")
        private String jnSpm;
        @JsonProperty("nama_penerima")
        private String namaPenerima;
        @JsonProperty("keterangan")
        private String keterangan;
        @JsonProperty("npwp")
        private String npwp;
        @JsonProperty("bank_penerima")
        private String bankPenerima;
        @JsonProperty("rekening_penerima")
        private String rekeningPenerima;
        @JsonProperty("tanggal_penguji")
        private Timestamp tanggalPenguji;
        @JsonProperty("nama_bank")
        private String namaBank;
        @JsonProperty("no_rekening")
        private String noRekening;
        @JsonProperty("nilai")
        private BigDecimal nilai;
        @JsonProperty("date_created")
        private Timestamp dateCreated;
        @JsonProperty("cair")
        private BigDecimal cair;
        @JsonProperty("tanggal_cair")
        private Timestamp tanggalCair;
        @JsonProperty("gaji")
        private BigDecimal gaji;
        @JsonProperty("nama_unit")
        private String namaUnit;
        @JsonProperty("nama_sub_unit")
        private String namaSubUnit;
        @JsonProperty("uraian")
        private String uraian;

    }

}
