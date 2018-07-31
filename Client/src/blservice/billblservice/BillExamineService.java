package blservice.billblservice;

import vo.billvo.BillVO;

public interface BillExamineService {

	/**
	 * ЩѓХњЕЅОн
	 * @param billId
	 * @return
	 */
	public boolean examineBill(String billId);
	
	public boolean notPassBill(String billId);
}
