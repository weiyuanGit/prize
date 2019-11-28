package zyxhj.cn.start;

import com.alibaba.druid.pool.DruidDataSource;

import zyxhj.prize.domain.FriendInvite;
import zyxhj.prize.domain.Prize;
import zyxhj.prize.domain.PrizeUser;
import zyxhj.prize.domain.Product;
import zyxhj.prize.domain.WinningList;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.rds.RDSUtils;

public class Test {

	public static void main(String[] args) {

		testDB();

	}

	private static void testDB() {
		System.out.println("testDB");

		try {
			DruidDataSource dds = DataSource.getDruidDataSource("rdsDefault.prop");
//			RDSUtils.dropTableByEntity(dds, InterestTag.class);
//			RDSUtils.createTableByEntity(dds,FriendInvite.class);
//			RDSUtils.createTableByEntity(dds,Prize.class);
//			RDSUtils.createTableByEntity(dds,PrizeUser.class);
//			RDSUtils.createTableByEntity(dds,WinningList.class);
			RDSUtils.createTableByEntity(dds,Product.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
}
