package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/*
 * 用户信息表
 */
@RDSAnnEntity(alias = "tb_prize_prizeUser")
public class PrizeUser {
	//id
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	//微信OPENID
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String openId;
	
	//用户头像
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String userHead;
	
	//用户名
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String userName;
	
	//用户手机号
	@RDSAnnField(column = RDSAnnField.TEXT_PWD)
	public String phoneNum;
	
	//创建时间
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
}
