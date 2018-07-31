package blservice;

import vo.CommodityVO;

public interface CommodityBLService extends DataBLService {

	/**
	 * 增加一条商品信息的记录
	 * @param commodity 商品VO
	 * @return
	 */
	public boolean add(CommodityVO commodity);
	/**
	 * 更改一条商品信息
	 * @param commodity 商品VO
	 * @return
	 */
	public boolean change(CommodityVO commodity);
}
