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
@RDSAnnEntity(alias = "tb_prize_product")
public class Product {

	//商品id
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long productId;
	
	//商品名称
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String productName;
	
	//商品图片
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String productImg;
}
