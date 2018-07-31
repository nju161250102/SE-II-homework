package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.AccountPO;

public interface AccountDataService extends Remote {

	/**
	 * 根据账户的完整ID返回一个AccountPO对象，不论这个账户是否被删除<br/>
	 * 找不到就返回一个null...
	 * @param id 账户的id [id格式：四位数字字符串，如0001]
	 * @return 查找到的AccountPO对象
	 * @throws RemoteException
	 */
	public AccountPO findById(String id) throws RemoteException;
	/**
	 * 向数据库中增加一个AccountPO对象
	 * @param account 打包好的AccountPO对象
	 * @return 是否增加成功(数据库读写中出错返回false)(id已经存在返回false)
	 * @throws RemoteException
	 */
	public boolean add(AccountPO account) throws RemoteException;
	/**
	 * 向数据库中删除一个AccountPO对象
	 * @param id 需要删除的账户的唯一id [id格式：四位数字字符串，如0001]
	 * @return 是否删除成功(数据库操作中出错返回false)(id不存在返回false)
	 * @throws RemoteException
	 */
	public boolean delete(String id) throws RemoteException;
	/**
	 * 更新数据库中一个已经存在的AccountPO对象
	 * @param account 需要更新的AccountPO对象
	 * @return 是否更新成功(数据库操作中出错返回false)(id不存在返回false)
	 * @throws RemoteException
	 */
	public boolean update(AccountPO account) throws RemoteException;
	/**
	 * 获取数据库中所有未被删除的AccountPO对象列表
	 * @return 数据库中所有未被删除的AccountPO对象
	 * @throws RemoteException
	 */
	public ArrayList<AccountPO> getAllAccount() throws RemoteException;
	/**
	 * 查询符合条件的AccountPO记录
	 * @param field 查询的字段名，需要和数据库保持一致！
	 * @param content 查询的内容，非法字符由客户端过滤！
	 * @param isfuzzy 是否资磁模糊查找，true表示资磁（这是最吼的！）
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<AccountPO> getAccountsBy(String field, String content, boolean isfuzzy) throws RemoteException;
}
