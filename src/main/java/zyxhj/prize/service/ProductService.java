package zyxhj.prize.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.prize.domain.AdvertInfo;
import zyxhj.prize.domain.Product;
import zyxhj.prize.repository.AdvertInfoRepository;
import zyxhj.prize.repository.ProductRepository;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;

public class ProductService extends Controller{
	private DruidDataSource ds;
	private ProductRepository productRepository;
	private AdvertInfoRepository advertInfoRepository;
	public ProductService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			productRepository = Singleton.ins(ProductRepository.class);
			advertInfoRepository = Singleton.ins(AdvertInfoRepository.class);
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
	/**
	 * 创建广告
	 */
	@POSTAPI(//
		path = "createAdvert", 
		des = "创建广告", 
		ret = "" 
	)
	public AdvertInfo createAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "图片地址") String imgSrc,
		@P(t = "详情链接") String linkSrc,
		@P(t = "排序大小",r= false) int sortSize,
		@P(t = "备注") String remake,
		@P(t = "类型",r= false) Byte type,
		@P(t = "所属专栏",r= false) Long channelId,
		@P(t = "状态",r= false) Byte status
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			AdvertInfo t = new AdvertInfo();
			t.moduleId = moduleId;
			t.id = IDUtils.getSimpleId();
			t.imgSrc = imgSrc;
			t.linkSrc = linkSrc;
			t.createTime = new Date();
			t.remark = remake;
			t.sortSize = sortSize;
			if(status == null) {
				t.status = AdvertInfo.STATUS_OPEN;				
			}else {
				t.status = status;
			}
			t.type = type;
			t.channelId = channelId;
			advertInfoRepository.insert(conn, t);
			return t;
		}
		
	}
	/**
	 * 查询广告
	 */
	@POSTAPI(//
		path = "getAdverts", 
		des = "查询广告", 
		ret = "" 
	)
	public List<AdvertInfo> getAdverts(
		@P(t = "模块编号") Long moduleId,
		@P(t = "类型",r= false) Byte type,
		@P(t = "所属专栏",r= false) Long channelId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.getList(conn, EXP.INS(false).key("module_id", moduleId).andKey("type", type).andKey("channel_id", channelId)
					.andKey("status", AdvertInfo.STATUS_OPEN).append("order by sort_size desc"), count, offset);
		}
	}
	@POSTAPI(//
		path = "delAdvert", 
		des = "删除广告", 
		ret = "" 
	)
	public int delAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "id") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.delete(conn, EXP.INS().key("id", id).andKey("module_id", moduleId));
		}
	}
	@POSTAPI(//
		path = "updateAdvert", 
		des = "修改广告", 
		ret = "" 
	)
	public int updateAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "id") Long id,
		@P(t = "图片地址",r = false) String imgSrc,
		@P(t = "详情链接",r = false) String linkSrc,
		@P(t = "备注",r = false) String remake,
		@P(t = "状态",r = false) Byte status,
		@P(t = "排序大小",r = false) int sortSize,
		@P(t = "类型",r= false) Byte type,
		@P(t = "所属专栏",r= false) Long channelId
	) throws ServerException, SQLException {
		AdvertInfo t = new AdvertInfo();
		t.imgSrc = imgSrc;
		t.linkSrc = linkSrc;
		t.remark = remake;
		t.status = status;
		t.sortSize = sortSize;
		t.type = type;
		t.channelId = channelId;
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.update(conn, EXP.INS().key("id", id).andKey("module_id", moduleId),t,true);
		}
	}

}
