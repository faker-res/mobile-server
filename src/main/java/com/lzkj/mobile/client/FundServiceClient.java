package com.lzkj.mobile.client;

import com.lzkj.mobile.v2.common.PageBean;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.bank.*;
import com.lzkj.mobile.v2.returnVO.bank.BankAccountRecordVO;
import com.lzkj.mobile.v2.returnVO.bank.BankAccountVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.client 
 *  * @Description: TODO   资金服务
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 12:06  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@FeignClient(name = "fund-service")
public interface FundServiceClient {

    /******************  用户银行卡 *******************/
    @PostMapping("/bankAccount/getPage")
    Response<PageBean<BankAccountVO>> getBankAccountPage(@RequestBody BankAccountPageVO vo);

    @PostMapping("/bankAccount/update")
    Response<Void> updateBankAccount(@RequestBody BankAccountUpdVO vo);

    @PostMapping("/bankAccountRecord/getPage")
    Response<PageBean<BankAccountRecordVO>> getBankAccountRRecordPage(@RequestBody BankAccountRecordPageVO vo);

    @GetMapping("/bankAccountRecord/canChange")
    Response<Boolean> canChangeBankCard(@RequestParam("gameId") Integer gameId);

    @PostMapping("/bankAccountRecord/add")
    Response<Void> addBankAccountRecord(@RequestBody BankAccountRecordAddVO vo);

    @PostMapping("/bankAccountRecord/update")
    Response<Void> updateBankAccountRecord(@RequestBody BankAccountRecordUpdVO vo);

}