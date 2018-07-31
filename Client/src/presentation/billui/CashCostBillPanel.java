package presentation.billui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import blservice.billblservice.CashCostBillBLService;
import blservice.infoservice.GetUserInterface;
import businesslogic.CashCostBillBL;
import businesslogic.UserBL;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.AccountChooseWin;
import presentation.tools.Timetools;
import vo.AccountVO;
import vo.MyTableModel;
import vo.UserVO;
import vo.billvo.BillVO;
import vo.billvo.CashCostBillVO;

public class CashCostBillPanel extends JPanel implements BillPanelInterface {

	private CashCostBillBLService cashCostBillBL = new CashCostBillBL();
	private GetUserInterface userInfo = new UserBL();
	private UserVO user;
	private BillVO bill = null;
	
	private JTextField billIdField, operatorField, accountIdField, sumField;
	private JButton accountChooseButton, itemChooseButton, itemDeleteButton;
	private JTable itemListTable;
	
	public CashCostBillPanel(UserVO user) {
		this.user = user;
		initBillPanel();
        this.billIdField.setText(cashCostBillBL.getNewId());
        this.operatorField.setText(user.getName());
	}

	public CashCostBillPanel(UserVO user, CashCostBillVO cashCostBill) {
		this.user = user;
		this.bill = cashCostBill;
		initBillPanel();
        billIdField.setText(cashCostBill.getAllId());
        operatorField.setText(userInfo.getUser(cashCostBill.getOperator()).getName());
        accountIdField.setText(cashCostBill.getAccountId());
        itemListTable.setModel(cashCostBill.getTableModel());
        sumUp();
	}
	
	private void initBillPanel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格，jdk6 update10版本以后的才会出现
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JScrollPane itemListPane;
		JPanel headPanel, accountInfoPanel, centerPanel, itemButtonPanel;
		
		double firstPanelSize[][]={
				{20,55,5,TableLayout.PREFERRED,20,55,5,TableLayout.PREFERRED,TableLayout.FILL},
				{12,25,TableLayout.FILL}
		};
		billIdField = new JTextField(15);
		operatorField = new JTextField(10);
		billIdField.setEditable(false);
		operatorField.setEditable(false);
		headPanel = new JPanel(new TableLayout(firstPanelSize));
		headPanel.add(new JLabel("单据编号"),"1,1");
		headPanel.add(billIdField,"3,1");
		headPanel.add(new JLabel("操作人"),"5,1");
		headPanel.add(operatorField,"7,1");
		
		accountIdField = new JTextField(20);
		accountIdField.setText("");
		accountIdField.setEditable(false);
		double secondPanelSize[][]={
				{20,55,5,TableLayout.PREFERRED,5,TableLayout.PREFERRED,TableLayout.FILL},
				{8,25,TableLayout.FILL}
		};
		accountChooseButton=new JButton("选择");
		accountChooseButton.addActionListener(e -> handleChooseAccount());
		accountInfoPanel = new JPanel(new TableLayout(secondPanelSize));
		accountInfoPanel.add(new JLabel("账户"),"1,1");
		accountInfoPanel.add(accountIdField,"3,1");
	    accountInfoPanel.add(accountChooseButton,"5,1");
		
		String[] itemListAttributes={"条目名", "金额", "备注"};
		itemListTable = new JTable(new MyTableModel(null, itemListAttributes));
		itemListTable.getTableHeader().setReorderingAllowed(false);
		itemListPane = new JScrollPane(itemListTable);
		
		double forthPanelSize[][]={
				{TableLayout.PREFERRED,TableLayout.FILL},
				{25,10,25,10,25,10,25,TableLayout.FILL},
		};
		itemChooseButton=new JButton("新建条目", new ImageIcon("resource/AddButton.png"));
        itemChooseButton.addActionListener(e -> addItem());
		itemDeleteButton=new JButton("删除条目", new ImageIcon("resource/DeleteButton.png"));
        itemDeleteButton.addActionListener(e -> deleteItem());
		sumField = new JTextField(10);
		sumField.setEditable(false);
		sumField.setText("0.0");
		itemButtonPanel = new JPanel(new TableLayout(forthPanelSize));
		itemButtonPanel.add(itemChooseButton, "0,0");
		itemButtonPanel.add(itemDeleteButton, "0,2");
		itemButtonPanel.add(new JLabel("总额："), "0,4");
		itemButtonPanel.add(sumField, "0,6");

		centerPanel=new JPanel();
		double centerPanelSize[][]={
				{20,0.8,20,0.2},
				{10,25,5,TableLayout.FILL}
		};
		centerPanel.setLayout(new TableLayout(centerPanelSize));
		centerPanel.add(new JLabel("条目列表"),"1,1");
		centerPanel.add(itemListPane, "1,3");
		centerPanel.add(itemButtonPanel, "3,3");
		
		double mainPanelSize[][]={
				{TableLayout.FILL},
				{0.08, 0.08, 0.6, TableLayout.FILL}
		};
		this.setLayout(new TableLayout(mainPanelSize));
		this.add(headPanel, "0,0");
		this.add(accountInfoPanel, "0,1");
		this.add(centerPanel, "0,2");

	}

	private void addItem(){
        String[] newRow = new InputCashCostItemInfoWin().getRowData();
        if (newRow != null) {
            MyTableModel model = (MyTableModel) itemListTable.getModel();
            model.addRow(newRow);
        	sumUp();
        } 
    }
	
    private void deleteItem(){
	    int row = itemListTable.getSelectedRow();
	    if(row >= 0) {
	    	((MyTableModel)itemListTable.getModel()).removeRow(itemListTable.getSelectedRow());
	    	sumUp();
	    } else new InfoWindow("请选择删除的条目");
    }
	
    private void handleChooseAccount(){
        AccountVO a = new AccountChooseWin().getAccount();
        if(a == null) return;
        accountIdField.setText(a.getNumber());
    }
    
    private void sumUp(){
	    MyTableModel model = (MyTableModel)this.itemListTable.getModel();
        double total = 0;
	    for(int i = 0; i < model.getRowCount(); i++){
	        total += Double.parseDouble((String)model.getValueAt(i, 1));
	    }
	    sumField.setText(Double.toString(total));
    }
	/**
	 * 获得单据VO
	 * @return
	 */
	public CashCostBillVO getBill(int state) {
		if (itemListTable.getRowCount() == 0) new InfoWindow("没有选择条目");
		else if ("".equals(accountIdField.getText())) new InfoWindow("没有选择账户");
		else {
			return new CashCostBillVO(
					bill == null ? Timetools.getDate() : bill.getDate(),
			        bill == null ? Timetools.getTime() : bill.getTime(),
			        bill == null ? cashCostBillBL.getNewId().split("-")[2] : billIdField.getText().split("-")[2],
			        user.getId(), state, accountIdField.getText(),(MyTableModel) itemListTable.getModel());
		}
		return null;
	}
	@Override
	public void setEditable(boolean b) {
		accountChooseButton.setEnabled(b);
		itemChooseButton.setEnabled(b);
		itemDeleteButton.setEnabled(b);
	}
	@Override
	public void newAction() {
		int response = JOptionPane.showConfirmDialog(null, "确认要新建一张现金费用单吗？", "提示", JOptionPane.YES_NO_OPTION);
        if(response == 1)return;
        billIdField.setText(cashCostBillBL.getNewId());
        accountIdField.setText("");
        sumField.setText("0.0");
        MyTableModel model = (MyTableModel) itemListTable.getModel();
        while(model.getRowCount() > 0)  model.removeRow(0);
	}

	@Override
	public void saveAction() {
        CashCostBillVO bill = getBill(BillVO.SAVED);
        if (bill != null && cashCostBillBL.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已保存。");
    
	}

	@Override
	public void commitAction() {
		CashCostBillVO bill = getBill(BillVO.COMMITED);
        if (bill != null && cashCostBillBL.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已提交。");
	}
}
