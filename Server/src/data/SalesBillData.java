package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.SalesBillDataService;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
public class SalesBillData extends UnicastRemoteObject implements SalesBillDataService{
	private static final long serialVersionUID = -952253035895433810L;
	private String billName="SalesBill";
	private String recordName="SalesRecord";
	private String[] billAttributes={"SBID","SBCustomerID","SBSalesmanName","SBOperatorID","SBBeforeDiscount",
			"SBDiscount","SBCoupon","SBAfterDiscount","SBRemark","SBPromotionID","generateTime","SBCondition"};
	private String[] recordAttributes={"SRID","SRComID","SRComQuantity","SRPrice","SRComSum","SRRemark"};

	public SalesBillData() throws RemoteException {
		super();
		
	}

	@Override
	public boolean saveBill(SalesBillPO bill) throws RemoteException {
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
	public boolean deleteBill(String billid) throws RemoteException {
		return BillDataHelper.deleteBill(billid);
	}

	@Override
	public String getNewId() throws RemoteException {
		return BillDataHelper.getNewBillId(billName, billAttributes[0]);		
	}

	@Override
	public SalesBillPO getBillById(String id) throws RemoteException {
		return BillDataHelper.getSalesBill(id);
	}
	

	@Override
	public ArrayList<SalesBillPO> getBillsBy(String field, String key, boolean isFuzzy) throws RemoteException {
		ArrayList<SalesBillPO> bills=new ArrayList<SalesBillPO>();
		ResultSet res_Bill=null;
		try{
			if(isFuzzy){
				Statement s1 = DataHelper.getInstance().createStatement();
				res_Bill = s1.executeQuery("SELECT * FROM SalesBill WHERE "+field+" LIKE '%"+key+"%';");
			}
			else {
				res_Bill =SQLQueryHelper.getRecordByAttribute(billName, field, key);
			}
			while(res_Bill.next()) bills.add(BillDataHelper.getSalesBill(res_Bill.getString(billAttributes[0])));
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<SalesBillPO>();
		}
		return bills;
	}

	@Override
	public ArrayList<SalesBillPO> getBillByDate(String from, String to) throws RemoteException {
		ArrayList<SalesBillPO> bills=new ArrayList<SalesBillPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM "+billName+
					" WHERE generateTime>'"+from+"' AND generateTime<DATEADD(DAY,1,"+"'"+to+"');");
			while(r.next()) bills.add(BillDataHelper.getSalesBill(r.getString(billAttributes[0])));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean addBill(SalesBillPO bill){
		ArrayList<SalesItemsPO> items = bill.getSalesBillItems();
		Object[] billValues={bill.getAllId(), bill.getCustomerId(), bill.getSalesManName(),
				bill.getOperator(), bill.getBeforeDiscount(), bill.getDiscount(),
				bill.getCoupon(), bill.getAfterDiscount(), bill.getRemark(),
				bill.getPromotionId(), bill.getDate() + " "+bill.getTime(), bill.getState()};
		
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2 = true;
		for(int i=0;i<items.size();i++){
			b2 = b2 && SQLQueryHelper.add(recordName, bill.getAllId()
					,items.get(i).getComId(),items.get(i).getComQuantity(),items.get(i).getComPrice()
					,items.get(i).getComSum(),items.get(i).getComRemark());
		}
		return b1&&b2;
	}
	
}
