package blservice;

import vo.MyTableModel;

public interface DataBLService {

    /**
     * 生成一条新的对应数据的id
     * @return 新id
     */
    public String getNewId();
	/**
	 * 根据唯一的id编号删除一条信息
	 * @param id 能唯一确定的id编号
	 * @return 成功删除返回true
	 */
	public boolean delete(String id);
	/**
	 * 确定搜索方式和关键词，得到搜索结果
	 * @param type 搜索方式
	 * @param key 关键词
	 * @return 返回表格的模型
	 */
	public MyTableModel search(String type, String key);
	/**
	 * 更新表格模型
	 * @return 更新后的表格模型
	 */
	public MyTableModel update();
	
}
