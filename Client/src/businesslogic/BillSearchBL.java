package businesslogic;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import blservice.billblservice.BillSearchBLService;
import blservice.infoservice.GetCustomerInterface;
import blservice.infoservice.GetUserInterface;
import dataservice.BillSearchDataService;
import ds_stub.BillSearchDs_stub;
import po.billpo.BillPO;
import po.billpo.CashCostBillPO;
import po.billpo.ChangeBillPO;
import po.billpo.GiftBillPO;
import po.billpo.PaymentBillPO;
import po.billpo.PurchaseBillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.ReceiptBillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesReturnBillPO;
import rmi.Rmi;
import vo.MyTableModel;
import vo.UserVO;

/**
 * @author 恽叶霄
 */
public class BillSearchBL implements BillSearchBLService {
    
    private BillSearchDataService billSearchDs;
    private GetUserInterface userInfo;
    private GetCustomerInterface customerInfo;

    public BillSearchBL() {
        billSearchDs = Rmi.flag ? Rmi.getRemote(BillSearchDataService.class) : new BillSearchDs_stub();
        userInfo = new UserBL();
        customerInfo = new CustomerBL();
    }
    
    @Override
	public MyTableModel getBillTable(UserVO user) {
		try {
			ArrayList<BillPO> list = billSearchDs.getBillList(user.toPO());
			String[] attributes = new String[]{"制定时间","单据编号","单据类型","状态"};
			String[][] info = new String[list.size()][attributes.length];
			
			Collections.sort(list, new Comparator<BillPO>() {  
	            @Override  
	            public int compare(BillPO o1, BillPO o2) {
	            	int[] states = {BillPO.SAVED, BillPO.NOTPASS, BillPO.COMMITED, BillPO.PASS};
	            	if (Arrays.binarySearch(states, o1.getState()) == Arrays.binarySearch(states, o2.getState())) {
	            		DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            			try {
							Date time1 = df.parse(o1.getDate() + " " + o1.getTime());
							Date time2 = df.parse(o2.getDate() + " " + o2.getTime());
							return time1.after(time2) ? -1 : 1;
						} catch (ParseException e) {
							e.printStackTrace();
							return -1;
						}
	            	} else return Arrays.binarySearch(states, o1.getState()) > Arrays.binarySearch(states, o2.getState()) ? 1 : -1 ;
	            }  
	        });
			
			for (int i = 0; i < list.size(); i++) {
				BillPO bill = list.get(i);
				info[i][0] = bill.getDate() + "  " + bill.getTime();
				info[i][1] = bill.getAllId();
				info[i][2] = BillTools.getBillName(bill);
				info[i][3] = bill.getStateName();
			}
			
			return new MyTableModel(info, attributes);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
    @Override
    public MyTableModel filterInventoryBills(String from, String to, String store, String operatorId, boolean isOver, int state) {
        try{
            ArrayList<ChangeBillPO> bills = billSearchDs.searchChangeBills(from, to, store, operatorId, isOver, state); 
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名"};
            String[][] data = new String[bills.size()][];
            String pref = isOver ? "BYD-" : "BSD-";
            for(int i = 0; i < bills.size(); i++){
                ChangeBillPO bill = bills.get(i);
                String id = pref + bill.getDate() + "-" + bill.getId();
                String time = bill.getDate() + ' ' + bill.getTime();
                String opeId = bill.getOperator();
                String opeName = userInfo.getUser(opeId).getName();
                data[i] = new String[]{id, time, opeId, opeName};
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterPurchaseBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<PurchaseBillPO> bills = billSearchDs.searchPurchaseBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "进货商编号", "进货商姓名", "交易额", "备注"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                PurchaseBillPO bill = bills.get(i);
                data[i][0] = "JHD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getSupplierId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
                data[i][6] = bill.getSum() + "";
                data[i][7] = bill.getRemark();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterPurchaseReturnBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<PurchaseReturnBillPO> bills = billSearchDs.searchPurchaseReturnBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "进货商编号", "进货商姓名", "交易额", "备注"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                PurchaseReturnBillPO bill = bills.get(i);
                data[i][0] = "JHTHD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getSupplierId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
                data[i][6] = bill.getSum() + "";
                data[i][7] = bill.getRemark();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterSalesBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<SalesBillPO> bills = billSearchDs.searchSalesBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "销售商编号", "销售商姓名", "交易额", "备注"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                SalesBillPO bill = bills.get(i);
                data[i][0] = "XSD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getCustomerId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
                data[i][6] = bill.getAfterDiscount() + "";
                data[i][7] = bill.getRemark();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterSalesReturnBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<SalesReturnBillPO> bills = billSearchDs.searchSalesReturnBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "销售商编号", "销售商姓名", "交易额", "备注"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                SalesReturnBillPO bill = bills.get(i);
                data[i][0] = "XSTHD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = new UserBL().getUser(data[i][2]).getName();
                data[i][4] = bill.getCustomerId();
                data[i][5] = new CustomerBL().getCustomer(data[i][4]).getName();
                data[i][6] = bill.getReturnSum() + "";
                data[i][7] = bill.getRemark();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterCashCostBills(String from, String to, String accountId, String operatorId, int state) {
        try{
            ArrayList<CashCostBillPO> bills = billSearchDs.searchCashCostBills(from, to, accountId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "银行账户", "总额"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                CashCostBillPO bill = bills.get(i);
                data[i][0] = "XJFYD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getAccountId();
                data[i][5] = ""+bill.getSum();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterPaymentBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<PaymentBillPO> bills = billSearchDs.searchPaymentBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "客户编号", "客户姓名", "总额"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                PaymentBillPO bill = bills.get(i);
                data[i][0] = "FKD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getCustomerId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
                data[i][6] = ""+bill.getSum();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterReceiptBills(String from, String to, String customerId, String operatorId, int state) {
        try{
            ArrayList<ReceiptBillPO> bills = billSearchDs.searchReceiptBills(from, to, customerId, operatorId, state);
            String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "客户编号", "客户姓名", "总额"};
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
                ReceiptBillPO bill = bills.get(i);
                data[i][0] = "SKD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getCustomerId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
                data[i][6] = ""+bill.getSum();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel filterGiftBills(String from, String to, String customerId) {
    	String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名", "客户编号", "客户姓名"};
        try {
			ArrayList<GiftBillPO> bills = billSearchDs.searchGiftBills(from, to, customerId, 2);
            String[][] data = new String[bills.size()][columnNames.length];
            for(int i = 0; i < data.length; i++){
            	GiftBillPO bill = bills.get(i);
                data[i][0] = "SKD-" + bill.getDate() + "-" + bill.getId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
                data[i][4] = bill.getCustomerId();
                data[i][5] = customerInfo.getCustomer(data[i][4]).getName();
            }
            return new MyTableModel(data, columnNames);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        return new MyTableModel(null, columnNames);
    }

    
    @Override
    public MyTableModel filterBills(String from, String to) {
    	String[] columnNames = {"单据编号", "制定时间", "操作员编号", "操作员姓名"};
    	try {
    		ArrayList<ChangeBillPO> overflowBills = billSearchDs.searchChangeBills(from, to, null, null, true, 2); 
            ArrayList<ChangeBillPO> brokenBills = billSearchDs.searchChangeBills(from, to, null, null, false, 2); 
            ArrayList<PurchaseBillPO> purchaseBills = billSearchDs.searchPurchaseBills(from, to, null, null, 2);
            ArrayList<PurchaseReturnBillPO> purchaseReturnBills = billSearchDs.searchPurchaseReturnBills(from, to, null, null, 2);
            ArrayList<SalesBillPO> salesBills = billSearchDs.searchSalesBills(from, to, null, null, 2);
            ArrayList<SalesReturnBillPO> salesReturnBills = billSearchDs.searchSalesReturnBills(from, to, null, null, 2);
            ArrayList<CashCostBillPO> cashCostBills = billSearchDs.searchCashCostBills(from, to, null, null, 2);
            ArrayList<PaymentBillPO> paymentBills = billSearchDs.searchPaymentBills(from, to, null, null, 2);
            ArrayList<ReceiptBillPO> receiptBills = billSearchDs.searchReceiptBills(from, to, null, null, 2);
            ArrayList<GiftBillPO> giftBills = billSearchDs.searchGiftBills(from, to, null, 2);

            ArrayList<BillPO> bills = new ArrayList<BillPO>();
            bills.addAll(overflowBills);
            bills.addAll(brokenBills);
            bills.addAll(purchaseBills);
            bills.addAll(purchaseReturnBills);
            bills.addAll(salesBills);
            bills.addAll(salesReturnBills);
            bills.addAll(cashCostBills);
            bills.addAll(paymentBills);
            bills.addAll(receiptBills);
            bills.addAll(giftBills);
            
        	String[][] data = new String[bills.size()][columnNames.length];
        	for(int i = 0; i < bills.size(); i++) {
        		BillPO bill = bills.get(i);
        		data[i][0] = bill.getAllId();
                data[i][1] = bill.getDate() + ' ' + bill.getTime();
                data[i][2] = bill.getOperator();
                data[i][3] = userInfo.getUser(data[i][2]).getName();
        	}
            return new MyTableModel(data, columnNames);
    	}catch (RemoteException e) {
    		e.printStackTrace();
    	}
        return new MyTableModel(null, columnNames);
    }

}
