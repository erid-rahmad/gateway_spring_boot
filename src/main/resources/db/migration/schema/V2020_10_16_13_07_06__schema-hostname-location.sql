create table hostnames
(
    id           character varying(64)  not null primary key default uuid_generate_v4(),
    protocol     character varying(5)   not null             default 'http', -- required schema default http otherwise https
    service_id   character varying(100) not null,
    host         character varying(25)  not null,                            -- required hostname or ip address
    port         character varying(10)  not null,                            -- required port
    context_path character varying(100) not null             default '',     -- required context path if not have context path given empty string
    env          character varying(50)  not null             default 'dev',  -- required environment, choose [dev | test | prod] only
    message      text,
    kode_wilayah character varying(50),
    kode_cabang  character varying(50)
);

alter table hostnames
    add constraint uq_hostname_env unique (service_id, env);
