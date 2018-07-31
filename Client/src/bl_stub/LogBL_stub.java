package bl_stub;

import blservice.LogBLService;
import vo.MyTableModel;

public class LogBL_stub implements LogBLService {

	@Override
	public MyTableModel getLogInfo() {
		String[] attributes={"操作时间","操作员","操作类型","详情"};
		String[][] info={{"2017-11-06 12:48:32","他","新建进货单","…………"},
				{"2017-11-06 12:32:32","他","增加商品","…………"}};
		System.out.println("得到操作记录");
		return new MyTableModel(info, attributes);
	}

	@Override
	public MyTableModel searchByTime(String startTime, String endTime, String type) {
		String[] attributes={"操作时间","操作员","操作类型","详情"};
		String[][] info={{"2017-11-06 12:48:32","他","新建进货单","…………"}};
		System.out.println("得到操作记录");
		return new MyTableModel(info, attributes);
	}

}
