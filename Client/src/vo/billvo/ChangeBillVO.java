package vo.billvo;

import vo.MyTableModel;

public class ChangeBillVO extends BillVO {

	/*判断报溢或者报损*/
	private boolean isOver;
	private MyTableModel tableModel;

	public ChangeBillVO(String date, String time, String id, String operator, int state, boolean isOver, MyTableModel tableModel) {
		super(date, time, id, operator, state);
		this.isOver = isOver;
		this.tableModel = tableModel;
	}
	
	public boolean getFlag() {
		return isOver;
	}
	
	public MyTableModel getTableModel() {
		return tableModel;
	}
	
	@Override
	public String getAllId() {
		String s = isOver ? "BYD-" : "BSD-";
		return s + this.getDate() + "-" + this.getId();
	}

}
