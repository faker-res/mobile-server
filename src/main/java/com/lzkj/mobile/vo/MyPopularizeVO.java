package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class MyPopularizeVO {

   private Integer parent;               //上级ID

   private Integer gameId;               //用户游戏ID

   private Integer memberCount;          //团员人数

   private Integer drictMemberCount;     //直属玩家

   private Integer newMemberCount;       //新成员总数

   private Integer todayLogonCount;      //今日登录数
}
