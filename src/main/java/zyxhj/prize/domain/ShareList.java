package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/*
 * 分享记录表
 */
@RDSAnnEntity(alias = "tb_prize_shareList")
public class ShareList {
	//ID
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long ShareListId;
	
	//用户ID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	//被分享ID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long beShareId;
	
	//创建时间
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	
}
