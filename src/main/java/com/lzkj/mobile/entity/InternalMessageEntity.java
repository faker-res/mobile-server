package com.lzkj.mobile.entity;

import lombok.Data;

import java.util.Date;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.entity 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/11 20:52  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
public class InternalMessageEntity {

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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updater;

}
