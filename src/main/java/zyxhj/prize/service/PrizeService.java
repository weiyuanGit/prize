package zyxhj.prize.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.prize.domain.Prize;
import zyxhj.prize.domain.TempPrize;
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
import zyxhj.utils.data.rds.RDSAnnField;

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
		@P(t = "指定用户中奖")String prizeUserId,
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
			path = "updatePrize", //
			des = "修改抽奖", //
			ret = "" //
	)
	public void updatePrize(
		@P(t = "id")Long id,	
		@P(t = "一等奖id",r = false)Long fristPrizeId,
		@P(t = "一等奖数量",r = false)int fristPrizeNum,
		@P(t = "二等奖id",r = false)Long secondPrizeId,
		@P(t = "二等奖数量",r = false)int secondPrizeNum,
		@P(t = "三等奖id",r = false)Long threePrizeId,
		@P(t = "三等奖数量",r = false)int threePrizeNum,
		@P(t = "指定用户中奖",r = false)String prizeUserId,
		@P(t = "开奖方式",r = false)Byte prizeWay,
		@P(t = "开奖时间",r = false)Long prizeTime,
		@P(t = "抽奖说明",r = false)String prizeExplain,
		@P(t = "发起人id",r = false)Long createUserId,
		@P(t = "图文介绍",r = false)String imageText,
		@P(t = "领奖方式",r = false)Byte receiveWay,
		@P(t = "助力倍数",r = false)int helpTimes,
		@P(t = "是否开启好友助力",r = false)Boolean friendHelp
	) throws Exception {
		Prize p = new Prize();
		p.prizeId = IDUtils.getSimpleId();
		p.fristPrizeId = fristPrizeId;
		p.fristPrizeNum = fristPrizeNum;
		p.secondPrizeId = secondPrizeId;
		p.secondPrizeNum = secondPrizeNum;
		p.prizeUserId = prizeUserId;
		p.prizeTime = prizeTime;
		p.prizeExplain = prizeExplain;
		p.createUserId = createUserId;
		p.imageText = imageText;
		p.receiveWay = receiveWay;
		p.helpTimes = helpTimes;
		p.friendHelp = friendHelp;
		p.createTime = new Date();
		try(DruidPooledConnection conn = ds.getConnection()){
			prizeRepository.update(conn, EXP.INS().key("prize_id", id), p, true);
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
			path = "getWinning", //
			des = "查询抽奖记录详情", //
			ret = "" //
	)
	public WinningList getWinning(
		@P(t = "id")Long id
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			return winningListRepository.get(conn, EXP.INS().key("winning_id", id));
		}
	}
	public void setWinningStatus(int grade,List<Long> list) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			WinningList winn = new WinningList();
		    winn.winningStatus = WinningList.STATUS_OPEN;
		    winn.isWinning = true;
		    winn.winningGrade = (byte)grade;
		    winningListRepository.update(conn, EXP.INS().and(EXP.INS().IN("winning_id", list.toArray())), winn,true);
		}
	}
	@POSTAPI(//
			path = "getWinningList", //
			des = "查询抽奖记录记录", //
			ret = "" //
	)
	public List<WinningList>  getWinningList(
			@P(t = "抽奖id")Long PrizeId,
			@P(t = "用户id",r= false)Long winningUserId,
			@P(t = "状态",r= false)Byte winningStatus,
			@P(t = "奖品id",r= false)Long productId,
			@P(t = "奖品等级",r= false)Byte winningGrade,
			@P(t = "是否中奖",r= false)Byte isWinning,
			Integer count,
			Integer offset
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			EXP exp =  EXP.INS(false).key("prize_id", PrizeId).andKey("winning_user_id", winningUserId).andKey("winning_status", winningStatus)
					.andKey("product_id", productId).andKey("winning_grade", winningGrade).andKey("is_winning", isWinning);
			exp.append("ORDER BY winning_id ASC");
			return winningListRepository.getList(conn,exp,count,offset);
		}
	}
	/*
	 * 创建抽奖记录
	 */
	public WinningList createWinning(
		@P(t = "抽奖信息id")Long prizeId,
		@P(t = "用户id")Long userId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			WinningList w = new WinningList();
			w.winningId = IDUtils.getSimpleId();
			w.PrizeId = prizeId;
			w.winningUserId = userId;
			w.winningRate = 1;
			w.winningStatus = WinningList.STATUS_OPEN;
			w.isWinning = false;
			w.productId = 1L;
			w.winningGrade = 1;
			w.createTime = new Date();
			winningListRepository.insert(conn, w);
			return w;
		}
	}
	
	
	@POSTAPI(//
			path = "clickPrize", //
			des = "参与抽奖", //
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
				return APIResponse.getNewSuccessResp(createWinning(prizeId,userId));
			}
		}
	}
		@POSTAPI(//
				path = "startPrize", //
				des = "开始抽奖", //
				ret = "" //
		)
		public APIResponse startPrize(
			@P(t = "用户id")Long userId,
			@P(t = "抽奖信息id")Long prizeId
		) throws Exception {
			try(DruidPooledConnection conn = ds.getConnection()){
				//判断用户信息是否可以开奖
				Prize prize = prizeRepository.get(conn, EXP.INS().key("prize_id", prizeId));
				if(!prize.createUserId.equals(userId)) {
					return APIResponse.getNewFailureResp(new RC("fail","你没有权限开奖"));
				}else {
					//抽奖
					Prize p = new Prize();
					p.prizeStatus = Prize.STATUS_CLOSE;
					prizeRepository.update(conn, EXP.INS().key("prize_id", prizeId),p,true);//更改状态
					if(p.prizeUserId != null && p.prizeUserId.length()>0) {//有用户指定中奖
						
					}
					//抽奖
					int[] num = {prize.fristPrizeNum,prize.secondPrizeNum,prize.threePrizeNum};
					payPrize(num,winningListRepository.getList(conn, EXP.INS().key("prize_id", prize.prizeId), 500, 0),conn);
				}
			}
			return null;
	}
	public void payPrize(int num[],List<WinningList> winnings,DruidPooledConnection conn) throws Exception {
		int max = winnings.size();
		int min = 1;
		System.out.println(max+"**"+min);
		List<Long> oneList = new ArrayList<Long>();
		List<Long> twoList = new ArrayList<Long>();
		List<Long> threeList = new ArrayList<Long>();
		for(int i=0;i<num[0];i++) {
			int ranNum = (int) (Math.random()*(max-min)+min);
			oneList.add(winnings.get(ranNum).winningId);
		}
		for(int i=0;i<num[1];i++) {
			int ranNum = (int) (Math.random()*(max-min)+min);
			if(oneList.contains(winnings.get(ranNum).winningId)) {
				i--;
				continue;
			}
			twoList.add(winnings.get(ranNum).winningId);
		}
		for(int i=0;i<num[2];i++) {
			int ranNum = (int) (Math.random()*(max-min)+min);
			if(oneList.contains(winnings.get(ranNum).winningId)||twoList.contains(winnings.get(ranNum).winningId)) {
				i--;
				continue;
			}
			threeList.add(winnings.get(ranNum).winningId);
		}
		setWinningStatus(1, oneList);
		setWinningStatus(2, twoList);
		setWinningStatus(3, threeList);
	}
}
