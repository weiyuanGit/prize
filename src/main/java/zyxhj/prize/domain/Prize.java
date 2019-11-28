package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

@RDSAnnEntity(alias = "tb_prize_prize")
public class Prize {
	//id
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long prizeId;
	//一等奖id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long fristPrizeId;
	
	//一等奖数量
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer fristPrizeNum;
	
	//二等奖id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long secondPrizeId;
	
	//二等奖数量
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer secondPrizeNum;
	
	//三等奖id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long threePrizeId;
	
	//三等奖数量
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer threePrizeNum;
	
	//状态
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte prizeStatus;
	
	//指定用户中奖
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String prizeUserId;
	
	//开奖方式
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte prizeWay;
	
	
	//开奖时间-时间戳
	@RDSAnnField(column = RDSAnnField.ID)
	public Long prizeTime;
	
	//抽奖说明
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String prizeExplain;
	
	//发起人id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long createUserId;
	
	//图文介绍
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String imageText;
	
	//领奖方式
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte receiveWay;
	
	
	//助力倍数
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer helpTimes;
	
	//是否开启好友助力
	//true:开启（默认）
	//false：不开启
	@RDSAnnField(column = RDSAnnField.BOOLEAN)
	public Boolean friendHelp;
	
	//红包激励(暂时不做)
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String redPacket;
	
	//助力口令(暂时不做)
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String helpPasword;
	
	//是否开启评论(暂时不做)
	//true:开启
	//false：不开启（默认）
	@RDSAnnField(column = RDSAnnField.BOOLEAN)
	public Boolean isReply;
	
	//创建时间
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	

	@AnnDicField(alias = "待开奖")
	public static final Byte STATUS_OPEN = 0;
	@AnnDicField(alias = "已开奖")
	public static final Byte STATUS_GO = 1;
	@AnnDicField(alias = "已关闭")
	public static final Byte STATUS_CLOSE = 2;
	
	@AnnDicField(alias = "定时开奖")
	public static final Byte PRIZEWAY_TIMEING = 0;
	
	@AnnDicField(alias = "默认")
	public static final Byte RECEIVEWAY_DEFAULT = 0;
}
