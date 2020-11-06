create table iso_8583_template_url
(
    id           character varying(64)  not null primary key default uuid_generate_v4(),
    mti          character varying(4)   not null             default '0200', -- default mti value
    pcode        character varying(10)  not null,                            -- bit 3 or processing code value
    path         character varying(64)  not null,                            -- send to url
    params       character varying(255),                                     -- default parameter, write as json format
    service_name  character varying(100) not null,                            -- relate serviceId by name
    http_method  character varying(4)   not null             default 'GET',  -- http method GET | POST | PUT | DELETE
    response_type character varying(4)   not null             default 'MAP',  -- default value is 'MAP' otherwise 'LIST'
    is_return    boolean                not null             default false   -- set return data to incoming server, if value false will not return and if true will return the data.
);

alter table iso_8583_template_url
    add constraint uq_iso_template unique (mti, pcode, path, service_name);
