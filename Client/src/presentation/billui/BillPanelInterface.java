package presentation.billui;

public interface BillPanelInterface {
	/***/
	public void newAction();
	/***/
	public void saveAction();
	/**提交时执行的代码*/
	public void commitAction();
	/**设置单据内容能否编辑*/
	void setEditable(boolean b);
}
