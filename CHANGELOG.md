# `v0.0.1-snapshot`

- Integration JReactive version `1.3.0`
- Send iso 8583 with asci channel
- Automate echo message `x0800` every 5 seconds
- Enabled swagger UI
- Enabled Circuit Breaker

# `v2020.10.21.10.30-release`

- Implement jpos 
- Create rule

# `v2020.10.22.18.05-release`

- set default value for bit 2 = `627452` + bit 7
- Fixing missing rule.json

# `v2020.11.09.09.55-release`

- Message Converter 
    - iso (0200) -> json & json -> iso (0200) using `ReverseDefaultConverterHandler`
    - iso (0210) -> json & json -> iso (0210) using `DefaultConverterHandler`
- Enabled proxy to SIPKD
- Enabled Q2 Server using listener integration spring-boot
- Enabled stan / bit 7 using sequance generator

