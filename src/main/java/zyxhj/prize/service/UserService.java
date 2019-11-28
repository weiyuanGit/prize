package zyxhj.prize.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;

import zyxhj.prize.domain.FriendInvite;
import zyxhj.prize.domain.PrizeUser;
import zyxhj.prize.domain.WinningList;
import zyxhj.prize.repository.FriendInviteRepository;
import zyxhj.prize.repository.PrizeUserRepository;
import zyxhj.prize.repository.WinningListRepository;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;

public class UserService extends Controller{
	private DruidDataSource ds;
	private PrizeUserRepository userRepository;
	private FriendInviteRepository friendInviteRepository;
	private WinningListRepository winningListRepository;
	public UserService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			userRepository = Singleton.ins(PrizeUserRepository.class);
			friendInviteRepository = Singleton.ins(FriendInviteRepository.class);
			winningListRepository = Singleton.ins(WinningListRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@POSTAPI( //
			path = "userLogin", //
			des = "用户登录", //
			ret = "" //
	)
	public APIResponse userLogin(
		@P(t = "邀请人id", r = false)Long inviterId,
		@P(t = "微信OPENID")String openId,
		@P(t = "用户头像")String userHead,
		@P(t = "用户名")String userName,
		@P(t = "用户手机号")String phoneNum
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			PrizeUser user = userRepository.get(conn, EXP.INS().key("open_id", openId));
			if(user == null) {
				user = new PrizeUser();
				user.userId = IDUtils.getSimpleId();
				user.openId = openId;
				user.userHead = userHead;
				user.userName = userName;
				user.phoneNum = phoneNum;
				user.createTime = new Date();
				userRepository.insert(conn, user);
			}
			if(inviterId != null) {
				createFriendInvite(inviterId, user.userId);
			}
			return APIResponse.getNewSuccessResp("登录成功");
		}
	}
	
	@POSTAPI( //
			path = "getUserById", //
			des = "根据用户id获取用户信息", //
			ret = "" //
	)
	public PrizeUser getUserById(
		@P(t = "用户id")Long userId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			return userRepository.get(conn, EXP.INS().key("user_id", userId));
		}
	}
	
	@POSTAPI( //
			path = "getUserListByPrizeId", //
			des = "查询参加本次抽奖的所有用户", //
			ret = "" //
	)
	public JSONArray getUserListByPrizeId(
		@P(t = "抽奖活动id")Long PrizeId,
		@P(t = "是否中奖", r=false)Boolean isWinning,
		@P(t = "奖品等级", r=false)Byte winningGrade,
		Integer count,
		Integer offset
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			return userRepository.getUserListByPrizeId(conn, PrizeId, isWinning, winningGrade, count, offset);
		}
	}
	
	@POSTAPI( //
			path = "updateUserById", //
			des = "根据用户id修改用户信息", //
			ret = "" //
	)
	public void updateUserById(
		@P(t = "用户id")Long userId,
		@P(t = "用户头像")String userHead,
		@P(t = "用户名")String userName,
		@P(t = "用户手机号")String phoneNum
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			PrizeUser t = new PrizeUser();
			t.userHead = userHead;
			t.userName = userName;
			t.phoneNum = phoneNum;
			userRepository.update(conn, EXP.INS().key("user_id", userId), t, true);
		}
	}
	
	@POSTAPI( //
			path = "deleteUserById", //
			des = "根据用户id删除用户信息", //
			ret = "" //
	)
	public void deleteUserById(
		@P(t = "用户id")Long userId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			userRepository.delete(conn, EXP.INS().key("user_id", userId));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void createFriendInvite(
		@P(t = "用户id")Long userId,
		@P(t = "被邀请用户id")Long inviteUserId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			FriendInvite t = new FriendInvite();
			t.friendId = IDUtils.getSimpleId();
			t.userId = userId;
			t.inviteUserId = inviteUserId;
			t.createTime = new Date();
			friendInviteRepository.insert(conn, t);
		}
	}
	
	@POSTAPI( //
			path = "getFriendInviteListByUserId", //
			des = "查询用户指定抽奖活动所有邀请记录", //
			ret = "" //
	)
	public List<FriendInvite> getFriendInviteListByUserId(
		@P(t = "用户id")Long userId,
		@P(t = "抽奖活动id")Long prizeId,
		Integer count,
		Integer offset
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			return friendInviteRepository.getList(conn, EXP.INS().key("user_id", userId).andKey("prize_id", prizeId), count, offset);
		}
	}
	
	@POSTAPI( //
			path = "getInviteUserListByUserId", //
			des = "查询用户指定抽奖活动邀请的好友", //
			ret = "" //
	)
	public JSONArray getInviteUserListByUserId(
		@P(t = "用户id")Long userId,
		@P(t = "抽奖活动id")Long prizeId,
		Integer count,
		Integer offset
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			return userRepository.getInviteUserListByUserId(conn, userId, prizeId, count, offset);
		}
	}
}
