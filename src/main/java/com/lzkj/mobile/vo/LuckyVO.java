package com.lzkj.mobile.vo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
public class LuckyVO {
    private Integer serverId;
    private Integer gameId;
    private BigDecimal score;
    private Long endTime;
    private String nickName;

}
