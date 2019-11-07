package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.vo.CleanChipsConfigVO;
import com.lzkj.mobile.vo.CleanChipsTotalVO;
import com.lzkj.mobile.vo.CleanChipsVO;
import com.lzkj.mobile.vo.GlobeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clean")
public class CleanChipsController {


    @Autowired
    private PlatformServiceClient platformServiceClient;

    /**
     * 1.视讯，2.电子，3.棋牌,4.捕鱼,5.体育,6.彩票
     * @param userId
     * @param agentId
     * @return
     */
    @RequestMapping("/chips")
    private GlobeResponse<Object> getCleanChips(Integer userId, Integer agentId) {
        //获取第三方除捕鱼外的数据
        Map<Integer, List<CleanChipsVO>> thirdData = platformServiceClient.getCleanChips(userId, agentId);
        //获取VIP洗码配置
        List<CleanChipsConfigVO> list = platformServiceClient.getCleanChipsConfig(agentId);
        CleanChipsConfigVO c= list.get(0);
        BigDecimal totalBet = BigDecimal.ZERO;
        BigDecimal estimate = BigDecimal.ZERO;
        thirdData.forEach((k,v)->{
             v.forEach(l->{
                 if (c.getOpenType()){
                     totalBet.add(l.getCleanBet());
                     if(k==1){
                         l.setRatio(c.getBetVersion());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetVersion()).setScale(2,BigDecimal.ROUND_HALF_DOWN));

                     }
                     if(k==2){
                         l.setRatio(c.getBetElectron());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetElectron()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==3){
                         l.setRatio(c.getBetChess());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetChess()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==4){
                         l.setRatio(c.getBetFish());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetFish()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==5){
                         l.setRatio(c.getBetSport());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetSport()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==6){
                         l.setRatio(c.getBetLottery());
                         l.setCleanValue(l.getCleanBet().multiply(c.getBetLottery()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     estimate.add(l.getCleanValue());
                 }else{
                     totalBet.add(l.getCleanBet());
                     if(k==1){
                         l.setRatio(c.getVipVersion());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipVersion()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==2){
                         l.setRatio(c.getVipElectron());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipElectron()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==3){
                         l.setRatio(c.getVipChess());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipChess()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==4){
                         l.setRatio(c.getVipFish());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipFish()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==5){
                         l.setRatio(c.getVipSport());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipSport()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                     if(k==6){
                         l.setRatio(c.getVipLottery());
                         l.setCleanValue(l.getCleanBet().multiply(c.getVipLottery()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     }
                 }
                 estimate.add(l.getCleanValue());
             });
        });
        CleanChipsTotalVO cleanChipsTotalVO =new CleanChipsTotalVO();
        cleanChipsTotalVO.setData(thirdData);
        cleanChipsTotalVO.setTotalBet(totalBet.setScale(2));
        cleanChipsTotalVO.setTotalValue(estimate.setScale(2));
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(cleanChipsTotalVO);
        return globeResponse;
    }

    @RequestMapping("/washBet")
    private GlobeResponse washBet(Integer userId,Integer agentId){


        return null;
    }
}
