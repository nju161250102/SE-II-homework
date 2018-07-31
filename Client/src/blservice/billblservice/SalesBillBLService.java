package blservice.billblservice;

import vo.MyTableModel;
import vo.PromotionVO;
import vo.billvo.SalesBillVO;

public interface SalesBillBLService extends BillCreateBLService {

	/**
	 * 将单据保存到数据库（和保存单据不van全一致）
	 * @return 是否保存成功
	 */
	public boolean saveBill(SalesBillVO bill);
	
	public MyTableModel search(String type, String key);
	
	public MyTableModel getBillsByDate(String from, String to);
	
	public MyTableModel getFinishedBills();
	
	public MyTableModel getFinishedBills(String customerId);
	
	public PromotionVO getBestPromotion(int type, MyTableModel goods, double sum);
	
}
