package businesslogic.inter;

public interface AddLogInterface {

	/**
	 * 向数据库中添加一条操作记录
	 * @param operation 操作名称
	 * @param detail 详情
	 * @return 是否添加成功
	 */
	public boolean add(String operation, String detail);
	
}
