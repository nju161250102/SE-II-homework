package blservice;

import java.rmi.RemoteException;

import javax.swing.tree.DefaultTreeModel;

import vo.CategoryVO;
/**
 * 商品分类管理业务逻辑接口
 * @author 钱美缘
 *
 */
public interface CategoryBLService {

	/**
     * 生成一个新的商品编号
     * @return 新商品编号
     */
    public String getNewId();
    /**
	 * 根据商品分类的完整ID返回一个CategoryVO对象，不论这个商品分类是否被删除<br/>
	 * 找不到就返回一个null...
	 * @param id 商品分类的id [id格式：6位数字]
	 * @return 查找到的CategoryVO对象
	 * @throws RemoteException
	 */
	public CategoryVO findById(String id);
	/**
	 * 根据数据层的PO对象生成树供展示层显示
	 * @return 用于显示的TreeModel
	 */
	public DefaultTreeModel getModel();
	/**
	 * 增加一个节点</br>
	 * 只有叶节点的分类才能添加商品</br>
	 * 一旦分类下有商品就不能在该分类下再添加子分类
	 * @param category 界面层传递的VO对象
	 * @return 是否添加成功
	 */
	public boolean add(CategoryVO category);
	/**
	 * 删除一个节点</br>
	 * （那么如果已有商品能不能删除？？？）
	 * @param category 界面层传递的VO对象
	 * @return 是否删除成功
	 */
	public boolean delete(String id);
	/**
	 * 修改一个商品分类的信息（只能改名称。。。）
	 * @param category 界面层传递的VO对象
	 * @return 是否修改成功
	 */
	public boolean change(CategoryVO category);
	/**
	 * 根据ID查找父节点下是否有子节点
	 * @param id
	 * @return 
	 */
	public boolean hasContent(String id);
}
