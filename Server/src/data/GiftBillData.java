package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dataservice.GiftBillDataService;
import po.billpo.GiftBillPO;
import po.billpo.GiftItem;

public class GiftBillData extends UnicastRemoteObject implements GiftBillDataService {

	public GiftBillData() throws RemoteException {
		super();
	}
	
	private String billName="InventoryGiftBill";
	private String recordName="InventoryGiftRecord";
	private String[] billAttributes={"IGBID","IGBOperatorID","IGBCondition","generateTime","IGBSalesBillID","IGBCustomerID"};

	@Override
	public boolean add(GiftBillPO bill) throws RemoteException {
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
	public String getNewId() throws RemoteException {
		return BillDataHelper.getNewBillId(billName, billAttributes[0]);
	}

	@Override
	public GiftBillPO getById(String id) throws RemoteException {
		return BillDataHelper.getGiftBill(id);
	}
	
	private boolean deleteBill(String billid) throws RemoteException{
		return BillDataHelper.deleteBill(billid);
	}

	
	private boolean addBill(GiftBillPO bill){
		ArrayList<GiftItem> items = bill.getGifts();
		Object[] billValues={bill.getAllId(), bill.getOperator(), bill.getState(),
				bill.getDate() + " "+bill.getTime(), bill.getSalesBillId(), bill.getCustomerId()};
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2 = true;
		for(int i=0;i<items.size();i++){
			b2=b2&&SQLQueryHelper.add(recordName, bill.getAllId(), items.get(i).getComId(),
					items.get(i).getNum(), items.get(i).getPrice());
		}
		return b1&&b2;
	}

}
