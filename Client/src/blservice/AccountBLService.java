package blservice;

import vo.AccountVO;

public interface AccountBLService extends DataBLService {

	/**
	 * 增加一条账户信息的记录
	 * @param account 账户VO
	 * @return 是否成功添加
	 */
	public boolean add(AccountVO account);
	/**
	 * 更改一条账户信息
	 * @param account 账户VO
	 * @return 编号已存在并且成功更改返回true
	 */
	public boolean change(AccountVO account);
	
}
