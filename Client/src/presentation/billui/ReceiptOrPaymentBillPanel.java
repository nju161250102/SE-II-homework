package presentation.billui;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import blservice.billblservice.PaymentBillBLService;
import blservice.billblservice.ReceiptBillBLService;
import blservice.infoservice.GetCustomerInterface;
import blservice.infoservice.GetUserInterface;
import businesslogic.CustomerBL;
import businesslogic.PaymentBillBL;
import businesslogic.ReceiptBillBL;
import businesslogic.UserBL;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CustomerChooseWin;
import presentation.tools.Timetools;
import vo.CustomerVO;
import vo.MyTableModel;
import vo.UserVO;
import vo.billvo.BillVO;
import vo.billvo.PaymentBillVO;
import vo.billvo.ReceiptBillVO;

public class ReceiptOrPaymentBillPanel extends JPanel implements BillPanelInterface {

	private ReceiptBillBLService receiptBillBL = new ReceiptBillBL();
	private PaymentBillBLService paymentBillBL = new PaymentBillBL();

	private BillVO bill = null;
	private UserVO user;
	private JTextField billIdField, customerIdField, customerNameField, operaterField, sumField;
	private JButton customerChooseButton, transferChooseButton, transferDeleteButton;
	private JTable transferListTable;
	private JRadioButton receiptButton, paymentButton;
	
	private GetUserInterface userInfo = new UserBL();
	private GetCustomerInterface customerInfo = new CustomerBL();
	
	public ReceiptOrPaymentBillPanel(UserVO user) {
		this.user = user;
		initBillPanel();
        this.billIdField.setText(receiptBillBL.getNewId());
		this.operaterField.setText(user.getName());
	}

	public ReceiptOrPaymentBillPanel(UserVO user, ReceiptBillVO bill) {
		this.user = user;
		this.bill = bill;
		initBillPanel();
		receiptButton.setSelected(true);
		billIdField.setText(bill.getAllId());
		operaterField.setText(userInfo.getUser(bill.getOperator()).getName());
        customerIdField.setText(bill.getCustomerId());
        customerNameField.setText(customerInfo.getCustomer(bill.getCustomerId()).getName());
        transferListTable.setModel(bill.getTableModel());
        sumUp();
	}
	
	public ReceiptOrPaymentBillPanel(UserVO user, PaymentBillVO bill) {
		this.user = user;
		this.bill = bill;
		initBillPanel();
		paymentButton.setSelected(true);
		billIdField.setText(bill.getAllId());
		operaterField.setText(userInfo.getUser(bill.getOperator()).getName());
        operaterField.setText(bill.getOperator());
        customerIdField.setText(bill.getCustomerId());
        customerNameField.setText(customerInfo.getCustomer(bill.getCustomerId()).getName());
        transferListTable.setModel(bill.getTableModel());
        sumUp();
	}
	
	private void initBillPanel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JScrollPane transferListPane;
		JPanel headPanel, customerInfoPanel,centerPanel,transferButtonPanel;
		
		headPanel=new JPanel();
		double firstPanelSize[][]={
				{20,55,5,TableLayout.PREFERRED,20,55,5,TableLayout.PREFERRED,TableLayout.FILL},
				{12,25,TableLayout.FILL}
		};
		billIdField = new JTextField(15);
		operaterField = new JTextField(10);
		billIdField.setEditable(false);
		operaterField.setEditable(false);
		headPanel.setLayout(new TableLayout(firstPanelSize));
		headPanel.add(new JLabel("单据编号"),"1,1");
		headPanel.add(billIdField,"3,1");
		headPanel.add(new JLabel("操作人"),"5,1");
		headPanel.add(operaterField,"7,1");
		
		JPanel choosePanel = new JPanel();
		double choosePanelSize[][] = {
				{20,55,5,TableLayout.PREFERRED,20,TableLayout.PREFERRED,TableLayout.FILL},
				{12,25,TableLayout.FILL}
		};
		choosePanel.setLayout(new TableLayout(choosePanelSize));
		receiptButton = new JRadioButton("收款单");
		paymentButton = new JRadioButton("付款单");
		receiptButton.setSelected(true);
		receiptButton.addActionListener(e -> setBillId(true));
		paymentButton.addActionListener(e -> setBillId(false));
		choosePanel.add(new JLabel("单据类型"), "1,1");
		choosePanel.add(receiptButton, "3,1");
		choosePanel.add(paymentButton, "5,1");
		ButtonGroup chooseGroup = new ButtonGroup();
		chooseGroup.add(receiptButton);
		chooseGroup.add(paymentButton);
		
		customerInfoPanel=new JPanel();
		double secondPanelSize[][]={
				{20,45,5,70,12,70,12,70,60,TableLayout.FILL},
				{8,25,TableLayout.FILL}
		};
		customerIdField = new JTextField(10);
		customerNameField = new JTextField(10);
		customerIdField.setText("");
		customerNameField.setText("");
		customerIdField.setEditable(false);
		customerNameField.setEditable(false);
		customerChooseButton=new JButton("选择");	
        customerChooseButton.addActionListener(e -> handleChooseCustomer());
		customerInfoPanel.setLayout(new TableLayout(secondPanelSize));
		customerInfoPanel.add(new JLabel("客户"),"1,1");
		customerInfoPanel.add(customerIdField,"3,1");
		customerInfoPanel.add(new JLabel("--"),"4,1");
		customerInfoPanel.add(customerNameField,"5,1");
		customerInfoPanel.add(customerChooseButton,"7,1");
		
		String[] transferListAttributes={"银行账户", "转账金额", "备注"};
		transferListTable = new JTable(new MyTableModel(null,transferListAttributes));
		transferListTable.getTableHeader().setReorderingAllowed(false);
		transferListPane = new JScrollPane(transferListTable);

		transferButtonPanel=new JPanel();
		double forthPanelSize[][]={
				{TableLayout.PREFERRED,TableLayout.FILL},
				{25,10,25,10,25,10,25,TableLayout.FILL},
		};
		sumField = new JTextField(8);
		sumField.setText("0.0");
		sumField.setEditable(false);
		transferChooseButton=new JButton("新增转账", new ImageIcon("resource/AddButton.png"));
        transferChooseButton.addActionListener(e -> addItem());
        transferDeleteButton=new JButton("删除转账", new ImageIcon("resource/DeleteButton.png"));
        transferDeleteButton.addActionListener(e -> deleteItem());
		transferButtonPanel.setLayout(new TableLayout(forthPanelSize));
		transferButtonPanel.add(transferChooseButton, "0,0");
		transferButtonPanel.add(transferDeleteButton, "0,2");
		transferButtonPanel.add(new JLabel("总额"),"0,4");
		transferButtonPanel.add(sumField,"0,6");

		centerPanel=new JPanel();
		double centerPanelSize[][]={
				{20,0.8,20,0.2},
				{10,25,5,TableLayout.FILL}
		};
		centerPanel.setLayout(new TableLayout(centerPanelSize));
		centerPanel.add(new JLabel("转账列表"),"1,1");
		centerPanel.add(transferListPane, "1,3");
		centerPanel.add(transferButtonPanel, "3,3");
		
		double mainPanelSize[][]={
				{TableLayout.FILL},
				{0.08,0.08,0.08,0.6}	
		};
		this.setLayout(new TableLayout(mainPanelSize));
		this.add(headPanel, "0,0");
		this.add(choosePanel, "0,1");
		this.add(customerInfoPanel, "0,2");
		this.add(centerPanel,"0,3");
	}
	
	public void setEditable(boolean b) {
		customerChooseButton.setEnabled(b);
		transferChooseButton.setEnabled(b);
		transferDeleteButton.setEnabled(b);
		receiptButton.setEnabled(b);
		paymentButton.setEnabled(b);
	}
	
	private void addItem(){
        String[] newRow = new InputTransferItemInfoWin().getRowData();
        if(newRow != null) {
            MyTableModel model = (MyTableModel) transferListTable.getModel();
            for(int i = 0; i < model.getRowCount(); i++) {
            	if (newRow[0].equals(model.getValueAt(i, 1))) {new InfoWindow("表格中已有此账户"); return;}
            }
            model.addRow(newRow);
            sumUp();
        }
    }
	
    private void deleteItem(){
	    int row = transferListTable.getSelectedRow();
	    if(row >= 0) {
	    	((MyTableModel)transferListTable.getModel()).removeRow(transferListTable.getSelectedRow());
	    	sumUp();
	    }
	    else new InfoWindow("请选择删除的转账信息");
    }
	
    private void sumUp(){
	    MyTableModel model = (MyTableModel)this.transferListTable.getModel();
        double total = 0;
	    for(int i = 0; i < model.getRowCount(); i++){
	        total += Double.parseDouble((String)model.getValueAt(i, 1));
	    }
	    sumField.setText(Double.toString(total));
    }
    
    private void handleChooseCustomer(){
        CustomerVO c = new CustomerChooseWin().getCustomer();
        if(c == null) return;
        customerIdField.setText(c.getId());
        customerNameField.setText(c.getName());
    }

	protected void clear(){
		receiptButton.setSelected(true);
		billIdField.setText(receiptBillBL.getNewId());
        customerIdField.setText("");
        customerNameField.setText("");
        MyTableModel model = (MyTableModel) transferListTable.getModel();
        while(model.getRowCount() > 0){
            model.removeRow(0);
        }
        sumField.setText("0.0");
    }
	
	private boolean isCorrectable() {
		if (transferListTable.getRowCount() == 0) new InfoWindow("没有选择转账条目");
		else if ("".equals(customerIdField.getText())) new InfoWindow("请选择客户");
		else return true;
		return false;
	}
	
	/**
	 * 获得单据VO
	 * @return
	 */
	public ReceiptBillVO getReceiptBill(int state) {
		if(!isCorrectable()) return null;
        String date = bill == null ? Timetools.getDate() : bill.getDate();
        String time = bill == null ? Timetools.getTime() : bill.getTime();
        String id = bill == null ? receiptBillBL.getNewId().split("-")[2] : billIdField.getText().split("-")[2]
             , operater = user.getId()
             , customerId = customerIdField.getText();
        MyTableModel model = (MyTableModel)transferListTable.getModel();
        ReceiptBillVO receiptBillVO= new ReceiptBillVO(date, time, id, operater, state, customerId, model); 
        return receiptBillVO;
    }
	/**
	 * 获得单据VO
	 * @return
	 */
	public PaymentBillVO getPaymentBill(int state) {
		if(!isCorrectable()) return null;
		String date = bill == null ? Timetools.getDate() : bill.getDate();
        String time = bill == null ? Timetools.getTime() : bill.getTime();
        String id = bill == null ? paymentBillBL.getNewId().split("-")[2] : billIdField.getText().split("-")[2]
             , operater = user.getId()
             , customerId = customerIdField.getText();
        MyTableModel model = (MyTableModel)transferListTable.getModel();
        PaymentBillVO paymentBillVO= new PaymentBillVO(date, time, id, operater, state, customerId, model); 
        return paymentBillVO;
    }
	
	public BillVO getBill(int state) {
		if (receiptButton.isSelected()) return getReceiptBill(state);
		else return getPaymentBill(state);
	}
	
	private void setBillId(boolean isReceipt) {
		if (isReceipt) billIdField.setText(receiptBillBL.getNewId());
		else billIdField.setText(paymentBillBL.getNewId());
	}

	@Override
	public void newAction() {
		int response = JOptionPane.showConfirmDialog(null, "放弃当前未保存的工作新建一张单据？","警告",JOptionPane.YES_NO_OPTION);
		 if (response == 0) {
			 clear();
		 }
	}

	@Override
	public void saveAction() {
		if (receiptButton.isSelected()) {
			ReceiptBillVO bill = getReceiptBill(BillVO.SAVED);
	        if(bill != null && receiptBillBL.saveBill(bill)) new InfoWindow("单据已保存");
	        else new InfoWindow("单据保存失败");
		} else if (paymentButton.isSelected()) {
			PaymentBillVO bill = getPaymentBill(BillVO.SAVED);
	        if(bill != null && paymentBillBL.saveBill(bill)) new InfoWindow("单据已保存");
	        else new InfoWindow("单据保存失败");
		}
	}

	@Override
	public void commitAction() {
		if (receiptButton.isSelected()) {
			ReceiptBillVO bill = getReceiptBill(BillVO.COMMITED);
	        if(bill != null && receiptBillBL.saveBill(bill)){
	            JOptionPane.showMessageDialog(null, "单据已提交。");
	        }
		} else if (paymentButton.isSelected()) {
			PaymentBillVO bill = getPaymentBill(BillVO.COMMITED);
	        if(bill != null && paymentBillBL.saveBill(bill)){
	            JOptionPane.showMessageDialog(null, "单据已提交。");
	        }
		}
	}
}
