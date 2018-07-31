package vo.billvo;

import vo.MyTableModel;

public class CashCostBillVO extends BillVO {

	private String accountId;
	private MyTableModel tableModel;
	
	public CashCostBillVO(String date, String time, String id, String operator, int state, String accountId, MyTableModel tableModel) {
		super(date, time, id, operator, state);
		this.accountId  = accountId;
		this.tableModel = tableModel;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public MyTableModel getTableModel() {
		return tableModel;
	}
	
	@Override
	public String getAllId() {
		return "XJFYD-" + this.getDate() + "-" + this.getId();
	}

}
