package com.lzkj.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "accountsinfo",type = "base")
public class AccountsInfoEs {
	@Id
	@Field(type= FieldType.Integer)
	private int userID;

	@Field(type=FieldType.Integer)
	private int gameID;

  	@Field(type=FieldType.Integer)
	private String protectID;

	@Field(type=FieldType.Integer)
	private String passwordID;

	@Field(type=FieldType.Integer)
	private int spreaderID;

	@Field(type=FieldType.Text)
	private String accounts;

	@Field(type=FieldType.Text)
	private String nickname;

	@Field(type=FieldType.Text)
	private String account;

	@Field(type=FieldType.Text)
	private String regAccounts;

	@Field(type=FieldType.Text)
	private String underWrite;

	@Field(type=FieldType.Text)
	private String PassPortID;

	@Field(type=FieldType.Text)
	private String compellation;

	@Field(type=FieldType.Text)
	private String logonPass;

	@Field(type=FieldType.Text)
	private String insurePass;

	@Field(type=FieldType.Text)
	private String dynamicPass;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date dynamicPassTime;

	@Field(type=FieldType.Integer)
	private String faceID;

	@Field(type=FieldType.Integer)
	private String customID;

	@Field(type=FieldType.Integer)
	private int present;

	@Field(type=FieldType.Integer)
	private int userMedal;

	@Field(type=FieldType.Integer)
	private int experience;

	@Field(type=FieldType.Integer)
	private int growLevelID;

	@Field(type=FieldType.Integer)
	private int loveLiness;

	@Field(type=FieldType.Integer)
	private int userRight;

	@Field(type=FieldType.Integer)
	private int masterRight;

	@Field(type=FieldType.Integer)
	private int serviceRight;

	@Field(type=FieldType.Integer)
	private int masterOrder;

	@Field(type=FieldType.Integer)
	private int memberOrder;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date memberOverDate;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date memberSwitchDate;

	@Field(type=FieldType.Integer)
	private int customFaceVer;

 	@Field(type=FieldType.Integer)
	private int gender;

	@Field(type=FieldType.Integer)
	private int nullity;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
 	private Date nullityOverDate;

	@Field(type=FieldType.Integer)
	private int stunDown;

 	@Field(type=FieldType.Integer)
	private int moorMachine;

	@Field(type=FieldType.Integer)
	private int isAndroid;

	@Field(type=FieldType.Integer)
	private int webLogonTimes;

	@Field(type=FieldType.Integer)
	private int gameLogonTimes;

	@Field(type=FieldType.Integer)
	private int playTimeCount;

	@Field(type=FieldType.Integer)
	private int onLineTimeCount;

	@Field(type=FieldType.Text)
	private String LastLogonIP;

 	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date lastLogonDate;

	@Field(type=FieldType.Text)
	private String lastLogonMobile;

	@Field(type=FieldType.Text)
	private String lastLogonMachine;

	@Field(type=FieldType.Text)
	private String registerIP;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date registerDate;

	@Field(type=FieldType.Text)
	private String registerMobile;

	@Field(type=FieldType.Text)
	private String registerMachine;

 	@Field(type=FieldType.Integer)
	private int registerOrigin;

	@Field(type=FieldType.Integer)
	private int platformID;

	@Field(type=FieldType.Text)
	private String userUin;

	@Field(type=FieldType.Text)
	private String rankID;

 	@Field(type=FieldType.Integer)
	private int agentID;

	@Field(type=FieldType.Integer)
	private int parentID;

	@Field(type=FieldType.Integer)
	private int userType;

	@Field(type=FieldType.Text)
	private String advertiser;

	@Field(type=FieldType.Text)
	private String lastLogonIPAddress;

	@Field(type=FieldType.Text)
	private String advertPlat;

	@Field(type=FieldType.Integer)
	private int qmSpreaderID;

	@Field(type=FieldType.Double)
	private BigDecimal zzQmRatio;

	@Field(type=FieldType.Integer)
	private int zzIsAgent;

	@Field(type=FieldType.Integer)
	private Integer h5AgentId;

	@Field(type=FieldType.Text)
	private String h5siteCode;

	@Field(type=FieldType.Text)
	private String h5Account;

	@Field(type=FieldType.Integer)
	private String device;

	@Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date firstRechargeDate;

	@Field(type=FieldType.Integer)
	private Integer totalAgentID;

	@Field(type=FieldType.Text)
	private String phoneType;

	@Field(type=FieldType.Text)
	private String loginArea;

	@Field(type=FieldType.Text)
	private String channelAccount;

	@Field(type=FieldType.Text)
	private String channelPassword;

	@Field(type=FieldType.Text)
	private String isDefaultAgent;

	@Field(type=FieldType.Text)
	private String programName;

	@Field(type=FieldType.Text)
	private String programID;





}
