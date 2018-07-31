package businesslogic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import blservice.LogBLService;
import blservice.infoservice.GetUserInterface;
import businesslogic.inter.AddLogInterface;
import dataservice.LogDataService;
import ds_stub.LogDs_stub;
import po.LogInfoPO;
import presentation.main.MainWindow;
import rmi.Rmi;
import vo.MyTableModel;
import vo.UserVO;

public class LogBL implements LogBLService, AddLogInterface {

	private LogDataService logData = Rmi.flag ? Rmi.getRemote(LogDataService.class) : new LogDs_stub();
	private UserVO user;
	
	public LogBL(UserVO user) {
		this.user = user;
	}
	public LogBL() {
		this.user = MainWindow.getUser();
	}
	@Override
	public MyTableModel getLogInfo() {
		return listToTable(logData.getAllInfo());
	}

	@Override
	public MyTableModel searchByTime(String startTime, String endTime, String type) {
		ArrayList<LogInfoPO> list = logData.getAllInfo(startTime, endTime);
		if ("全部".equals(type)) return listToTable(list);
		Iterator<LogInfoPO> iterator = list.iterator();
		while(iterator.hasNext()) {
			LogInfoPO info = iterator.next();
			if(!type.equals(info.getOperation())) iterator.remove();
		}
		return listToTable(list);
	}
	/**
	 * 将日志记录列表转换为表格
	 * @param list 从数据层获取的日志记录list
	 * @return 表格模型
	 */
	private MyTableModel listToTable(ArrayList<LogInfoPO> list) {
		GetUserInterface getUserInfo = new UserBL();
		String[] attributes={"操作时间","操作员","操作类型","详情"};
		if (list == null) {
			String[][] info = {{"", "", "", ""}};
			return new MyTableModel(info, attributes);
		}
		
		String[][] info = new String[list.size()][4];
		for (int i = 0; i < list.size(); i++) {
			LogInfoPO logInfo= list.get(i);
			info[i][0] = logInfo.getTime();
			info[i][1] = getUserInfo.getUser(logInfo.getOperatorId()).getName();
			info[i][2] = logInfo.getOperation();
			info[i][3] = logInfo.getDetail();		
		}
		return new MyTableModel(info, attributes);
	}

	@Override
	public boolean add(String operation, String detail) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		String time = sdf.format(dt);
		return logData.add(new LogInfoPO(time, user.getId(), operation, detail));
	}
}
