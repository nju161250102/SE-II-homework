package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.PurchaseBillDataService;
import po.billpo.PurchaseBillPO;
import po.billpo.SalesItemsPO;
public class PurchaseBillData extends UnicastRemoteObject implements PurchaseBillDataService{
	
	public PurchaseBillData() throws RemoteException {
		super();
	}

	private String billName="PurchaseBill";
	private String recordName="PurchaseRecord";
	private String[] billAttributes={"PBID","PBSupplierID","PBOperatorID","PBSum","PBRemark","PBCondition","generateTime"};
	private String[] recordAttributes={"PRID","PRComID","PRComQuantity","PRComSum","PRRemark","PRComPrice"};

	@Override
	public boolean saveBill(PurchaseBillPO bill) throws RemoteException {
		
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
		return BillDataHelper.getNewBillId(billName, billAttributes[0]);
	}

	@Override
	public PurchaseBillPO getBillById(String id) throws RemoteException {
		return BillDataHelper.getPurchaseBill(id);
	}

	@Override
	public ArrayList<PurchaseBillPO> getBillsBy(String field, String key, boolean isFuzzy) throws RemoteException {

		ArrayList<PurchaseBillPO> bills=new ArrayList<PurchaseBillPO>();
		ResultSet res_Bill=null;
		try{
			if(isFuzzy){
				Statement s1 = DataHelper.getInstance().createStatement();
			    res_Bill = s1.executeQuery("SELECT * FROM PurchaseBill WHERE "+field+" LIKE '%"+key+"%';");
			}
			else if(!isFuzzy) res_Bill =SQLQueryHelper.getRecordByAttribute(billName, field, key);
			while(res_Bill.next()) bills.add(BillDataHelper.getPurchaseBill(res_Bill.getString(billAttributes[0])));
		}catch(Exception e){
			e.printStackTrace();
			return bills;
		}
		return bills;
	}

	@Override
	public ArrayList<PurchaseBillPO> getBillByDate(String from, String to) throws RemoteException {
		ArrayList<PurchaseBillPO> bills=new ArrayList<PurchaseBillPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM "+billName+
					" WHERE generateTime>'"+from+"' AND generateTime<DATEADD(DAY,1,"+"'"+to+"');");
			while(r.next()) bills.add(BillDataHelper.getPurchaseBill(r.getString(billAttributes[0])));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return bills;
	}
	
	private boolean addBill(PurchaseBillPO bill){
		
		ArrayList<SalesItemsPO> items = bill.getPurchaseBillItems();
		Object[] billValues={bill.getAllId(), bill.getSupplierId(), bill.getOperator(), bill.getSum(),
				bill.getRemark(), bill.getState(), bill.getDate() + " "+bill.getTime()};
		
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2 = true;
		for(int i=0;i<items.size();i++){
			b2 = b2 && SQLQueryHelper.add(recordName, bill.getAllId()
					,items.get(i).getComId(),items.get(i).getComQuantity(),items.get(i).getComSum()
					,items.get(i).getComRemark(),items.get(i).getComPrice());
		}
		return b1 && b2;


	}
}
