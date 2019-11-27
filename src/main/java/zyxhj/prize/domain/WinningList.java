package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;


/*
 * 参与抽奖记录表
 */
@RDSAnnEntity(alias = "tb_prize_winningList")
public class WinningList {
	//ID
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long winningId;
	
	//抽奖信息id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long PrizeId;
	
	//用户id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long winningUserId;
	
	//用户中奖率
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer winningRate;
	
	//状态
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte winningStatus;
	/*
	 * 是否中奖
	 * true:中奖
	 * false：没中奖
	 */
	@RDSAnnField(column = RDSAnnField.BOOLEAN)
	public Boolean isWinning;
	
	//奖品id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long productId;
	
	//奖品等级
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte winningGrade;
	
	//创建时间
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	
	@AnnDicField(alias = "待开奖")
	public static final Byte STATUS_WAITOPEN = 0;
	@AnnDicField(alias = "已开奖")
	public static final Byte STATUS_OPEN = 1;
}
