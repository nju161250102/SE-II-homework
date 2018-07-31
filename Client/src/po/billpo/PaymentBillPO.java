package po.billpo;

import java.io.Serializable;
import java.util.ArrayList;

public class PaymentBillPO extends BillPO implements Serializable{
	private String customerId;
	private ArrayList<TransferItem> transferList;
	private double sum;
	
	public PaymentBillPO(){};
	public PaymentBillPO(String date, String time, String id, String operatorId, int state, String customerId,
			ArrayList<TransferItem> transferList, double sum) {
		super(date, time, id, operatorId, state);
		this.customerId = customerId;
		this.transferList = transferList;
		this.sum = sum;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public ArrayList<TransferItem> getTransferList() {
		return transferList;
	}

	public double getSum() {
		return sum;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public void setTransferList(ArrayList<TransferItem> transferList) {
		this.transferList = transferList;
	}
	
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	@Override
	public String getAllId() {
		return "FKD-" + this.getDate() + "-" + this.getId();
	}
}
