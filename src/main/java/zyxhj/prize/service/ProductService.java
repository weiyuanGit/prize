package zyxhj.prize.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;

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
	
	/**
	 * 查询用户抽中的商品
	 */
	@POSTAPI(//
		path = "getProducts", 
		des = "查询用户抽中的商品", 
		ret = "" 
	)
	public JSONObject getProducts(
		@P(t = "抽奖活动编号") Long PrizeId,
		@P(t = "用户编号") Long winningUserId,
		@P(t = "用户是否中奖") Boolean isWinning
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return productRepository.getProducts(conn, PrizeId, winningUserId, isWinning);
		}
	}
	
	/**
	 * 根据商品id查询商品
	 */
	@POSTAPI(//
			path = "getProductById", 
			des = "根据商品id查询商品", 
			ret = "" 
	)
	public Product getProductById(
		@P(t = "商品id")Long productId
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return productRepository.get(conn, EXP.INS().key("product_id", productId));
		}
	}
	
	/**
	 * 修改商品信息
	 */
	@POSTAPI(//
			path = "updateProductById", 
			des = "修改商品信息", 
			ret = "" 
	)
	public void updateProductById(
		@P(t = "商品id")Long productId,
		@P(t = "商品名称", r = false)String productName,
		@P(t = "商品图片", r = false)String productImg
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			Product t = new Product();
			t.productName = productName;
			t.productImg = productImg;
			productRepository.update(conn, EXP.INS().key("product_id", productId), t, true);
		}
	}
	
	/**
	 * 添加商品信息
	 */
	@POSTAPI(//
			path = "createProducts", 
			des = "添加商品信息", 
			ret = "" 
	)
	public List<Long> createProducts(
		@P(t = "商品名称1")String productName1,
		@P(t = "商品图片1")String productImg1,
		@P(t = "商品名称2", r = false)String productName2,
		@P(t = "商品图片2", r = false)String productImg2,
		@P(t = "商品名称3", r = false)String productName3,
		@P(t = "商品图片3", r = false)String productImg3
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			Product t1 = new Product();
			Product t2 = null;
			Product t3 = null;
			t1.productId = IDUtils.getSimpleId();
			t1.productName = productName1;
			t1.productImg = productImg1;
			productRepository.insert(conn, t1);
			if(productImg2!=null && productName2!=null) {
				t2 = new Product();
				t2.productId = IDUtils.getSimpleId();
				t2.productName = productName2;
				t2.productImg = productImg2;
				productRepository.insert(conn, t2);
			}
			if(productImg3!=null && productName3!=null) {
				t3 = new Product();
				t3.productId = IDUtils.getSimpleId();
				t3.productName = productName3;
				t3.productImg = productImg3;
				productRepository.insert(conn, t3);
			}
			if(t2==null) {
				return Arrays.asList(t1.productId);
			}else{
				if(t3==null) {
					return Arrays.asList(t1.productId,t2.productId);
				}else {
					return Arrays.asList(t1.productId, t2.productId, t3.productId);
				}
			}
		}
	}
//	@POSTAPI(//
//			path = "delProduct", //
//			des = "删除商品", //
//			ret = "" //
//	)
//	public int setProductPrice(
//			@P(t = "所属模块编号")Long moduleId,
//			@P(t = "商品id")Long id
//	) throws Exception {
//		try (DruidPooledConnection conn = ds.getConnection()) {	
//			EXP exp = EXP.INS().key("module_id",moduleId).andKey("id", id);
//			return productRepository.delete(conn, exp);
//		}
//	}
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
