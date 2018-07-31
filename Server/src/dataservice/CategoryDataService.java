package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.CategoryPO;

public interface CategoryDataService extends Remote {

	/**
	 * 新增商品分类时，获取商品应当持有的唯一id
	 * @return 下一个商品应当持有的id [id格式：]
	 * @throws RemoteException
	 */
	public String getNewId() throws RemoteException;
	/**
	 * 根据商品分类的完整ID返回一个CategoryPO对象，不论这个商品分类是否被删除<br/>
	 * 找不到就返回一个null...
	 * @param id 商品分类的id [id格式：6位数字]
	 * @return 查找到的CategoryPO对象
	 * @throws RemoteException
	 */
	public CategoryPO findById(String id) throws RemoteException;
	/**
	 * 向数据库中增加一个CategoryPO对象
	 * @param category 打包好的CategoryPO对象
	 * @return 是否增加成功(数据库读写中出错返回false)(id已经存在返回false)
	 * @throws RemoteException
	 */
	public boolean add(CategoryPO category) throws RemoteException;
	/**
	 * 向数据库中删除一个CategoryPO对象
	 * @param id 需要删除的商品分类的唯一id [id格式：四位数字字符串，如0001]
	 * @return 是否删除成功(数据库操作中出错返回false)(id不存在返回false)
	 * @throws RemoteException
	 */
	public boolean delete(String id) throws RemoteException;
	/**
	 * 更新数据库中一个已经存在的CategoryPO对象
	 * @param category 需要更新的CategoryPO对象
	 * @return 是否更新成功(数据库操作中出错返回false)(id不存在返回false)
	 * @throws RemoteException
	 */
	public boolean update(CategoryPO category) throws RemoteException;
	/**
	 * 获取数据库中所有未被删除的CategoryPO对象列表
	 * @return 数据库中所有未被删除的CategoryPO对象
	 * @throws RemoteException
	 */
	public ArrayList<CategoryPO> getAllCategory() throws RemoteException;
	/**
	 * 查询符合条件的CategoryPO记录
	 * @param field 查询的字段名，需要和数据库保持一致！
	 * @param content 查询的内容，非法字符由客户端过滤！
	 * @param isfuzzy 是否资磁模糊查找，true表示资磁（这是最吼的！）
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<CategoryPO> getCategorysBy(String field, String content, boolean isfuzzy) throws RemoteException;
}