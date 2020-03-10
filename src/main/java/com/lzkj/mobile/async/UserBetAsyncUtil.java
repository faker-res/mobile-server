package com.lzkj.mobile.async;

import com.lzkj.mobile.client.FundServiceClient;
import com.lzkj.mobile.v2.inputVO.bet.UserBetInfoFillVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
@Async
public class UserBetAsyncUtil {

    @Resource
    private FundServiceClient fundServiceClient;

    public void pushUserBet(int userId, BigDecimal betTotal) {
        UserBetInfoFillVO vo = new UserBetInfoFillVO();
        vo.setUserId(userId);
        vo.setBetFact(betTotal);
        fundServiceClient.fillUserBetInfo(vo);
    }
}
