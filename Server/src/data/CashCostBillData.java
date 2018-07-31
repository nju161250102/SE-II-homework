package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dataservice.CashCostBillDataService;
import po.billpo.CashCostBillPO;
import po.billpo.CashCostItem;
public class CashCostBillData extends UnicastRemoteObject implements CashCostBillDataService{
	
	public CashCostBillData() throws RemoteException {
		super();
	}

	private String tableName="CashCostBill",recordName="CashCostRecord";
	private String[] billAttributes={"CCBID","CCBOperatorID","CCBAccountID","CCBSum","generateTime","CCBCondition"};

	@Override
	public boolean saveBill(CashCostBillPO bill) throws RemoteException{
		boolean isExist=BillDataHelper.isBillExist(tableName, billAttributes[0], bill);
		try{
			if(!isExist){
				return addBill(bill);
			}
			else{
				if(deleteBill(bill.getAllId()))return addBill(bill);
				else return false;
			}
		}catch(Exception e){
			  e.printStackTrace();
			  return false;
		}
	}
	
	@Override
	public boolean deleteBill(String billid) throws RemoteException{
		return BillDataHelper.deleteBill(billid);
	}

	@Override
	public String getNewId() throws RemoteException {
		return BillDataHelper.getNewBillId(tableName,billAttributes[0]);
	}

	@Override
	public CashCostBillPO getBillById(String id) throws RemoteException{
		return BillDataHelper.getCashCostBill(id);
	}
	
	private boolean addBill(CashCostBillPO bill){
		ArrayList<CashCostItem> items = bill.getCashcostList();
		boolean b1 = SQLQueryHelper.add(tableName, bill.getAllId()
				,bill.getOperator(),bill.getAccountId(),bill.getSum()
				,bill.getDate() + " "+bill.getTime(),bill.getState());
		boolean b2 = true;
		for(int i=0;i<items.size();i++) {
			b2 = b2 && SQLQueryHelper.add(recordName, bill.getAllId()
					,items.get(i).getName(),items.get(i).getMoney(),items.get(i).getRemark());
		}
		return b1 && b2;
	}
}
