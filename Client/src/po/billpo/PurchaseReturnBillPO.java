package po.billpo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create time: 2017/12/01<br>
 * Last update time: 2017/12/01<br>
 * <br>销售单相关的PO类
 * @author 万嘉雯
 */

public class PurchaseReturnBillPO extends BillPO implements Serializable{

private static final long serialVersionUID = 2578319197720284508L;
	
	private String  supplierId,remark;
	private double sum;
	private ArrayList<SalesItemsPO> purchaseReturnBillItems;
	
	public PurchaseReturnBillPO(){};
	
	public PurchaseReturnBillPO(String date, String time, String id, String operatorId, int state, String supplierId,
			String remark, double sum, ArrayList<SalesItemsPO> purchaseReturnBillItems) {
		super(date, time, id, operatorId, state);
		this.supplierId = supplierId;
		this.remark = remark;
		this.sum = sum;
		this.purchaseReturnBillItems = purchaseReturnBillItems;
	}
	
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}

	public ArrayList<SalesItemsPO> getPurchaseReturnBillItems() {
		return purchaseReturnBillItems;
	}

	public void setPurchaseReturnBillItems(ArrayList<SalesItemsPO> purchaseReturnBillItems) {
		this.purchaseReturnBillItems = purchaseReturnBillItems;
	}

	@Override
	public String getAllId() {
		return "JHTHD-" + this.getDate() + "-" + this.getId();
	}
}
