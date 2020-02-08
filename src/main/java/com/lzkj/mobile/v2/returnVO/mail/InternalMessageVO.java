package com.lzkj.mobile.v2.returnVO.mail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.vo 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/1/23 10:55  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "InternalMessageVO", description = "游戏用户邮件数据返回封装类")
public class InternalMessageVO {

	@ApiModelProperty(name = "id", value = "主键ID")
	private int id;

	@ApiModelProperty(name = "subject", value = "标题")
	private String subject;

	@ApiModelProperty(name = "content", value = "内容")
	private String content;

	@ApiModelProperty(name = "userId", value = "所属用户ID")
	private Integer userId;

	@ApiModelProperty(name = "userId", value = "用户ID")
	private Integer gameId;

	@ApiModelProperty(name = "agentId", value = "业主ID")
	private Integer agentId;

	@ApiModelProperty(name = "status", value = "状态: -1:删除 0:未读,1:已读")
	private Integer status;

	@ApiModelProperty(name = "createTime", value = "创建时间")
	private Date createTime;

}
