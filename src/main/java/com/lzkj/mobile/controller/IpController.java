package com.lzkj.mobile.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzkj.mobile.util.StringUtil;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.IpDetailVO;

@RestController
@RequestMapping("/ipInterface")
public class IpController {
	@Resource(name="gameMongoTemplate")    
    private MongoTemplate mongoTemplate;
	
	
	//TODO 导数据专用
	@RequestMapping("/initIp")
	public GlobeResponse<Object> initIp() {
		GlobeResponse<Object> globeResponse = new GlobeResponse<>();
		String lineTxt = null;
		try {
			File file = new File("C:\\Users\\Owner\\Desktop\\45464.txt");
			if (file.isFile() && file.exists()) {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
				BufferedReader br = new BufferedReader(isr);
				while ((lineTxt = br.readLine()) != null) {
					String[] lineSplit = lineTxt.split(" ");
					String[] lineCells = new String[4];
					int i = 0;
					for (String s : lineSplit) {
						if (!s.equals("")) {
							lineCells[i++] = s;
							if (i == 4)
								break;
						}
					}
					lineCells[0] = StringUtil.delBom(lineCells[0]);
					lineCells[1] = StringUtil.delBom(lineCells[1]);
					long ip1Number = StringUtil.ip2Number(lineCells[0]);
					long ip2Number = StringUtil.ip2Number(lineCells[1]);
					IpDetailVO ipDetail = new IpDetailVO();
					ipDetail.setIp(lineCells[0]);
					ipDetail.setIp2(lineCells[1]);
					ipDetail.setIpNumber(ip1Number);
					ipDetail.setIpNumber2(ip2Number);
					ipDetail.setRemark1(lineCells[2]);
					ipDetail.setRemark2(lineCells[3]);
					mongoTemplate.save(ipDetail);
				}
				br.close();
			} else {
				System.out.println("文件不存在!");
			}

		} catch (Exception e) {
			System.err.println(lineTxt);
			e.printStackTrace();
		}
		return globeResponse;
	}

	@RequestMapping("/getIp")
	public GlobeResponse<Object> getIp(String ip) {
		GlobeResponse<Object> globeResponse = new GlobeResponse<>();
		long now = System.currentTimeMillis();
		long number = StringUtil.ip2Number(ip);
		IpDetailVO data = mongoTemplate.findOne(
				new Query(Criteria.where("ipNumber").lte(number).and("ipNumber2").gte(number)), IpDetailVO.class);
		System.err.println(System.currentTimeMillis() - now);
		globeResponse.setData(data == null ? "未知" : data.getRemark1());
		globeResponse.setData(data == null ? "未知" : data);
		return globeResponse;
	}
}
