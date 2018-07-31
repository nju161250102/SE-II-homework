package presentation.dataui.customerui;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import blservice.CustomerBLService;
import presentation.dataui.SearchWindow;

public class SearchCustomerWindow extends SearchWindow{

	public SearchCustomerWindow(CustomerBLService customerBl) {
		super(customerBl);
        frame.setTitle("查询客户");
        frame.setSize(300, 200);
        frame.setVisible(true);
	}

	@Override
	protected ButtonGroup initTypeGroup() {
        JRadioButton idRadioButton = new JRadioButton("按编号搜索");
        JRadioButton keyRadioButton = new JRadioButton("按关键字搜索");
        idRadioButton.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(idRadioButton);
        typeGroup.add(keyRadioButton);
        return typeGroup;
	}

}
