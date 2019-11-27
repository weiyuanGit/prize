package zyxhj.cn.Prize;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.repository.WinningListRepository;
import zyxhj.prize.service.PrizeService;
import zyxhj.utils.Singleton;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;

public class AppTest {
	private static DruidPooledConnection conn;
	private static PrizeService prizeService;
	private static WinningListRepository winningListRepository;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			conn = DataSource.getDruidDataSource("rdsDefault.prop").getConnection();
			prizeService = new PrizeService(null);
			winningListRepository = Singleton.ins(WinningListRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		conn.close();
	}
	@Test
	public void te() {
		try {
//			Long n = 1L;
//			for(int i=0;i<20;i++) {
////				prizeService.createWinning(1001L, i+n);				
//			}
//			prizeService.payPrize(winningListRepository.getList(conn, EXP.INS().key("prize_id",1001), 500, 0),conn);
//		System.out.println(winningListRepository.getList(conn,EXP.INS().key("prize_id",1001), 200, 0));
			int[] num = {1,2,3};
			prizeService.payPrize(num,prizeService.getWinningList(1001L, null, null, null, null, null, 500, 0),conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void tea() {}
}
