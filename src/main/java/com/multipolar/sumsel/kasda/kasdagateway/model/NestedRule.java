package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;

@Data
public class NestedRule {
    private String other;
    private String key;
    private int length = 0;
    private String leftpad;
    private String rightpad;
    private String pcodee;
    private NestedRuleSec[] lainsec;
}
