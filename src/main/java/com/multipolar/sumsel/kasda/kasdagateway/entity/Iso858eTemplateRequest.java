package com.multipolar.sumsel.kasda.kasdagateway.entity;

import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

@Data
@Entity
@Table(name = "iso_8583_template_url")
public class Iso858eTemplateRequest {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "mti")
    private String mti;
    @Column(name = "pcode")
    private String pCode;
    @Column(name = "path")
    private String path;
    @Column(name = "params")
    private String params;
    @Column(name = "service_name")
    private String serviceName;
    @Enumerated(EnumType.STRING)
    @Column(name = "http_method")
    private HttpMethod httpMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "response_type")
    private Iso8583ResponseType responseType;
    @Column(name = "is_return")
    private Boolean isReturn;

}
