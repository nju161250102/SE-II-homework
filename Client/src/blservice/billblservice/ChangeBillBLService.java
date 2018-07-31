package blservice.billblservice;

import vo.billvo.ChangeBillVO;

public interface ChangeBillBLService extends BillCreateBLService {

	/**
	 * 将单据保存到数据库（和保存单据不van全一致）
	 * @return 是否保存成功
	 */
	public boolean saveBill(ChangeBillVO bill);
}
