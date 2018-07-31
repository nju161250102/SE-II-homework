package blservice;

import vo.MyTableModel;

public interface LogBLService {

	/**
	 * 得到所有操作记录
	 * @return 直接用于表格显示的TableModel
	 */
	public MyTableModel getLogInfo();
	/**
	 * 根据时间区间搜索操作记录
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @return 直接用于表格显示的TableModel
	 */
	public MyTableModel searchByTime(String startTime, String endTime, String type);
}
