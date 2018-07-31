package blservice.billblservice;

import vo.MyTableModel;
import vo.UserVO;

public interface BillSearchBLService {
	public MyTableModel getBillTable(UserVO user);
	
	MyTableModel filterInventoryBills(String from, String to, String store, String operatorId, boolean isOver, int state);
    
    MyTableModel filterPurchaseBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterPurchaseReturnBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterSalesBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterSalesReturnBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterCashCostBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterPaymentBills(String from, String to, String customerId, String operatorId, int state);

    MyTableModel filterReceiptBills(String from, String to, String customerId, String operatorId, int state);
    
    MyTableModel filterGiftBills(String from, String to, String customerId);
    
    MyTableModel filterBills(String from, String to);

}
