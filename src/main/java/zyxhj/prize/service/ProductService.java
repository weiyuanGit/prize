package zyxhj.prize.service;

import java.util.Date;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.domain.Product;
import zyxhj.prize.repository.ProductRepository;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.Controller;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;

public class ProductService extends Controller{
	private DruidDataSource ds;
	private ProductRepository productRepository;
	public ProductService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			productRepository = Singleton.ins(ProductRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@POSTAPI(//
			path = "createProduct", //
			des = "创建商品", //
			ret = "" //
	)
	public void createProduct(
		@P(t = "所属模块编号")Long moduleId,
		@P(t = "所属商店编号",r=false)Long storeId,
		@P(t = "状态",r=false)Byte status,
		@P(t = "标题",r=false)String title,
		@P(t = "库存",r=false)Integer stock,
		@P(t = "成本价",r=false)Double costPrice,
		@P(t = "市场价",r=false)Double marketPrice,
		@P(t = "售价",r=false)Double price,
		@P(t = "会员价",r=false)Double memberPrice,
		@P(t = "商品描述内容",r=false)String data,
		@P(t = "标签",r=false)String tags,
		@P(t = "商品规格信息",r=false)String sku
		
	) throws Exception {
		Product p = new Product();
		p.id = IDUtils.getSimpleId();
		p.moduleId = moduleId;
		p.storeId = storeId;
		p.status = status;
		p.title = title;
		p.stock = stock;
		p.costPrice = costPrice;
		p.marketPrice = marketPrice;
		p.price = price;
		p.memberPrice = memberPrice;
		p.data = data;
		p.tags = tags;
		p.sku = sku;
		p.createTime = new Date();
		try(DruidPooledConnection conn = ds.getConnection()){
			productRepository.insert(conn, p);
		}
	}
	
	@POSTAPI(//
			path = "delProduct", //
			des = "删除商品", //
			ret = "" //
	)
	public int setProductPrice(
			@P(t = "所属模块编号")Long moduleId,
			@P(t = "商品id")Long id
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {	
			EXP exp = EXP.INS().key("module_id",moduleId).andKey("id", id);
			return productRepository.delete(conn, exp);
		}
	}
	

}
