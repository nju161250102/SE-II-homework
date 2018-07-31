package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import dataservice.BillSearchDataService;
import po.UserPO;
import po.billpo.BillPO;
import po.billpo.CashCostBillPO;
import po.billpo.ChangeBillPO;
import po.billpo.ChangeItem;
import po.billpo.GiftBillPO;
import po.billpo.PaymentBillPO;
import po.billpo.PurchaseBillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.ReceiptBillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesReturnBillPO;

public class BillSearchData extends UnicastRemoteObject implements BillSearchDataService{
	public BillSearchData() throws RemoteException {
		super();
	}

	//查找所有状态的单据传state参数-1；
	@Override
	public ArrayList<PurchaseBillPO> searchPurchaseBills(String fromDate, String toDate, String customerId,
			String operatorId,int state) throws RemoteException {
		ArrayList<PurchaseBillPO> bills=new ArrayList<PurchaseBillPO>();
		try{
			ResultSet r = assembleSQL("PurchaseBill", fromDate, toDate,"PBCondition",state, 
					"PBSupplierID", customerId, "PBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getPurchaseBill(r.getString("PBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<PurchaseReturnBillPO> searchPurchaseReturnBills(String fromDate, String toDate, String customerId,
			String operatorId,int state) throws RemoteException {
		ArrayList<PurchaseReturnBillPO> bills=new ArrayList<PurchaseReturnBillPO>();
		try{
			ResultSet r = assembleSQL("PurchaseReturnBill", fromDate, toDate,"PRBCondition",state, 
					"PRBSupplierID", customerId, "PRBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getPurchaseRetrunBill(r.getString("PRBID")));
			return bills;	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<SalesBillPO> searchSalesBills(String fromDate, String toDate, String customerId, String operatorId,int state)
			throws RemoteException {
		ArrayList<SalesBillPO> bills=new ArrayList<SalesBillPO>();
		try{
			ResultSet r = assembleSQL("SalesBill", fromDate, toDate,"SBCondition",state, 
					"SBCustomerID", customerId, "SBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getSalesBill(r.getString("SBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<SalesReturnBillPO> searchSalesReturnBills(String fromDate, String toDate, String customerId,
			String operatorId,int state) throws RemoteException {
		ArrayList<SalesReturnBillPO> bills=new ArrayList<SalesReturnBillPO>();
		try{
			ResultSet r = assembleSQL("SalesReturnBill", fromDate, toDate,"SRBCondition",state,
					"SRBCustomerID", customerId, "SRBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getSalesReturnBill(r.getString("SRBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<CashCostBillPO> searchCashCostBills(String fromDate, String toDate, String customerId,
			String operatorId,int state) throws RemoteException {
		ArrayList<CashCostBillPO> bills=new ArrayList<CashCostBillPO>();
		try{
			ResultSet r = assembleSQL("CashCostBill", fromDate, toDate,"CCBCondition",state,
					"CCBAccountID", customerId, "CCBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getCashCostBill(r.getString("CCBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<PaymentBillPO> searchPaymentBills(String fromDate, String toDate, String customerId,
			String operatorId, int state) throws RemoteException {
		ArrayList<PaymentBillPO> bills=new ArrayList<PaymentBillPO>();
		try{
			ResultSet r = assembleSQL("PaymentBill", fromDate, toDate,"PBCondition",state, 
					"PBCustomerID", customerId, "PBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getPaymentBill(r.getString("PBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<ReceiptBillPO> searchReceiptBills(String fromDate, String toDate, String customerId,
			String operatorId, int state) throws RemoteException {
		ArrayList<ReceiptBillPO> bills=new ArrayList<ReceiptBillPO>();
		try{
			ResultSet r = assembleSQL("ReceiptBill", fromDate, toDate,"RBCondition",state, "RBCustomerID", customerId, "RBOperatorID", operatorId);
			while(r.next()) bills.add(BillDataHelper.getReceiptBill(r.getString("RBID")));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
		@Override
	public ArrayList<ChangeBillPO> searchChangeBills(String fromDate, String toDate, String store, String operatorId,
		boolean isOver,int state) throws RemoteException {
		ArrayList<ChangeBillPO> bills = new ArrayList<ChangeBillPO>();
		String[] attributes = isOver ?
				new String[]{"InventoryOverflowBill","IOBCondition","IOBID","IOBOperatorID"}:
				new String[]{"InventoryLostBill","ILBCondition","ILBID","ILBOperatorID"};
		try {
			ResultSet r = assembleSQL(attributes[0], fromDate, toDate,attributes[1],state, "ComStore", store, attributes[3], operatorId);
			while(r.next()) bills.add(BillDataHelper.getChangeBill(r.getString(attributes[2]),isOver));
			return bills;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return bills;
		}
	}

	private ResultSet assembleSQL(String tableName, String fromDate, String toDate, 
			String conditionName, int state, String... values){
		try {
			Statement s = DataHelper.getInstance().createStatement();
			String sql = "SELECT * FROM "+tableName+" WHERE generateTime>'"+fromDate+"' AND generateTime<DATEADD(DAY,1,"+"'"+toDate+"')";
			if (state != -1) sql += (" AND "+conditionName+"="+state);
			for (int i = 0; i < values.length; i+=2) {
				if (values[i+1] == null) continue;
				sql += (" AND " + values[i] + "=" + values[i+1]);
			}
			System.out.println(sql);
			ResultSet r = s.executeQuery(sql+";");
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<BillPO> getBillList(UserPO user) throws RemoteException {

		ArrayList<BillPO> bills=new ArrayList<BillPO>();

		Calendar now = Calendar.getInstance(); 
		String today=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
		String start="2000-01-01";
		String userId=user.getUserId();
	
		if(user.getUsertype()==0){
			try{
				ArrayList<ChangeBillPO> lostBills=searchChangeBills(start, today, null, userId,false, -1);
				ArrayList<ChangeBillPO> overflowBills=searchChangeBills(start, today, null, userId, true, -1);
				bills.addAll(lostBills);
				bills.addAll(overflowBills);	
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		//销售人员
		else if(user.getUsertype()==1){
			ArrayList<SalesBillPO> salesBills=searchSalesBills(start, today, null, userId, -1);
			ArrayList<SalesReturnBillPO> salesReturnBills=searchSalesReturnBills(start, today, null, userId, -1);
			ArrayList<PurchaseBillPO> purchaseBills=searchPurchaseBills(start, today, null, userId, -1);
			ArrayList<PurchaseReturnBillPO> purchaseReturnBills=searchPurchaseReturnBills(start, today, null, userId, -1);
			bills.addAll(purchaseReturnBills);
			bills.addAll(purchaseBills);
			bills.addAll(salesReturnBills);
			bills.addAll(salesBills);
		}
		//财务人员
		else if(user.getUsertype()==UserPO.UserType.ACCOUNTANT){
			ArrayList<CashCostBillPO> cashCostBills=searchCashCostBills(start, today, null, userId, -1);
			ArrayList<PaymentBillPO> paymentBills=searchPaymentBills(start, today, null, userId, -1);
			ArrayList<ReceiptBillPO> receiptBills=searchReceiptBills(start, today, null, userId, -1);
			bills.addAll(receiptBills);
			bills.addAll(cashCostBills);
			bills.addAll(paymentBills);
		}
		return bills;
	}

	@Override
	public ArrayList<GiftBillPO> searchGiftBills(String fromDate, String toDate, String customerId, int state)
			throws RemoteException {
		ArrayList<GiftBillPO> bills=new ArrayList<GiftBillPO>();
		try{
			ResultSet r = assembleSQL("InventoryGiftBill", fromDate, toDate,"IGBCondition", state, "IGBCustomerID", customerId);
			while(r.next()) bills.add(BillDataHelper.getGiftBill((r.getString("IGBID"))));
			return bills;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
