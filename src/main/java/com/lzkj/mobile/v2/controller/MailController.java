package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.entity.InternalMessageEntity;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.util.bean.BeanUtils;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.returnVO.mail.InternalMessageVO;
import com.lzkj.mobile.vo.GlobeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mail")
@Api(value = "MailController", tags = "用户邮件接口")
public class MailController {

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @GetMapping("/getMailsInfo")
    @ApiOperation(value = "获取用户邮件", notes = "获取用户邮件,后台限制最多30条")
    @ApiImplicitParams ({
        @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "gameId", value = "用户游戏ID", paramType = "query", dataType = "int", required = true)
    })
    public Response<List<InternalMessageVO>> getMailsInfo(@RequestParam Integer gameId) {
    	long startMillis = System.currentTimeMillis();
        //获取该用户可以看的邮件
        Response<List<InternalMessageVO>> response = accountsServiceClient.getMailsInfo(gameId);
        log.info("/getMailsInfo,耗时:{}", System.currentTimeMillis() - startMillis);
        return response;
    }

    @GetMapping("/openMail")
    @ApiOperation(value = "查看邮件内容", notes = "查看邮件内容")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "邮件ID集合", paramType = "query", dataType = "int", required = true)
    })
    public Response<List<InternalMessageVO>> openMail(@RequestParam Integer[] id) {
        List<Integer> ids = Arrays.asList(id);
        Response<Boolean> response = accountsServiceClient.openMail(ids);
        if (!response.getData()) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "读取邮件失败");
        }
        return accountsServiceClient.getOpenMailList(ids);
    }

    @GetMapping("/openAllMail")
    @ApiOperation(value = "打开用户所有邮件", notes = "打开用户所有邮件")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gameId", value = "用户游戏ID", paramType = "query", dataType = "int", required = true)
    })
    public Response<List<InternalMessageVO>> openAllMail(@RequestParam Integer gameId) {
        Response<Boolean> response = accountsServiceClient.openAllMail(gameId);
        if (!response.getData()) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "读取邮件失败");
        }
        return accountsServiceClient.getMailsInfo(gameId);
    }

    @GetMapping("/deleteMail")
    @ApiOperation(value = "删除邮件", notes = "删除邮件")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "邮件ID集合", paramType = "query", dataType = "int", required = true)
    })
    public Response<Boolean> deleteMail(@RequestParam Integer[] id) {
    	List<Integer> ids = Arrays.asList(id);
        return accountsServiceClient.deleteMail(ids);
    }

    @GetMapping("/deleteAllMail")
    @ApiOperation(value = "删除用户所有邮件", notes = "删除用户所有邮件")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gameId", value = "用户游戏ID", paramType = "query", dataType = "int", required = true)
    })
    public Response<Boolean> deleteAllMail(@RequestParam Integer gameId) {
        return accountsServiceClient.deleteAllMail(gameId);
    }

    @GetMapping("/totalMail")
    @ApiOperation(value = "统计用户未读邮件数", notes = "统计用户未读邮件数,最大值30")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gameId", value = "用户游戏ID", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public Response<Integer> totalMail(@RequestParam Integer gameId, Integer agentId) {
        Response<Integer> response = accountsServiceClient.totalMail(gameId, agentId);
        if (response.getData() != null &&  response.getData() > 30) {
            response.setData(30);
        }
        return response;
    }

    @GetMapping("/isOrNotOpenMail")
    @ApiOperation(value = "是否开启邮箱系统功能", notes = "是否开启邮箱系统功能")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public Response<Boolean> isOrNotOpenMail(@RequestParam int agentId) {
        return accountsServiceClient.isOrNotOpenMail(agentId);
    }

}
