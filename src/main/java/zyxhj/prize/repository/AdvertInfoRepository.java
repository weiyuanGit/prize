package zyxhj.prize.repository;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.domain.AdvertInfo;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.utils.data.rds.RDSRepository;

public class AdvertInfoRepository extends RDSRepository<AdvertInfo>{
	private DruidDataSource ds;
	public  AdvertInfoRepository() {
		super(AdvertInfo.class);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void updateAppStartAdvert(Long moduleId) throws SQLException, ServerException {
		AdvertInfo t = new AdvertInfo();
		t.status = AdvertInfo.STATUS_CLOSE;
		try(DruidPooledConnection conn = ds.getConnection()){
			this.update(conn, EXP.INS().key("type", AdvertInfo.TYPE_START).andKey("module_id", moduleId),t,true);
		}
	}
}
