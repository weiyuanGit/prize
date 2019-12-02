package zyxhj.prize.service;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
			path = "getOpenId", //
			des = "获取openid", //
			ret = "" //
	)
	public JSONObject getOpenId(
		@P(t = "小程序appId")String appid, 
		@P(t = "小程序appSecret")String secret, 
		@P(t = "登录时获取的code")String js_code, 
		@P(t = "授权类型，此处只需填写 authorization_code")String grant_type
	) throws Exception {
		// 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 定义请求的参数
        URI uri = new URIBuilder("https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type).build();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);
        //response 对象
        CloseableHttpResponse response = null;
        try {
        	String content = null;
            // 执行http get请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            return JSONObject.parseObject(content);
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
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
		@P(t = "用户手机号", r=false)String phoneNum
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
			}else {
				updateUserByOpenId(openId, userHead, userName, null);
			}
			if(inviterId != null) {
				createFriendInvite(inviterId, user.userId);
			}
			return APIResponse.getNewSuccessResp("登录成功");
		}
	}
	
	@POSTAPI( //
			path = "getUserByOpenId", //
			des = "根据用户openId获取用户信息", //
			ret = "" //
	)
	public PrizeUser getUserByOpenId(
		@P(t = "用户openId")String openId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			return userRepository.get(conn, EXP.INS().key("open_id", openId));
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
			path = "updateUserByOpenId", //
			des = "根据用户openId修改用户信息", //
			ret = "" //
	)
	public void updateUserByOpenId(
		@P(t = "用户openId")String openId,
		@P(t = "用户头像")String userHead,
		@P(t = "用户名")String userName,
		@P(t = "用户手机号", r=false)String phoneNum
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			PrizeUser t = new PrizeUser();
			t.userHead = userHead;
			t.userName = userName;
			t.phoneNum = phoneNum;
			userRepository.update(conn, EXP.INS().key("open_id", openId), t, true);
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
	
	@POSTAPI( //
			path = "isCreate", //
			des = "判断登录用户是否为发布用户", //
			ret = "" //
	)
	public void isCreate(
		@P(t = "抽奖活动id")Long prizeId,
		@P(t = "当前登录用户openId")Long openId
	) throws Exception{
		
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
