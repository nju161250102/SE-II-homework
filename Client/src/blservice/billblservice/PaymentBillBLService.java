package blservice.billblservice;

import vo.billvo.PaymentBillVO;

public interface PaymentBillBLService extends BillCreateBLService {
	
	public boolean saveBill(PaymentBillVO bill);
}
