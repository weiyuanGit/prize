package zyxhj.prize.repository;

import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.domain.Prize;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.rds.RDSRepository;

public class PrizeRepository extends RDSRepository<Prize>{
	public PrizeRepository() {
		super(Prize.class);
	}
	public void setFristPrizeNum(DruidPooledConnection conn,Long id) throws ServerException {
		String sql = "UPDATE  tb_prize_prize SET frist_prize_num = frist_prize_num-1 WHERE prize_id ="+id;
		this.executeUpdateSQL(conn, sql, null);
	}
	public void setSecondPrizeNum(DruidPooledConnection conn,Long id) throws ServerException {
		String sql = "UPDATE  tb_prize_prize SET second_prize_num = second_prize_num-1  WHERE prize_id ="+id;
		this.executeUpdateSQL(conn, sql, null);
	}
	public void setThreePrizeId(DruidPooledConnection conn,Long id) throws ServerException {
		String sql = "UPDATE  tb_prize_prize SET three_prize_num = three_prize_num-1 WHERE prize_id ="+id;
		this.executeUpdateSQL(conn, sql, null);
	}

}
