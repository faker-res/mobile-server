package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.FundServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.v2.common.PageBean;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.bank.*;
import com.lzkj.mobile.v2.returnVO.bank.BankAccountVO;
import com.lzkj.mobile.v2.returnVO.bank.BankAgentVO;
import com.lzkj.mobile.vo.BankInfoVO;
import com.lzkj.mobile.vo.GlobeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.controller 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 12:17  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@RequestMapping("/bank")
@Api(value = "BankAccountInfoController", tags = "用户银行卡数据接口")
public class BankAccountInfoController {

    @Resource
    private FundServiceClient fundServiceClient;

    @GetMapping("/getBankInfo")
    @ApiOperation(value = "获取银行列表", notes = "获取银行列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public Response<List<BankAgentVO>> getBankInfo(@RequestParam Integer agentId) {
        return fundServiceClient.getBankList(agentId);
    }

    @GetMapping("/account/list")
    @ApiOperation(value = "银行卡管理", notes = "银行卡管理分页数据")
    public Response<PageBean<BankAccountVO>> bankList(@Valid BankAccountPageVO vo){
        vo.setPageNo(1);
        vo.setPageSize(3);
        vo.setWeb(true);
        return fundServiceClient.getBankAccountPage(vo);
    }

    @GetMapping("/account/setUse")
    @ApiOperation(value = "置为取款卡", notes = "置为取款卡")
    public Response setAccountUse(@Valid BankAccountUpdVO vo){
        vo.setType(BankAccountUpdVO.TYPE_ONE);
        vo.setIsUse(1);
        return fundServiceClient.updateBankAccount(vo);
    }

    @GetMapping("/record/list")
    @ApiOperation(value = "绑定记录", notes = "绑定记录")
    public Response recordList(@Valid BankAccountRecordPageVO vo){
        return fundServiceClient.getBankAccountRRecordPage(vo);
    }

    @GetMapping("/record/add")
    @ApiOperation(value = "添加银行卡", notes = "添加银行卡")
    public Response addRecord(@Valid BankAccountRecordAddVO vo){
        return fundServiceClient.addBankAccountRecord(vo);
    }

    @GetMapping("/record/canChange")
    @ApiOperation(value = "是否可以更换银行卡", notes = "是否可以更换银行卡")
    public Response canChangeBankCard(@Valid CanChangeBankCardVO vo){
        return fundServiceClient.canChangeBankCard(vo.getGameId());
    }

    @GetMapping("/record/changeCard")
    @ApiOperation(value = "更换银行卡", notes = "更换银行卡")
    public Response changeCard(@Valid BankAccountRecordAddVO vo){
        return fundServiceClient.addBankAccountRecord(vo);
    }

    @GetMapping("/record/cancel")
    @ApiOperation(value = "撤销申请", notes = "撤销申请")
    public Response cancelRecord(@Valid BankAccountRecordUpdVO vo){
        vo.setId(vo.getRecordId());
        vo.setStatus(5);
        return fundServiceClient.updateBankAccountRecord(vo);
    }

}
