package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.CashCostBillDataService;
import po.billpo.CashCostBillPO;

public class CashCostDs_stub implements CashCostBillDataService {

	@Override
	public boolean saveBill(CashCostBillPO bill) {
		return true;
	}

	@Override
	public boolean deleteBill(String billid) {
		return true;
	}

	@Override
	public String getNewId() {
		return "XJFYD-20171203-00001";
	}

	@Override
	public CashCostBillPO getBillById(String id) {
		return null;
	}

	@Override
	public ArrayList<CashCostBillPO> getBillsByDate(String from, String to) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
