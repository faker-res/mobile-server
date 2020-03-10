package com.lzkj.mobile.async;

import com.lzkj.mobile.client.FundServiceClient;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.bet.UserBetInfoFillVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Component
@Async
public class UserBetAsyncUtil {

    @Resource
    private FundServiceClient fundServiceClient;

    public void pushUserBet(int userId, BigDecimal betTotal) {
        UserBetInfoFillVO vo = new UserBetInfoFillVO();
        vo.setUserId(userId);
        vo.setBetFact(betTotal);
        Response response = fundServiceClient.fillUserBetInfo(vo);
        if(!Response.SUCCESS.equals(response.getCode())){
            log.info("天天游戏推送打码量失败：入参{}, 返回", vo, response);
        }
    }
}
