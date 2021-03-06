package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.entity.AccountsInfoEntity;
import com.lzkj.mobile.util.bean.BeanUtils;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.AccountsDetailPageVO;
import com.lzkj.mobile.v2.inputVO.personCenter.UpdateUserContactInfoVO;
import com.lzkj.mobile.v2.returnVO.user.UserInformationVO;
import com.lzkj.mobile.v2.service.PersonCenterService;
import com.lzkj.mobile.v2.util.ValidateParamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.controller 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/11 19:33  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(tags = "个人中心接口")
public class PersonCenterController {

    @Resource
    private AccountsServiceClient accountsServiceClient;
    @Resource
    private PersonCenterService personCenterService;
    @Resource
    private ValidateParamUtil validateParamUtil;

    @GetMapping("/mobileInterface/getUserVipLevel")
    @ApiOperation(value = "查询玩家VIP等级", notes = "查询玩家VIP等级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "parentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public Response<Map<String, Object>> getUserVipLevel(@RequestParam Integer userId, Integer parentId) {
        Map<String, Object> data = personCenterService.getUserVipLevel(userId, parentId);
        return Response.successData(data);
    }

    @GetMapping("/mobileInterface/getVipUserInfo")
    @ApiOperation(value = "获取用户个人信息", notes = "获取用户个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "int"),
    })
    public Response<UserInformationVO> getVipUserInfo(@RequestParam Integer userId) {
        AccountsInfoEntity accountsInfoEntity = accountsServiceClient.getUsersInfo(userId);
        UserInformationVO userInfo = BeanUtils.copyProperties(accountsInfoEntity, UserInformationVO::new);
        userInfo.setMobilePhone(accountsInfoEntity.getRegisterMobile());
        return Response.successData(userInfo);
    }

    @GetMapping("/mobileInterface/getAccountDetails")
    @ApiOperation(value = "获取账户明细", notes = "获取账户明细")
    public Response<Object> getAccountDetails(AccountsDetailPageVO vo) {
        validateParamUtil.valid(vo);
        Map<String, Object> map = personCenterService.getAccountDetails(vo);
        return Response.successData(map);
    }

    @GetMapping("/mobileInterface/updateUserContactInfo")
    @ApiOperation(value = "修改用户个人信息", notes = "修改用户个人信息")
    public Response updateUserContactInfo(UpdateUserContactInfoVO vo) {
        validateParamUtil.valid(vo);
        return accountsServiceClient.updateUserContactInfo(vo);
    }

}
