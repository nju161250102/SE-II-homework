package blservice.billblservice;

import vo.billvo.CashCostBillVO;

public interface CashCostBillBLService extends BillCreateBLService {
	/**
	 * 将单据保存到数据库
	 * @return 是否保存成功
	 */
	boolean saveBill(CashCostBillVO bill);
}