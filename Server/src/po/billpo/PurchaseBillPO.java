package po.billpo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create time: 2017/12/01<br>
 * Last update time: 2017/12/01<br>
 * <br>销售单相关的PO类
 * @author 万嘉雯
 */

public class PurchaseBillPO extends BillPO implements Serializable{
	
	private static final long serialVersionUID = -3790261082899739952L;
	
	private String  supplierId,remark;
	private double sum;
	private ArrayList<SalesItemsPO> purchaseBillItems;
	
	public PurchaseBillPO(){};
	
	public PurchaseBillPO(String date, String time, String id, String operatorId, int state, String supplierId,
			String remark, double sum, ArrayList<SalesItemsPO> purchaseBillItems) {
		super(date, time, id, operatorId, state);
		this.supplierId = supplierId;
		this.remark = remark;
		this.sum = sum;
		this.purchaseBillItems = purchaseBillItems;
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

	public ArrayList<SalesItemsPO> getPurchaseBillItems() {
		return purchaseBillItems;
	}

	public void setPurchaseBillItems(ArrayList<SalesItemsPO> purchaseBillItems) {
		this.purchaseBillItems = purchaseBillItems;
	}

	@Override
	public String getAllId() {
		return "JHD-" + this.getDate() + "-" + this.getId();
	}
}
