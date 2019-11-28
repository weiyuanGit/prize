package zyxhj.prize.repository;

import java.util.Arrays;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;

import zyxhj.prize.domain.Product;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.rds.RDSRepository;

public class ProductRepository extends RDSRepository<Product>{

	public  ProductRepository() {
		super(Product.class);
	}
	
	public int setStock(DruidPooledConnection conn, Long id) throws ServerException {
		String sql = "update tb_shop_product set stock = stock-1 where id = "+id;
		int count = this.executeUpdateSQL(conn, sql,null);
		return count;
	}

	public JSONObject getProducts(DruidPooledConnection conn, Long PrizeId, Long winningUserId, Boolean isWinning) throws ServerException {
		String sql = "select * from tb_prize_winninglist w join tb_prize_product p on w.product_id=p.product_id where prize_id=? and winning_user_id=? and is_winning=?";
		return ProductRepository.sqlGetJSONObject(conn, sql, Arrays.asList(PrizeId,winningUserId,isWinning));
	}
}



