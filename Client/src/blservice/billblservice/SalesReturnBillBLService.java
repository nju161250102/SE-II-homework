package blservice.billblservice;

import vo.billvo.SalesReturnBillVO;

public interface SalesReturnBillBLService extends BillCreateBLService {
    
    boolean saveBill(SalesReturnBillVO bill);
}
