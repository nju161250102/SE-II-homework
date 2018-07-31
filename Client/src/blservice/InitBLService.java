package blservice;

import vo.MyTableModel;

public interface InitBLService {

	/**
	 * 获取当前账套的年份
	 * @return 如果记录里没有当前账套则返回null
	 */
	public String getYear();
	/**
	 * 获得期初列表
	 * @return
	 */
	public String[] getInitInfo();
	/**
	 * 返回选中账套的商品信息，如果年份为null则返回当前信息
	 * @param year
	 * @return
	 */
	public MyTableModel getCommodityInfo(String year);
	
	public MyTableModel getCustomerInfo(String year);
	
	public MyTableModel getAccountInfo(String year);
	/**
	 * 期初信息初始化
	 * @return
	 */
	public boolean initNewOne();
}
