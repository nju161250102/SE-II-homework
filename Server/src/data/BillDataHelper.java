package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import po.billpo.BillPO;
import po.billpo.CashCostBillPO;
import po.billpo.CashCostItem;
import po.billpo.ChangeBillPO;
import po.billpo.ChangeItem;
import po.billpo.GiftBillPO;
import po.billpo.GiftItem;
import po.billpo.PaymentBillPO;
import po.billpo.PurchaseBillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.ReceiptBillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
import po.billpo.SalesReturnBillPO;
import po.billpo.TransferItem;

public class BillDataHelper {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public static ChangeBillPO getChangeBill(String id, boolean isOver) {
		ArrayList<ChangeItem> changeItems=new ArrayList<ChangeItem>();
		String billName = isOver ? "InventoryOverflowBill" : "InventoryLostBill";
		String recordName = isOver ? "InventoryOverflowRecord" : "InventoryLostRecord";
		String[] recordColumns = isOver ? new String[]{"IORID","IORComID","IORComQuantity","IOROverQuantity"} : 
			new String[]{"ILRID","ILRComID","ILRComQuantity","ILRLostQuantity"};
		String[] billColumns = isOver ? new String[] {"IOBID","IOBOperatorID","IOBCondition"} :
			new String[]{"ILBID","ILBOperatorID","ILBCondition"};
		try{
			ResultSet r1 = SQLQueryHelper.getRecordByAttribute(recordName, recordColumns[0], id);
			while(r1.next()) {	
				ChangeItem item = new ChangeItem(r1.getString(recordColumns[1])
						,r1.getInt(recordColumns[2]),r1.getInt(recordColumns[3]));
				changeItems.add(item);
			}
			ResultSet r2 = SQLQueryHelper.getRecordByAttribute(billName, billColumns[0], id);
			r2.next();
			return new ChangeBillPO(
					dateFormat.format(r2.getDate("generateTime")),
					timeFormat.format(r2.getTime("generateTime")),
					r2.getString(billColumns[0]).split("-")[2],
					r2.getString(billColumns[1]),
					r2.getInt(billColumns[2]),isOver,changeItems);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static CashCostBillPO getCashCostBill(String id) {
		try {
			ArrayList<CashCostItem> items = new ArrayList<CashCostItem>();
			ResultSet r1 = SQLQueryHelper.getRecordByAttribute("CashCostRecord", "CCRID", id);
			while(r1.next()) {	
				CashCostItem item=new CashCostItem(r1.getString("CCRCostName"),r1.getDouble("CCRMoney"),r1.getString("CCRRemark"));
			    items.add(item);
			}
			ResultSet r2=SQLQueryHelper.getRecordByAttribute("CashCostBill", "CCBID", id);
			r2.next();
			return new CashCostBillPO(
					dateFormat.format(r2.getDate("generateTime")),
					timeFormat.format(r2.getTime("generateTime")),
					r2.getString("CCBID").split("-")[2],
					r2.getString("CCBOperatorID"),
					r2.getInt("CCBCondition"),
					r2.getString("CCBAccountID"),
					items,
					r2.getDouble("CCBSum"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReceiptBillPO getReceiptBill(String id) {
		try {
			ResultSet r2 = SQLQueryHelper.getRecordByAttribute("ReceiptRecord", "RRID", id);
			ArrayList<TransferItem> items=new ArrayList<TransferItem>();
			while(r2.next()){
				TransferItem item=new TransferItem(
						r2.getString("RRAccountID"),
						r2.getDouble("RRTransfer"),
						r2.getString("RRRemark"));
				items.add(item);
			}
			ResultSet r = SQLQueryHelper.getRecordByAttribute("ReceiptBill", "RBID", id);
			r.next();
			ReceiptBillPO bill=new ReceiptBillPO(
					dateFormat.format(r.getDate("generateTime")),
					timeFormat.format(r.getTime("generateTime")),
					r.getString("RBID").split("-")[2],
					r.getString("RBOperatorID"),
					r.getInt("RBCondition"),
					r.getString("RBCustomerID"),
					items,
					r.getDouble("RBSum"));
			return bill;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static PaymentBillPO getPaymentBill(String id) {
		try{
			ArrayList<TransferItem> items = new ArrayList<TransferItem>();
			ResultSet r2 = SQLQueryHelper.getRecordByAttribute("PaymentRecord", "PRID", id);
			while(r2.next()){	
				TransferItem item = new TransferItem(r2.getString("PRAccountID"),r2.getDouble("PRTransfer"),r2.getString("PRRemark"));
			    items.add(item);
			}
			ResultSet r = SQLQueryHelper.getRecordByAttribute("PaymentBill", "PBID", id);
			r.next();
			PaymentBillPO bill = new PaymentBillPO(
					dateFormat.format(r.getDate("generateTime")),
					timeFormat.format(r.getTime("generateTime")),
					r.getString("PBID").split("-")[2],
					r.getString("PBOperatorID"),
					r.getInt("PBCondition"),
					r.getString("PBCustomerID"),
					items,
					r.getDouble("PBSum"));
			return bill;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static PurchaseBillPO getPurchaseBill(String id){
		try{
			ArrayList<SalesItemsPO> items = new ArrayList<SalesItemsPO>();
			ResultSet r1=SQLQueryHelper.getRecordByAttribute("PurchaseRecord", "PRID", id);
			while(r1.next()){	
			    SalesItemsPO item=new SalesItemsPO(
					r1.getString("PRComID"),
					r1.getString("PRRemark"),
					r1.getInt("PRComQuantity"),
					r1.getDouble("PRComPrice"),
					r1.getDouble("PRComSum"));
			    items.add(item);
			}
			ResultSet r2 = SQLQueryHelper.getRecordByAttribute("PurchaseBill", "PBID", id);
			r2.next();
			return new PurchaseBillPO(
				dateFormat.format(r2.getDate("generateTime")),
				timeFormat.format(r2.getTime("generateTime")),
				r2.getString("PBID").split("-")[2],
				r2.getString("PBOperatorID"),
				r2.getInt("PBCondition"),
				r2.getString("PBSupplierID"),
				r2.getString("PBRemark"),
				r2.getDouble("PBSum"),
				items);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static PurchaseReturnBillPO getPurchaseRetrunBill(String id) {
		try{
			ArrayList<SalesItemsPO> items=new ArrayList<SalesItemsPO>();
			ResultSet r1 = SQLQueryHelper.getRecordByAttribute("PurchaseReturnRecord", "PRRID", id);
			while(r1.next()){	
			    SalesItemsPO item=new SalesItemsPO(
					r1.getString("PRRComID"),
					r1.getString("PRRRemark"),
					r1.getInt("PRRComQuantity"),
					r1.getDouble("PRRComPrice"),
					r1.getDouble("PRRComSum"));
			    items.add(item);
			}
			ResultSet r2=SQLQueryHelper.getRecordByAttribute("PurchaseReturnBill", "PRBID", id);
			r2.next();
			return new PurchaseReturnBillPO(
				dateFormat.format(r2.getDate("generateTime")),
				timeFormat.format(r2.getTime("generateTime")),
				r2.getString("PRBID").split("-")[2],
				r2.getString("PRBOperatorID"),
				r2.getInt("PRBCondition"),
				r2.getString("PRBSupplierID"),
				r2.getString("PRBRemark"),
				r2.getDouble("PRBSum"),
				items);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static SalesBillPO getSalesBill(String id) {
		try{
			ArrayList<SalesItemsPO> items=new ArrayList<SalesItemsPO>();
			ResultSet r1=SQLQueryHelper.getRecordByAttribute("SalesRecord", "SRID", id);
			while(r1.next()) {	
			    SalesItemsPO item=new SalesItemsPO(
					r1.getString("SRComID"),
					r1.getString("SRRemark"),
					r1.getInt("SRComQuantity"),
					r1.getDouble("SRPrice"),
					r1.getDouble("SRComSum"));
			    items.add(item);
			}
			ResultSet r2=SQLQueryHelper.getRecordByAttribute("SalesBill", "SBID", id);
			r2.next();
			String promotionId = "null".equals(r2.getString("SBPromotionID")) ? null : r2.getString("SBPromotionID");
			return new SalesBillPO(
				dateFormat.format(r2.getDate("generateTime")),
				timeFormat.format(r2.getTime("generateTime")),
				r2.getString("SBID").split("-")[2],
				r2.getString("SBOperatorID"),
				r2.getInt("SBCondition"),
				r2.getString("SBCustomerID"),
				r2.getString("SBSalesmanName"),
				r2.getString("SBRemark"),
				promotionId,
				r2.getDouble("SBBeforeDiscount"),
				r2.getDouble("SBDiscount"),
				r2.getDouble("SBCoupon"),
				r2.getDouble("SBAfterDiscount"),
				items);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static SalesReturnBillPO getSalesReturnBill(String id) {
		try{
			ArrayList<SalesItemsPO> items=new ArrayList<SalesItemsPO>();
			ResultSet r1=SQLQueryHelper.getRecordByAttribute("SalesReturnRecord", "SRRID", id);
			while(r1.next()){	
			    SalesItemsPO item=new SalesItemsPO(
					r1.getString("SRRComID"),
					r1.getString("SRRRemark"),
					r1.getInt("SRRComQuantity"),
					r1.getDouble("SRRPrice"),
					r1.getDouble("SRRComSum"));
			    items.add(item);
			}
			ResultSet r2=SQLQueryHelper.getRecordByAttribute("SalesReturnBill", "SRBID", id);
			r2.next();
			return new SalesReturnBillPO(
				dateFormat.format(r2.getDate("generateTime")),
				timeFormat.format(r2.getTime("generateTime")),
				r2.getString("SRBID").split("-")[2],
				r2.getString("SRBOperatorID"),
				r2.getInt("SRBCondition"),
				r2.getString("SRBCustomerID"),
				r2.getString("SRBSalesmanName"),
				r2.getString("SRBRemark"),
				r2.getString("SRBOriginalSBID"),
				r2.getDouble("SRBOriginalSum"),
				r2.getDouble("SRBReturnSum"),
				items);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static GiftBillPO getGiftBill(String id){
		ArrayList<GiftItem> items=new ArrayList<GiftItem>();
		try{
			ResultSet r1=SQLQueryHelper.getRecordByAttribute("InventoryGiftRecord", "IGRID", id);
			while(r1.next()){
				GiftItem item=new GiftItem(
						r1.getString("IGRComID"),
						r1.getInt("IGRComQuantity"),
						r1.getDouble("IGRComPrice"));
				items.add(item);
			}
			ResultSet r2=SQLQueryHelper.getRecordByAttribute("InventoryGiftBill", "IGBID", id);
			r2.next();
			return new GiftBillPO(
					dateFormat.format(r2.getDate("generateTime")),
					timeFormat.format(r2.getTime("generateTime")),
					r2.getString("IGBID").split("-")[2],
					r2.getString("IGBOperatorID"),
					r2.getInt("IGBCondition"),
					items,
					r2.getString("IGBSalesBillID"),
					r2.getString("IGBCustomerID"));	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//获取单据类id,不加标识
	public static String getNewBillId(String tableName, String idName){
		int num = 0;
		Calendar now = Calendar.getInstance();
		int year=now.get(Calendar.YEAR), month=now.get(Calendar.MONTH),  day=now.get(Calendar.DAY_OF_MONTH);
		String date = year+"-"+(month+1)+"-"+day;
		
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT "+idName+" FROM "+tableName+
				" WHERE generateTime>'"+date+"' AND generateTime<DATEADD(DAY,1,'"+date+"');");
			while(r.next()) num++;
			return String.format("%05d", num+1);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean deleteBill(String id) {
		String[][] data = {{"XJFYD","FKD","SKD","JHD","JHTHD","XSD","XSTHD","BYD","BSD","SPZSD"},
				{"CashCostBill","PaymentBill","ReceiptBill","PurchaseBill","PurchaseReturnBill","SalesBill","SalesReturnBill","InventoryOverflowBill","InventoryLostBill","InventoryGiftBill"},
				{"CCBID","PBID","RBID","PBID","PRBID","SBID","SRBID","IOBID","ILBID","IGBID"}};
		String type = id.split("-")[0];
		int num = 0;
		for (int i = 0; i < data[0].length; i++) if (type.equals(data[0][i])) num = i;
		String billTableName = data[1][num];
		String billTableId = data[2][num];
		try {
			Statement s1 = DataHelper.getInstance().createStatement();
			int r1 = s1.executeUpdate("DELETE FROM "+billTableName+" WHERE "+billTableId+"='"+id+"';");
			return r1 > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isBillExist(String billName,String idName,BillPO bill){
		int num=0;
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT COUNT(*) AS num FROM "+billName+" WHERE "+idName+"='"+bill.getAllId()+"';");
			r.next(); 
			num=r.getInt("num");
			if(num > 0) return true;
			else return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
