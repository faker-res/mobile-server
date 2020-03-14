package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.FundServiceClient;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.bank.WithdrawInputVO;
import com.lzkj.mobile.v2.inputVO.bet.UserBetInfoInputVO;
import com.lzkj.mobile.v2.returnVO.bet.UserBetInfoUnionVO;
import com.lzkj.mobile.v2.util.IPUtils;
import com.lzkj.mobile.v2.util.ValidateParamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.controller 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/5 12:17  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(tags = "提现")
public class WithdrawController {

    @Resource
    private AccountsServiceClient accountsServiceClient;

    @Resource
    private FundServiceClient fundServiceClient;

    @Resource
    private ValidateParamUtil validateParamUtil;

    @GetMapping("/withdraw/deal")
    @ApiOperation(value = "提款", notes = "提款")
    public Response withdrawDeal(WithdrawInputVO vo, HttpServletRequest request) {
        validateParamUtil.valid(vo);
        vo.setIp(IPUtils.getIp(request));
        return accountsServiceClient.withdrawDeal(vo);
    }

    @GetMapping("/agentSystem/cashFlowDetails")
    @ApiOperation(value = "资金流水", notes = "资金流水")
    public Response<UserBetInfoUnionVO> cashFlowDetails(UserBetInfoInputVO vo) {
        validateParamUtil.valid(vo);
        vo.setWeb(true);
        return this.fundServiceClient.cashFlowDetails(vo);
    }


}
