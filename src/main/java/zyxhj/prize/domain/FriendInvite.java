package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/*
 * 邀请好友关系表
 */
@RDSAnnEntity(alias = "tb_prize_friendInvite")
public class FriendInvite {
	//id
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long friendId;
	
	//用户id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	//被邀请用户id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long inviteUserId;
	
	//抽奖活动id
	@RDSAnnField(column = RDSAnnField.ID)
	public Long prizeId;
	
	//创建时间
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
}
