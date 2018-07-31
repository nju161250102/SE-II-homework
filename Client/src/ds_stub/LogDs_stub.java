package ds_stub;

import java.util.ArrayList;

import dataservice.LogDataService;
import po.LogInfoPO;

public class LogDs_stub implements LogDataService {

	private static ArrayList<LogInfoPO> list = new ArrayList<LogInfoPO>();
	
	@Override
	public boolean add(LogInfoPO logInfo) {
		System.out.println("logInfo added");
		list.add(logInfo);
		return true;
	}

	@Override
	public ArrayList<LogInfoPO> getAllInfo() {
		return list;
	}

	@Override
	public ArrayList<LogInfoPO> getAllInfo(String startTime, String EndTime) {
		return null;
	}

}
