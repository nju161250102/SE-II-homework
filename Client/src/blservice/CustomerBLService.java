package blservice;

import vo.CustomerVO;

public interface CustomerBLService extends DataBLService {

	/**
	 * 增加一条客户信息的记录
	 * @param customer 客户VO
	 * @return 是否成功添加
	 */
	public boolean add(CustomerVO customer);
	/**
	 * 更改一条客户信息
	 * @param customer 客户VO
	 * @return 编号已存在并且成功更改返回true
	 */
	public boolean change(CustomerVO customer);
	
}
