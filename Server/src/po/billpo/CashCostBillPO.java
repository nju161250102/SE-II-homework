package po.billpo;

import java.io.Serializable;
import java.util.ArrayList;

public class CashCostBillPO extends BillPO implements Serializable{

	private String accountId;
	private ArrayList<CashCostItem> cashcostList;
	private double sum;
	
	public CashCostBillPO(){};
	
	public CashCostBillPO(String date, String time, String id, String operatorId, int state, String accountId,
			ArrayList<CashCostItem> cashcostList, double sum) {
		super(date, time, id, operatorId, state);
		this.accountId = accountId;
		this.cashcostList = cashcostList;
		this.sum = sum;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setCashcostList(ArrayList<CashCostItem> cashcostList) {
		this.cashcostList = cashcostList;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public ArrayList<CashCostItem> getCashcostList() {
		return cashcostList;
	}

	public double getSum() {
		return sum;
	}

	@Override
	public String getAllId() {
		return "XJFYD-" + this.getDate() + "-" + this.getId();
	}
}
