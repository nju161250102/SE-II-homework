package blservice.infoservice;

import vo.CommodityVO;
import vo.MyTableModel;

public interface GetCommodityInterface {

	public CommodityVO getCommodity(String id);

	/**
	 * 返回某一分类下是否有商品
	 * @param categoryId 商品分类id
	 * @return
	 */
	public boolean hasCommodity(String categoryId);
	
	public MyTableModel getCategoryCommodities(String categoryId);
}
