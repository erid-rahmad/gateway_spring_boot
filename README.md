# kasda-gateway

### System Requirement

- PostgreSQL database

### Q2 config

TODO: change host & port inside folder `q2/deploy/10_channel_jpos.xml`

```conf
<?xml version="1.0" ?>

<channel-adaptor name="vlink-channel" class="org.jpos.q2.iso.ChannelAdaptor" logger="Q2">
    <channel class="org.jpos.iso.channel.ASCIIChannel"
             packager="org.jpos.iso.packager.ISO87APackager">

        <!-- changed port & host property to your core-banking -->
        <property name="host" value="localhost"/>
        <property name="port" value="8000"/>
    </channel>
    <in>vlink-send</in>
    <out>vlink-receive</out>
    <reconnect-delay>10000</reconnect-delay>
</channel-adaptor>
```

### Deploying

- Build with maven `mvn clean -DskipTests package`
- Copy file `target/kasda-gateway-x.x.x-release.jar` dan folder `target/q2` ke folder yang di inginkan
- Running as jar
    ```bash
    java -jar file-name.jar --DATABASE_HOST=localhost --DATABASE_PORT=5432 --DATABASE_NAME=your-db-name DATABASE_USER=your-db-user --DATABASE_PASSWORD=your-db-pass
    ```
- Running as service systemd (linux)
    - create file di `/etc/systemd/system/kasda-gateway.service`
        ```service
        [Unit]
        Description=sales-tools-auth-service
        After=syslog.target

        [Service]
        ExecStart=/path-your-app/application.jar --DATABASE_HOST=localhost --DATABASE_PORT=5432 --DATABASE_NAME=your-db-name DATABASE_USER=your-db-user --DATABASE_PASSWORD=your-db-pass
        SuccessExitStatus=143

        [Install]
        WantedBy=multi-user.target
        ```
    - reload systemd `sudo systemctl daemon-reload`
    - start service `sudo systemctl start kasda-gateway.service`


