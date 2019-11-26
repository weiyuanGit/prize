package zyxhj.prize.domain;

import java.util.Date;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 
 * 商品（复用）
 */
@RDSAnnEntity(alias = "tb_shop_product")
public class Product {

	public static final Byte STATUS_ENABLE = 0;
	public static final Byte STATUS_DISABLE = 1;

	public static final Byte TYPE_EXPRESS = 0;// 快递物流
	public static final Byte TYPE_DELIVER = 1;// 自行配送
	public static final Byte TYPE_AUTOMATIC = 2;// 自动（电子商品等等）

	/**
	 * 所属模块编号
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;

	/**
	 * 所属商店编号
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long storeId;

	/**
	 * 商品编号
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;

	/**
	 * 状态
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte status;

	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;

	/**
	 * 标题
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String title;

	/**
	 * 库存
	 */
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer stock;

	/**
	 * 成本价
	 */
	@RDSAnnField(column = RDSAnnField.DOUBLE)
	public Double costPrice;

	/**
	 * 市场价
	 */
	@RDSAnnField(column = RDSAnnField.DOUBLE)
	public Double marketPrice;

	/**
	 * 售价
	 */
	@RDSAnnField(column = RDSAnnField.DOUBLE)
	public Double price;

	/**
	 * 会员价
	 */
	@RDSAnnField(column = RDSAnnField.DOUBLE)
	public Double memberPrice;

	/**
	 * 商品描述内容
	 */
	@RDSAnnField(column = RDSAnnField.TEXT)
	public String data;

	/**
	 * 标签
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String tags;

	/**
	 *商品规格信息
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String sku;

	@AnnDicField(alias = "上架中")
	public static final Byte STATUS_UP = 0;
	@AnnDicField(alias = "已下架")
	public static final Byte STATUS_DOWN = 1;
}
