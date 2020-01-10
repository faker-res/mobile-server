package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class SignAwardConfig {
    private Integer id;
    private Integer agentId;
    /**
     * 显示方式：1随机不展示金额 2随机展示金额 3固定不展示金额
     */
    private Integer displayType;
    List<SignAwardConfigItem> itemList;
}
