package presentation.billui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import blservice.infoservice.GetCustomerInterface;
import blservice.infoservice.GetUserInterface;
import businesslogic.CustomerBL;
import businesslogic.UserBL;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CustomerChooseWin;
import presentation.tools.DoubleField;
import presentation.tools.InputCheck;
import vo.CustomerVO;
import vo.MyTableModel;
import vo.UserVO;
import vo.billvo.BillVO;
import vo.billvo.MarketBillVO;

/**
 * 用于销售类单据的基类<br>
 * 其中销售单由于存在优惠和折扣，还需要额外重写某些方法外，其他只需要实现这里的抽象方法即可。
 * @author 恽叶霄
 */
public abstract class CommonSaleBillPanel extends JPanel implements BillPanelInterface {

	protected UserVO user;
	protected BillVO bill;
    protected JTextField billIdField, operatorField, customerIdField, customerNameField, remarkField;
    protected DoubleField sumField;
    protected JButton customerChooseButton, goodsChooseButton, goodsDeleteButton;
	protected JTable goodsListTable;
	
	private GetUserInterface userInfo = new UserBL();
	private GetCustomerInterface customerInfo = new CustomerBL();

    public CommonSaleBillPanel(UserVO user) {
    	this.user = user;
		initBillPanel();
    }
    
    public CommonSaleBillPanel(UserVO user, MarketBillVO bill){
    	this.user = user;
    	this.bill = bill;
		initBillPanel();
        billIdField.setText(bill.getAllId());
        operatorField.setText(userInfo.getUser(bill.getOperator()).getName());
        customerIdField.setText(bill.getCustomerId());
        customerNameField.setText(customerInfo.getCustomer(bill.getCustomerId()).getName());
        remarkField.setText(bill.getRemark());
        sumField.setText(bill.getSum() + "");
        goodsListTable.setModel(bill.getModel());
    }

    private void initBillPanel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格，jdk6 update10版本以后的才会出现
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.setLayout(new BorderLayout());
		initNorth();
		initCenter();
		initEast();
		initSouth();
    }
    
    protected boolean isCorrectable(){
        if (customerIdField.getText().length() == 0) new InfoWindow("请选择客户");
        else if (goodsListTable.getModel().getRowCount() == 0) new InfoWindow("列表为空");
        else if (! InputCheck.isLegalOrBlank(remarkField.getText())) new InfoWindow("备注非法");
        else return true;
        return false;
    }
    
    public void setEditable(boolean b) {
    	customerChooseButton.setEnabled(b);
    	goodsChooseButton.setEnabled(b);
    	goodsDeleteButton.setEnabled(b);
    	remarkField.setEditable(b);
    }
    
    abstract protected String getObjectType();
    
    abstract protected String getTableTitle();
    
    
    protected JPanel getCustomerPanel(){
        customerIdField = new JTextField(10);
        customerIdField.setEditable(false);
        customerNameField = new JTextField(10);
		customerNameField.setEditable(false);
	    double size[][]={
				{20,45,5,70,12,100,5,60,TableLayout.FILL},
				{8,25,TableLayout.FILL}
		};
		JPanel customerInfoPanel=new JPanel(new TableLayout(size));
		customerChooseButton = new JButton("选择");
		customerChooseButton.addActionListener(e -> handleChooseCustomer());
		customerInfoPanel.add(new JLabel(getObjectType()),"1,1");
		customerInfoPanel.add(customerIdField,"3,1");
		customerInfoPanel.add(new JLabel("--"),"4,1");
		customerInfoPanel.add(customerNameField,"5,1");
		customerInfoPanel.add(customerChooseButton,"7,1");
		return customerInfoPanel;
    }
    
    
    
    protected void initSouth(){
        remarkField = new JTextField(10);
        sumField = new DoubleField(5);
        sumField.setEditable(false);
        double[][] size = {
                {20.0, -2.0, 10.0, 100.0, -1.0, -1.0},
                {10.0, -1.0, 10.0, -1.0, -1.0}
        };
        JPanel southPanel = new JPanel(new TableLayout(size));
        southPanel.add(new JLabel("        备注"), "1 1");
        southPanel.add(remarkField, "3 1 4 1");
        southPanel.add(new JLabel("金额合计"), "1 3");
        southPanel.add(sumField, "3 3");
        
        this.add(southPanel, BorderLayout.SOUTH);
    }
    
    protected void handleAddItem(){
        String[] newRow = new InputCommodityInfoWin().getRowData();
        if (newRow != null && !newRow[5].equals("0")) {
            addItem(newRow);
        } 
    }
    
    protected void handleChooseCustomer(boolean isPurchase){
        CustomerVO c = new CustomerChooseWin().getCustomer();
        if(c == null) return;
        if (c.getType() == (isPurchase?0:1)) {
        	customerIdField.setText(c.getId());
        	customerNameField.setText(c.getName());
        } else new InfoWindow(isPurchase?"请选择进货商":"请选择销售商");
    }
    
    abstract protected void handleChooseCustomer();

    protected void sumUp(){
	    MyTableModel model = (MyTableModel)this.goodsListTable.getModel();
        double total = 0;
	    for(int i = 0; i < model.getRowCount(); i++){
	        total += Double.parseDouble((String)model.getValueAt(i, 6));
	    }
	    sumField.setValue(total);
    }

    protected void clear(){
        billIdField.setText("");
        customerIdField.setText("");
        customerNameField.setText("");
        remarkField.setText("");
        remarkField.setEditable(true);
        sumField.setText("0.0");
        MyTableModel model = (MyTableModel) goodsListTable.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);
    }

    protected String getDate(){
        return billIdField.getText().split("-")[1];
    }

    protected String getId(){
        return billIdField.getText().split("-")[2];
    }

    protected void addItem(String[] newRow){
        MyTableModel model = (MyTableModel) goodsListTable.getModel();
        int rowIndex = getRowIndex(model, newRow[0]);
        if(rowIndex >= 0){
            int amount = Integer.parseInt((String)model.getValueAt(rowIndex, 5))
                       + Integer.parseInt(newRow[5]);
            double sum = Double.parseDouble((String)model.getValueAt(rowIndex, 6))
                       + Double.parseDouble(newRow[6]);
            model.setValueAt(amount + "", rowIndex, 5);
            model.setValueAt(sum + "", rowIndex, 6);
        } else {
            model.addRow(newRow);
        }
        sumUp();
       
    }
    
    private void initNorth(){
        double[][] size = {{-1.0}, {-1.0, -1.0}};
        JPanel northPanel = new JPanel(new TableLayout(size));
        northPanel.add(getHeader(), "0 0");
        northPanel.add(getCustomerPanel(), "0 1");
        
        this.add(northPanel, BorderLayout.NORTH);
    }
    
    private void initCenter(){
        String[] goodsListAttributes={"商品编号","名称","型号","仓库","单价","数量","金额","备注"};
		goodsListTable = new JTable(new MyTableModel(null, goodsListAttributes));
		JScrollPane goodsListPane = new JScrollPane(goodsListTable);

		double size[][]={
				{20,-1.0},
				{10,25,5,TableLayout.FILL}
		};
		JPanel centerPanel = new JPanel(new TableLayout(size));
		centerPanel.add(new JLabel(getTableTitle()),"1,1");
		centerPanel.add(goodsListPane, "1,3");
		
		this.add(centerPanel, BorderLayout.CENTER);
    }
   
    private void initEast(){
        double size[][]={
                {20,TableLayout.PREFERRED,TableLayout.FILL},
                {40.0,25,10,25,10,25,10,TableLayout.FILL},
        };
        JPanel goodsButtonPanel = new JPanel(new TableLayout(size));
        goodsChooseButton=new JButton("选择商品", new ImageIcon("resource/AddButton.png"));
        goodsChooseButton.addActionListener(e -> handleAddItem());
        goodsDeleteButton=new JButton("删除商品", new ImageIcon("resource/DeleteButton.png"));
        goodsDeleteButton.addActionListener(e -> deleteItem());

        goodsButtonPanel.add(goodsChooseButton, "1,1");
        goodsButtonPanel.add(goodsDeleteButton, "1,3");

        this.add(goodsButtonPanel, BorderLayout.EAST);
    }
    private JPanel getHeader(){
        billIdField = new JTextField(15);
        billIdField.setEditable(false);
        operatorField = new JTextField(10);
        operatorField.setEditable(false);
		double size[][]={
				{20,-2.0,5,-2.0,20,-2.0,5,-2.0,20.0,-2.0,5.0,-2.0,TableLayout.FILL},
				{12,25,TableLayout.FILL}
		};
	    JPanel headPanel = new JPanel(new TableLayout(size));
		headPanel.add(new JLabel("单据编号"),"1,1");
		headPanel.add(billIdField,"3,1");
		headPanel.add(new JLabel("操作人"),"5,1");
		headPanel.add(operatorField,"7,1");
		return headPanel;
    }
    
    private int getRowIndex(MyTableModel model, String comId){
        for(int i = 0; i < model.getRowCount(); i++){
            String[] row = model.getValueAtRow(i);
            if(row[0].equals(comId)){
                return i;
            }
        }
        return -1;
    }

    private void deleteItem(){
	    int row = goodsListTable.getSelectedRow();
	    if(row >= 0) {
	    	((MyTableModel)goodsListTable.getModel()).removeRow(goodsListTable.getSelectedRow());
	    	sumUp();
	    } else new InfoWindow("请选择删除的商品");
    }
}
