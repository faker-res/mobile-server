package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DomainVO {

    private Integer id;
    private Integer duration;
    private BigDecimal fee;
    private Integer agentId;
}
