package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hostnames")
public class HostnameRoutes {

    @Id
    @GenericGenerator(name = "uuid_gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid_gen")
    @Column(name = "id")
    private String id;
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "host")
    private String host;
    @Column(name = "protocol")
    private String protocol;
    @Column(name = "port")
    private String port;
    @Column(name = "context_path")
    private String contextPath;
    @Column(name = "env")
    private String environment;
    @Column(name = "message")
    private String message;
    @Column(name = "kode_cabang")
    private String kodeCabang;
    @Column(name = "kode_wilayah")
    private String kodeWilayah;

}
