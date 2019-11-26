package zyxhj.prize.repository;

import com.alibaba.druid.pool.DruidPooledConnection;

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

}



