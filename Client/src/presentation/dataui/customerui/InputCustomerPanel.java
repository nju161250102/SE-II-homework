package presentation.dataui.customerui;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.tools.InputCheck;
import vo.CustomerVO;

public class InputCustomerPanel extends JPanel{
	private JTextField customerIdTextField, customerNameTextField, customerTelNumberTextField,
	customerAddressTextField, customerCodeTextField, customerMailTextField, customerSalesmanTextField,
	customerRecRangeTextField; 
	
	private String[] customer;
	private JComboBox<String> comboBox;
	private JRadioButton purchaseRadioButton;
	
	/**
	 * 根据已有数据初始化界面
	 * @param 表格中取出的一行数据
	 */
	protected InputCustomerPanel(String[] customer, boolean isSaleGM) {
		super();
		this.customer = customer;
		double[] rows = new double[37];
        rows[0] = TableLayout.FILL;
        for(int i = 0; i < 12; i++){
            rows[3 * i + 1] = TableLayout.PREFERRED;
            rows[3 * i + 2] = 10.0;
        }
        rows[rows.length - 1] = TableLayout.FILL;
        double[][] size = {{TableLayout.FILL, TableLayout.PREFERRED, 10.0, 0.5, TableLayout.FILL}, rows};
        this.setLayout(new TableLayout(size));
		
		String[] texts = {"客户编号", "客户姓名", "分类", "级别", "电话", "地址", 
				"邮编", "电子邮箱", "应收额度", "默认业务员"};
        JLabel[] labels = new JLabel[texts.length];
        for(int i = 0; i < labels.length; i++){
            labels[i] = new JLabel(texts[i]);
            this.add(labels[i], "1 " + (3 * i + 1));
        }
		
        customerIdTextField = new JTextField(customer[0]);
		customerNameTextField = new JTextField(customer[1]);
		customerTelNumberTextField = new JTextField(customer[4]);
        customerAddressTextField = new JTextField(customer[5]);
        customerCodeTextField = new JTextField(customer[6]);
        customerMailTextField = new JTextField(customer[7]);
        customerRecRangeTextField = new JTextField(customer[8]);
        customerSalesmanTextField = new JTextField(customer[11]);


        customerIdTextField.setEditable(false);
        customerRecRangeTextField.setEditable(isSaleGM);

		purchaseRadioButton = new JRadioButton("进货商");
	    JRadioButton saleRadioButton = new JRadioButton("销售商");
		purchaseRadioButton.setSelected(true);
		JPanel typePanel = new JPanel();
		typePanel.add(purchaseRadioButton);
		typePanel.add(saleRadioButton);
		ButtonGroup typeButtonGroup = new ButtonGroup();
		typeButtonGroup.add(purchaseRadioButton);
		typeButtonGroup.add(saleRadioButton);
		
		comboBox = new JComboBox<String>(new String[]{"LV1","LV2","LV3","LV4","LV5"});

		int length = customer.length;
		
		if(length > 2 && customer[2] != null){
			if(customer[2].equals(purchaseRadioButton.getText()))
		        purchaseRadioButton.setSelected(true);
		    else saleRadioButton.setSelected(true);
		}

		if(length > 3 && customer[3] != null){
		    Enumeration<AbstractButton> eb = typeButtonGroup.getElements();
		    while(eb.hasMoreElements()){
		        AbstractButton b = eb.nextElement(); 
		        if(b.getText().equals(customer[3])){
		            b.setSelected(true);
		            break;
		        }
		    }
		}
		
		add(customerIdTextField, "3 1");
		add(customerNameTextField, "3 4");
		add(typePanel, "3 7");
		add(comboBox, "3 10");
		add(customerTelNumberTextField, "3 13");
		add(customerAddressTextField, "3 16");
		add(customerCodeTextField, "3 19");
		add(customerMailTextField, "3 22");
		add(customerRecRangeTextField, "3 25");
		add(customerSalesmanTextField, "3 28");
	}
	
	/**
	 * 得到面板输入的CustomerVO对象
	 * @return
	 */
	public CustomerVO getCustomerVO() {
		String id = customerIdTextField.getText(),
	    		name = customerNameTextField.getText(),
	    		telNumber = customerTelNumberTextField.getText(), 
	    		address = customerAddressTextField.getText(), 
	    		code = customerCodeTextField.getText(),
	    		mail = customerMailTextField.getText(),
	    		salesman = customerSalesmanTextField.getText();
		if (! InputCheck.isLegal(customerNameTextField.getText())) new InfoWindow("请输入合法的姓名");
		else if (! InputCheck.isAllNumber(telNumber, 11)) new InfoWindow("请输入正确的手机号");
		else if (! InputCheck.isLegal(address)) new InfoWindow("请输入合法的地址");
		else if (! InputCheck.isAllNumber(code, 6)) new InfoWindow("请输入正确的邮编");
		else if (! InputCheck.isLegal(salesman)) new InfoWindow("请输入合法的业务员姓名");
		else if (! InputCheck.isDouble(customerRecRangeTextField.getText())) new InfoWindow("请输入正确的应收额度");
		else {
		    int type = purchaseRadioButton.isSelected()?0:1;
		    int rank = comboBox.getSelectedIndex()+1;
	        double recRange = Double.parseDouble(customerRecRangeTextField.getText());
	        double receivable = Double.parseDouble(customer[9]);
	        double payment = Double.parseDouble(customer[10]);
	        
		    return new CustomerVO(id, name, type, rank, telNumber, address, code, mail, recRange, receivable, payment, salesman);
		}
	    return null;
	}
}		
