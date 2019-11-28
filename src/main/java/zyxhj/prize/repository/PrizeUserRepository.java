package zyxhj.prize.repository;

import java.util.Arrays;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;

import zyxhj.prize.domain.PrizeUser;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.rds.RDSRepository;

public class PrizeUserRepository extends RDSRepository<PrizeUser>{
	public PrizeUserRepository() {
		super(PrizeUser.class);
	}
	
	public JSONArray getUserListByPrizeId(DruidPooledConnection conn, Long PrizeId, Boolean isWinning, Byte winningGrade, Integer count, Integer offset) throws ServerException {
		String sql = "SELECT * FROM tb_prize_prizeuser u JOIN tb_prize_winninglist w ON u.user_id=w.winning_user_id WHERE w.prize_id=?";
		if(isWinning!=null&&winningGrade==null) {
			sql += " and w.is_winning=?";
			return PrizeUserRepository.sqlGetJSONArray(conn, sql, Arrays.asList(PrizeId,isWinning), count, offset);
		}
		if(winningGrade!=null&&isWinning==null) {
			sql += " and w.winning_grade=?";
			return PrizeUserRepository.sqlGetJSONArray(conn, sql, Arrays.asList(PrizeId,winningGrade), count, offset);
		}
		if(winningGrade!=null&&isWinning!=null) {
			sql += " and w.is_winning=? and w.winning_grade=?";
			return PrizeUserRepository.sqlGetJSONArray(conn, sql, Arrays.asList(PrizeId,isWinning,winningGrade), count, offset);
		}
		return PrizeUserRepository.sqlGetJSONArray(conn, sql, Arrays.asList(PrizeId), count, offset);
	}
	
	public JSONArray getInviteUserListByUserId(DruidPooledConnection conn,Long userId, Long PrizeId, Integer count, Integer offset) throws ServerException {
		String sql = "SELECT * FROM tb_prize_friendinvite f JOIN tb_prize_prizeuser u ON f.invite_user_id=u.user_id WHERE f.user_id=? and f.prize_id=?";
		return PrizeUserRepository.sqlGetJSONArray(conn, sql, Arrays.asList(userId,PrizeId), count, offset);
	}
}
