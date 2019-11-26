package zyxhj.prize.service;

import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.domain.Prize;
import zyxhj.prize.domain.WinningList;
import zyxhj.prize.repository.PrizeRepository;
import zyxhj.prize.repository.PrizeUserRepository;
import zyxhj.prize.repository.WinningListRepository;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.RC;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;

public class PrizeService extends Controller{
	private PrizeRepository prizeRepository;
	private PrizeUserRepository userRepository;
	private WinningListRepository winningListRepository;
	private DruidDataSource ds;
	public PrizeService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			prizeRepository = Singleton.ins(PrizeRepository.class);
			userRepository = Singleton.ins(PrizeUserRepository.class);
			winningListRepository = Singleton.ins(WinningListRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@POSTAPI(//
			path = "createPrize", //
			des = "创建抽奖", //
			ret = "" //
	)
	public void createProduct(
		@P(t = "一等奖id")Long fristPrizeId,
		@P(t = "一等奖数量")int fristPrizeNum,
		@P(t = "二等奖id")Long secondPrizeId,
		@P(t = "二等奖数量")int secondPrizeNum,
		@P(t = "三等奖id")Long threePrizeId,
		@P(t = "三等奖数量")int threePrizeNum,
		@P(t = "指定用户中奖")Long prizeUserId,
		@P(t = "开奖方式")Byte prizeWay,
		@P(t = "开奖时间")Long prizeTime,
		@P(t = "抽奖说明")String prizeExplain,
		@P(t = "发起人id")Long createUserId,
		@P(t = "图文介绍")String imageText,
		@P(t = "领奖方式")Byte receiveWay,
		@P(t = "助力倍数")int helpTimes,
		@P(t = "是否开启好友助力")Boolean friendHelp
	) throws Exception {
		Prize p = new Prize();
		p.prizeId = IDUtils.getSimpleId();
		p.fristPrizeId = fristPrizeId;
		p.fristPrizeNum = fristPrizeNum;
		p.secondPrizeId = secondPrizeId;
		p.secondPrizeNum = secondPrizeNum;
		p.prizeUserId = prizeUserId;
		p.prizeStatus = Prize.STATUS_OPEN;
		p.prizeWay = Prize.PRIZEWAY_TIMEING;
		p.prizeTime = prizeTime;
		p.prizeExplain = prizeExplain;
		p.createUserId = createUserId;
		p.imageText = imageText;
		p.receiveWay = receiveWay;
		p.helpTimes = helpTimes;
		p.friendHelp = friendHelp;
		p.createTime = new Date();
		try(DruidPooledConnection conn = ds.getConnection()){
			prizeRepository.insert(conn, p);
		}
	}
	@POSTAPI(//
			path = "getPrizeList", //
			des = "查询抽奖列表", //
			ret = "" //
	)
	public List<Prize> getPrizeList(
		@P(t = "状态",r = false)Byte status,
		@P(t = "开奖方式",r = false)Byte prizeWay,
		@P(t = "发起人id",r = false)Long createUserId,
		Integer count,
		Integer offset
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			return prizeRepository.getList(conn, EXP.INS().key("prize_status", status==null?Prize.STATUS_GO:status).andKey("prize_way", prizeWay).andKey("create_user_id", createUserId), count, offset);
		}
	}
	@POSTAPI(//
			path = "getPrize", //
			des = "查询抽奖详情", //
			ret = "" //
	)
	public Prize getPrize(
		@P(t = "id")Long id
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			return prizeRepository.get(conn, EXP.INS().key("prize_id", id));
		}
	}
	
	@POSTAPI(//
			path = "clickPrize", //
			des = "点击参加抽奖", //
			ret = "" //
	)
	public APIResponse clickPrize(
		@P(t = "用户id")Long userId,
		@P(t = "抽奖信息id")Long prizeId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			//判断是否有用户信息
			//判断时间
			//判断是否已经参与
			if(userRepository.get(conn, EXP.INS().key("user_id", userId)) == null) {
				return APIResponse.getNewFailureResp(new RC("fail", "没有找到你的用户信息，请先登录"));
			}else if(getPrize(prizeId).prizeStatus.equals(Prize.STATUS_CLOSE)) {
				return APIResponse.getNewFailureResp(new RC("fail", "抽奖已经结束"));
			}else if(winningListRepository.get(conn, EXP.INS().key("winning_user_id", userId).andKey("Prize_id", prizeId)) != null) {
				return APIResponse.getNewFailureResp(new RC("fail", "你已经参加过了"));
			}else {
				WinningList w = new WinningList();
				w.winningId = IDUtils.getSimpleId();
				w.PrizeId = prizeId;
				w.winningUserId = userId;
				w.winningRate = 1;
				w.winningStatus = WinningList.STATUS_OPEN;
				w.isWinning = false;
				w.productId = null;
				w.winningGrade = null;
				w.createTime = new Date();
				winningListRepository.insert(conn, w);
				return APIResponse.getNewSuccessResp(w);
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
