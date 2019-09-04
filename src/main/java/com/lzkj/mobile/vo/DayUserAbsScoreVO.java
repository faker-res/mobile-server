package com.lzkj.mobile.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DayUserAbsScoreVO {
	private Integer id;
	private Integer userId;
	private Integer parentId;
	private Integer gameId;
	private String account;
	private String nickName;
	private Date today;
	private BigDecimal absScore;
	private BigDecimal directAbsScore;
	private BigDecimal score;
	private int ranking;
}
