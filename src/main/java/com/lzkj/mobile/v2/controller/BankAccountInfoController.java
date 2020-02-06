package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.FundServiceClient;
import com.lzkj.mobile.v2.common.PageBean;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.bank.*;
import com.lzkj.mobile.v2.returnVO.bank.BankAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/bank")
@Api(value = "BankAccountInfoController", tags = "用户银行卡数据接口")
public class BankAccountInfoController {

    @Resource
    private FundServiceClient fundServiceClient;

    @GetMapping("/account/list")
    @ApiOperation(value = "银行卡管理", notes = "银行卡管理分页数据")
    public Response<PageBean<BankAccountVO>> bankList(@Valid BankAccountPageVO vo){
        vo.setPageNo(1);
        vo.setPageSize(3);
        vo.setWeb(true);
        return fundServiceClient.getBankAccountPage(vo);
    }

    @PostMapping("/account/setUse")
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

    @PostMapping("/record/add")
    @ApiOperation(value = "添加银行卡", notes = "添加银行卡")
    public Response addRecord(@Valid BankAccountRecordAddVO vo){
        return fundServiceClient.addBankAccountRecord(vo);
    }

    @GetMapping("/record/canChange")
    @ApiOperation(value = "是否可以更换银行卡", notes = "是否可以更换银行卡")
    public Response canChangeBankCard(@RequestParam("gameId") Integer gameId){
        return fundServiceClient.canChangeBankCard(gameId);
    }

    @PostMapping("/record/changeCard")
    @ApiOperation(value = "更换银行卡", notes = "更换银行卡")
    public Response changeCard(@Valid BankAccountRecordAddVO vo){
        return fundServiceClient.addBankAccountRecord(vo);
    }

    @PostMapping("/record/cancel")
    @ApiOperation(value = "撤销申请", notes = "撤销申请")
    public Response cancelRecord(@RequestParam("recordId") Integer recordId){
        BankAccountRecordUpdVO vo = new BankAccountRecordUpdVO();
        vo.setId(recordId);
        vo.setStatus(5);
        return fundServiceClient.updateBankAccountRecord(vo);
    }

}
