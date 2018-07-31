package blservice.billblservice;

import vo.MyTableModel;
import vo.billvo.PurchaseBillVO;

public interface PurchaseBillBLService extends BillCreateBLService {
    
    boolean saveBill(PurchaseBillVO bill);
    
    MyTableModel getFinishedBills(String customerId);
    
    MyTableModel search(String type, String key);
    
    MyTableModel getBillByDate(String from, String to);

}
