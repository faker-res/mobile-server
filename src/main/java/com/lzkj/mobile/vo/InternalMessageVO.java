package com.lzkj.mobile.vo;

import lombok.Data;

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
public class InternalMessageVO {

	/**
	 * 主键ID
	 */
	private int id;
	/**
	 * 标题
	 */
	private String subject;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 所属用户ID
	 */
	private Integer userId;
	/**
	 * 所属用户游戏ID
	 */
	private Integer gameId;
	/**
	 * 业主ID
	 */
	private Integer agentId;
	/**
	 * 状态: -1:删除 0:未读,1:已读
	 */
	private Integer status;

}
