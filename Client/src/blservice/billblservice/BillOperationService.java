package blservice.billblservice;

import vo.billvo.BillVO;

/**
 * 单据操作使用的接口
 * @author 钱美缘
 *
 */
public interface BillOperationService {

	/**
	 * 红冲单据
	 * @param billId 单据编号
	 * @return
	 */
	public boolean offsetBill(String billId);
	/**
	 * 复制一张单据
	 * @param billId 单据编号
	 * @return
	 */
	public boolean copyBill(BillVO bill);
	/**
	 * 删除一张单据（已提交状态的单据不能删除）
	 * @param billId 单据编号
	 * @return 是否删除成功
	 */
	public boolean deleteBill(String billId);
	
	public BillVO getBillById(String billId);
}
