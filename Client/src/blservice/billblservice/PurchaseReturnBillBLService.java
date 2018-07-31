package blservice.billblservice;

import vo.billvo.PurchaseReturnBillVO;

public interface PurchaseReturnBillBLService extends BillCreateBLService {
    
    boolean saveBill(PurchaseReturnBillVO bill);
}
