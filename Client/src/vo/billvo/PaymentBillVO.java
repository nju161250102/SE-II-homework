package vo.billvo;

import vo.MyTableModel;

public class PaymentBillVO extends BillVO {

	private String customerId;
	private MyTableModel tableModel;
	
	public PaymentBillVO(String date, String time, String id, String operator, int state, String customerId, MyTableModel tableModel) {
		super(date, time, id, operator, state);
		this.customerId = customerId;
		this.tableModel = tableModel;
	}

	public String getCustomerId() {
		return customerId;
	}
	
	public MyTableModel getTableModel() {
		return tableModel;
	}
	
	@Override
	public String getAllId() {
		return "FKD-" + this.getDate() + "-" + this.getId();
	}

}
