package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommonPageVO<E> {

    private List<E> lists;

    private int pageCount;

    private int recordCount;
}
