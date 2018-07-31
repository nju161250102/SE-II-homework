package blservice.billblservice;

import vo.billvo.ReceiptBillVO;

public interface ReceiptBillBLService extends BillCreateBLService {

	public boolean saveBill(ReceiptBillVO bill);
}
