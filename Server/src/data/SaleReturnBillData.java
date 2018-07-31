package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.SalesReturnBillDataService;
import po.billpo.SalesItemsPO;
import po.billpo.SalesReturnBillPO;
public class SaleReturnBillData extends UnicastRemoteObject implements SalesReturnBillDataService{
	private String billName="SalesReturnBill";
	private String recordName="SalesReturnRecord";
	private String[] billAttributes={"SRBID","SRBCustomerID","SRBSalesmanName","SRBOperatorID","SRBOriginalSum",
			"SRBReturnSum","SRBRemark","SRBOriginalSBID","generateTime","SRBCondition"};

	public SaleReturnBillData() throws RemoteException {
		super();

	}

	@Override
	public boolean saveBill(SalesReturnBillPO bill) throws RemoteException {
		boolean isExist=BillDataHelper.isBillExist(billName, billAttributes[0], bill);
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
	public boolean deleteBill(String id) throws RemoteException {
		return BillDataHelper.deleteBill(id);
	}

	@Override
	public String getNewId() throws RemoteException {	
		return BillDataHelper.getNewBillId(billName,billAttributes[0]);		
	}

	@Override
	public SalesReturnBillPO getBillById(String id) throws RemoteException {
		return BillDataHelper.getSalesReturnBill(id);
	}

	@Override
	public ArrayList<SalesReturnBillPO> getBillsByDate(String from, String to) throws RemoteException {
		ArrayList<SalesReturnBillPO> bills=new ArrayList<SalesReturnBillPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM "+billName+
					" WHERE generateTime>='"+from+"' AND generateTime<DATEADD(DAY,1,"+"'"+to+"');");
			while(r.next()) bills.add(BillDataHelper.getSalesReturnBill(r.getString(billAttributes[0])));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return bills;
		}
	}
	
	private boolean addBill(SalesReturnBillPO bill){
		ArrayList<SalesItemsPO> items=bill.getSalesReturnBillItems();
		Object[] billValues={bill.getAllId(), bill.getCustomerId(), bill.getSalesManName(),
				bill.getOperator(), bill.getOriginalSum(), bill.getReturnSum(), bill.getRemark(),
				bill.getOriginalSBId(), bill.getDate() + " "+bill.getTime(), bill.getState()};
		
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2 = true;
		for(int i=0;i<items.size();i++){
			b2 = b2 && SQLQueryHelper.add(recordName, bill.getAllId()
					,items.get(i).getComId(),items.get(i).getComQuantity(),items.get(i).getComPrice(),items.get(i).getComSum()
					,items.get(i).getComRemark());
		}
		return b1 && b2;
	}
}
