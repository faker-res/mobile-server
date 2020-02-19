package com.lzkj.mobile.v2.service;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.util.TimeUtil;
import com.lzkj.mobile.v2.inputVO.AccountsDetailPageVO;
import com.lzkj.mobile.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.service 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/11 19:34  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Service
public class PersonCenterService {

    @Resource
    private PlatformServiceClient platformServiceClient;
    @Resource
    private AccountsServiceClient accountsServiceClient;
    @Resource
    private TreasureServiceClient treasureServiceClient;

    public Map<String, Object> getUserVipLevel(Integer userId, Integer parentId) {
        Map<String, Object> data = new HashMap<>();

        Integer vipLevelCount = platformServiceClient.getVipLevelCount(parentId);
        List<VipRankReceiveVO> levelss = platformServiceClient.getUserLevelReceive(userId);
        //新创建用户添加VIP等级奖励
        if (levelss == null || levelss.size() == 0) {
            List<VipRankReceiveVO> lists = new ArrayList<>();
            for (int i = 1; i <= vipLevelCount; i++) {
                VipRankReceiveVO vo = new VipRankReceiveVO();
                vo.setVipRank(i);
                vo.setUserId(userId);
                vo.setNullity(false);
                vo.setRankMoney(BigDecimal.ZERO);
                vo.setReceiveDate(TimeUtil.getNow());
                lists.add(vo);

            }
            platformServiceClient.insertVipRankReceive(lists);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }

        } else if (levelss.size() < vipLevelCount) {

            List<VipRankReceiveVO> lists = new ArrayList<>();
            for (int i = levelss.size() + 1; i <= vipLevelCount; i++) {
                VipRankReceiveVO vo = new VipRankReceiveVO();
                vo.setVipRank(i);
                vo.setUserId(userId);
                vo.setNullity(false);
                vo.setRankMoney(BigDecimal.ZERO);
                vo.setReceiveDate(TimeUtil.getNow());
                lists.add(vo);

            }
            platformServiceClient.insertVipRankReceive(lists);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
        }

        List<VipLevelRewardVO> w1 = new ArrayList<>();
        List<VipLevelRewardVO> w2 = new ArrayList<>();
        List<VipLevelRewardVO> w3 = new ArrayList<>();
        List<VipLevelRewardVO> w4 = new ArrayList<>();
        List<VipLevelRewardVO> w5 = new ArrayList<>();
        List<VipLevelRewardVO> w6 = new ArrayList<>();
        VipLevelRewardVO vipLevel = accountsServiceClient.getUserVipLevel(userId);
        List<VipLevelRewardVO> vipConfig = accountsServiceClient.getVipLevelConfig(parentId);
        //用户VIP等级   当前VIP积分 还差下一级需要多少积分
        int size = vipConfig.size() - 1;
        for (int i = 0; i < vipConfig.size(); i++) {
            if (vipLevel.getVipIntegral().compareTo(vipConfig.get(size).getVipIntegral()) == 1) {
                vipLevel.setVipIntegral(new BigDecimal("0"));
                vipLevel.setTotal(new BigDecimal("0"));
                break;
            }
            if (i == size) {
                vipLevel.setVipIntegral(new BigDecimal("0"));
                vipLevel.setTotal(vipConfig.get(i).getVipIntegral());
                break;
            }
            if (vipLevel.getVipLevel() == 0) {
                vipLevel.setVipIntegral(vipConfig.get(i).getVipIntegral().subtract(vipLevel.getVipIntegral()));
                vipLevel.setTotal(vipConfig.get(i).getVipIntegral());
                break;
            }
            if (vipLevel.getVipLevel() == vipConfig.get(i).getVipLevel()) {
                vipLevel.setVipIntegral(vipConfig.get(i + 1).getVipIntegral().subtract(vipLevel.getVipIntegral()));
                vipLevel.setTotal(vipConfig.get(i + 1).getVipIntegral());
                break;
            }

        }

        List<VipLevelRewardVO> list = platformServiceClient.getUserVIPLevelReward(parentId);
        List<VIPReceiveInfoVO> week = platformServiceClient.getUserWeekReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> month = platformServiceClient.getUserMonthReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> day = platformServiceClient.getUserDayReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> year = platformServiceClient.getUserYearReceive(userId, vipLevel.getVipLevel());
        List<VipRankReceiveVO> levels = platformServiceClient.getUserLevelReceive(userId);
        int levelsLen = levels.size();
        int rewardListLen = list.size();
        int loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO jf = new VipLevelRewardVO();
            jf.setVipLevel(list.get(i).getVipLevel());
            jf.setVipIntegral(list.get(i).getVipIntegral());
            w6.add(jf);
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取天奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (day == null || day.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getDayReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setDayReward(list.get(i).getDayReward());
            vo.setStatus(status);
            w4.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取周奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (week == null || week.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getWeekReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setWeekReward(list.get(i).getWeekReward());
            vo.setStatus(status);
            w1.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取月奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (month == null || month.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getMonthReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setMonthReward(list.get(i).getMonthReward());
            vo.setStatus(status);
            w2.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取年奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (year == null || year.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getYearReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setYearReward(list.get(i).getYearReward());
            vo.setStatus(status);
            w5.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }

        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取等级奖励
        for (int i = 0; i < loopIndex; i++) {
            VipRankReceiveVO levelItem = levels.get(i);
            VipLevelRewardVO listItem = list.get(i);
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (vipLevel.getVipLevel() >= levelItem.getVipRank() && levelItem.getNullity() == false && listItem.getVipRankReward().compareTo(BigDecimal.ZERO) > 0) {
                status = 0;
            }
            if (vipLevel.getVipLevel() >= levelItem.getVipRank() && levelItem.getNullity() == true) {
                status = 2;
            }
            vo.setVipLevel(listItem.getVipLevel());
            vo.setVipRankReward(listItem.getVipRankReward());
            vo.setStatus(status);
            w3.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        Integer agentId = parentId;
        List<CleanChipsConfigVO> ls = platformServiceClient.getCleanChipsConfig(agentId);
        List<VipLevelRewardVO> clearBetAmount = new ArrayList<VipLevelRewardVO>();
        levelsLen = ls.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //洗码比例
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            if (vipLevel.getVipLevel() == 0) {
                if (ls.get(i).getVipLevel() == 1) {
                    vo.setVipLevel(0);
                    vo.setVipVersion(ls.get(i).getVipVersion());
                } else {
                    vo.setVipLevel(ls.get(i).getVipLevel());
                    vo.setVipVersion(ls.get(i).getVipVersion());
                }
            } else {
                vo.setVipLevel(ls.get(i).getVipLevel());
                vo.setVipVersion(ls.get(i).getVipVersion());
            }
            clearBetAmount.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }

        List<VipLevelRewardVO> yebRate = new ArrayList<>();
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //余额宝利率
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            if (vipLevel.getVipLevel() == 0) {
                if (list.get(i).getVipLevel() == 1) {
                    vo.setVipLevel(0);
                    vo.setYebRate(list.get(i).getYebRate());
                } else {
                    vo.setVipLevel(list.get(i).getVipLevel());
                    vo.setYebRate(list.get(i).getYebRate());
                }
            } else {
                vo.setVipLevel(list.get(i).getVipLevel());
                vo.setYebRate(list.get(i).getYebRate());
            }
            yebRate.add(vo);
        }
        //data.put("isOpen", "0");
        data.put("VipLevels", vipLevel);
        data.put("weekList", w1);
        data.put("dayList", w4);
        data.put("monthList", w2);
        data.put("yearList", w5);
        data.put("vipLevelList", w3);
        data.put("clearBetAmount", clearBetAmount);
        data.put("vipLevelCount", vipLevelCount);
        data.put("yebRate", yebRate);
        data.put("vipScore", w6);

        return data;
    }

    public Map<String, Object> getAccountDetails(AccountsDetailPageVO pageVo) {
        Integer userId = pageVo.getUserId();
        Integer date = pageVo.getDate();
        Integer typeId = pageVo.getTypeId();

        Map<String, Object> data = new HashMap<>();
        CommonPageVO<MemberRechargeVO> page = treasureServiceClient.getAccountDetails(pageVo);
        List<MemberRechargeVO> l = page.getLists();
        List<MemberRechargeVO> temp = new ArrayList<>();
        if (typeId.equals(10)) {
            for (int i = 0; i < l.size(); i++) {
                MemberRechargeVO vo = new MemberRechargeVO();
                if (!StringUtils.isBlank(l.get(i).getCollectNote())) {
                    vo.setTypeName(l.get(i).getCollectNote());
                } else {
                    vo.setTypeName(l.get(i).getTypeName());
                }
                vo.setBalance(l.get(i).getBalance());
                vo.setCollectDate(l.get(i).getCollectDate());
                if (l.get(i).getPresentScore().signum() == -1) {
                    vo.setExpenditureScore(l.get(i).getPresentScore().abs());
                } else {
                    vo.setPresentScore(l.get(i).getPresentScore());
                }
                temp.add(vo);
                page.setLists(temp);
            }
        } else if (typeId.equals(8)) {
            if (page.getLists() != null && page.getLists().size() > 0) {
                page.getLists().forEach(object -> {
                    if (!StringUtils.isBlank(object.getCollectNote())) {
                        object.setTypeName(object.getCollectNote());
                    }
                });
            }
        }
        AccountChangeStatisticsVO list = treasureServiceClient.accountChangeStatistics(userId, date);
        data.put("list", page.getLists());
        data.put("total", page.getPageCount());
        data.put("count", list);
        return data;
    }
}
