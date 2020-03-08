package com.lzkj.mobile.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.accounts.db.entity 
 *  * @Description: TODO   AccountsInfo表实体
 *  * @Author:      
 *  * @CreateDate:  2020/1/23 12:16  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
public class AccountsInfoEntity {

    /**
     * 所属用户ID
     */
    private Integer userId;
    /**
     * 所属用户游戏ID
     */
    private Integer gameId;
    /**
     * 密保标识
     */
    private Integer protectId;
    /**
     * 口令索引
     */
    private Integer passwordId;
    /**
     * 推广员标识
     */
    private Integer spreaderId;
    /**
     * 用户帐号
     */
    private String accounts;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 注册帐号
     */
    private String regAccounts;
    /**
     * 个性签名
     */
    private String underWrite;
    /**
     * 身份证号
     */
    private String passPortId;
    /**
     * 真实名字
     */
    private String compellation;
    /**
     * 登录密码
     */
    private String logonPass;
    /**
     * 安全密码
     */
    private String insurePass;
    /**
     * 动态密码
     */
    private String dynamicPass;
    /**
     * 动态密码更新时间
     */
    private Date dynamicPassTime;
    /**
     * 头像标识
     */
    private Integer faceId;
    /**
     * 自定标识
     */
    private Integer customId;
    /**
     * 赠送礼物
     */
    private Integer present;
    /**
     * 用户奖牌
     */
    private Integer userMedal;
    /**
     * 经验数值
     */
    private Integer experience;
    /**
     *
     */
    private Integer growLevelId;
    /**
     * 用户魅力
     */
    private Integer loveLiness;
    /**
     * 用户权限
     */
    private Integer userRight;
    /**
     * 管理权限
     */
    private Integer masterRight;
    /**
     * 服务权限
     */
    private Integer serviceRight;
    /**
     * 管理等级
     */
    private Integer masterOrder;
    /**
     * 会员等级
     */
    private Integer memberOrder;
    /**
     * 过期日期
     */
    private Date memberOverDate;
    /**
     * 切换时间
     */
    private Date memberSwitchDate;
    /**
     * 头像版本
     */
    private Integer customFaceVer;
    /**
     * 用户性别
     */
    private Integer gender;
    /**
     * 禁止服务
     */
    private Integer nullity;
    /**
     * 禁止时间
     */
    private Date nullityOverDate;
    /**
     * 关闭标志
     */
    private Integer stunDown;
    /**
     * 固定机器
     */
    private Integer moorMachine;
    /**
     * 是否机器人
     */
    private Integer isAndroid;
    /**
     * 登录次数
     */
    private Integer webLogonTimes;
    /**
     * 登录次数
     */
    private Integer gameLogonTimes;
    /**
     * 游戏时间
     */
    private Integer playTimeCount;
    /**
     * 在线时间
     */
    private Integer onLineTimeCount;
    /**
     * 登录地址IP
     */
    private String lastLogonIp;
    /**
     * 登录时间
     */
    private Date lastLogonDate;
    /**
     * 登录手机
     */
    private String lastLogonMobile;
    /**
     * 登录机器
     */
    private String lastLogonMachine;
    /**
     * 注册地址
     */
    private String registerIp;
    /**
     * 注册时间
     */
    private Date registerDate;
    /**
     * 注册手机
     */
    private String registerMobile;
    /**
     * 注册机器
     */
    private String registerMachine;
    /**
     * PC       0x00     ,
     * ANDROID  0x10(cocos 0x11,u3d 0x12)     ,  16
     * ITOUCH   0x20     ,
     * IPHONE   0x30(cocos 0x31,u3d 0x32)     ,  48
     * IPAD     0x40(cocos 0x41,u3d 0x42)     ,
     * WEB      0x50                             80
     */
    private Integer registerOrigin;
    /**
     * =2 表示是游客
     */
    private Integer platformId;
    /**
     *
     */
    private String userUin;
    /**
     *
     */
    private Integer rankId;
    /**
     *
     */
    private Integer agentId;
    /**
     * 所属业主ID T_Acc_Agent.AgentID 677这套代理
     */
    private Integer parentId;
    /**
     * 变色用
     */
    private Integer userType;
    /**
     * 试玩任务ID
     */
    private String advertiser;
    /**
     *
     */
    private String lastLogonIpAddress;
    /**
     * 试玩平台名
     */
    private String advertPlat;
    /**
     *
     */
    private Integer qmSpreaderId;
    /**
     *
     */
    private BigDecimal zzQmRatio;
    /**
     *
     */
    private Integer zzIsAgent;
    /**
     *
     */
    private Integer h5AgentId;
    /**
     *
     */
    private String h5SiteCode;
    /**
     *
     */
    private String h5Account;
    /**
     *
     */
    private Integer device;
    /**
     * 首次充值日期
     */
    private Date firstRechargeDate;
    /**
     *
     */
    private Integer totalAgentId;
    /**
     *
     */
    private String phoneType;
    /**
     *
     */
    private String loginArea;
    /**
     *
     */
    private String channelAccount;
    /**
     *
     */
    private String channelPassword;
    /**
     * 是否为默认代理
     */
    private Integer isDefaultAgent;
    /**
     * 负盈利方案ID
     */
    private String programId;
    /**
     * 返佣方案名称
     */
    private String programName;
    /**
     * QQ
     */
    private String qq;
    /**
     * email
     */
    private String email;
    /**
     * 备注
     */
    private String remark;


}
