package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.MailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    /**
     * 获取可以查看的邮件
     *
     * @param gameId
     * @param agentId
     * @return
     */
    @RequestMapping("/getMailsInfo")
    public GlobeResponse getMailsInfo(Integer gameId, Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "业主ID参数错误");
        }

        if (gameId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "玩家游戏ID参数错误");
        }
        //获取该用户可以看的邮件
        List<MailVO> lists = accountsServiceClient.getMailsInfo(gameId, agentId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(lists);
        return globeResponse;
    }

    /**
     * 查看邮件内容
     */
    @RequestMapping("/openMail")
    public GlobeResponse openMail(Integer id) {
        MailVO mailsVO = accountsServiceClient.openMail(id);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(mailsVO);
        return globeResponse;
    }
    /**
     * 删除邮件
     */
    @RequestMapping("/deleteMail")
    public GlobeResponse deleteMail(int [] id) {
    	List<Integer> ids = new ArrayList<Integer>();
    	for (int i = 0; i < id.length; i++) {
    		ids.add(id[i]);
		}
        Boolean flag= accountsServiceClient.deleteMail(ids);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(flag);
        return globeResponse;
    }
}
