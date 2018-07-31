package presentation.dataui.customerui;

import java.awt.BorderLayout;

import blservice.CustomerBLService;
import presentation.dataui.FatherWindow;
import vo.CustomerVO;

public class AddCustomerWindow extends FatherWindow{
	
    private InputCustomerPanel centerPanel;
	private CustomerBLService customerBL;
	
	public AddCustomerWindow(int userRank, CustomerBLService customerBl) {
        super();
        this.customerBL = customerBl;
        
        frame.setTitle("增加客户");
        centerPanel = new InputCustomerPanel(new String[]{customerBl.getNewId(), null, null, null, null, null, null, null, "50000", "0", "0", null}, userRank==1);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setSize(400, 550);
        frame.setVisible(true);
	}

	@Override
	protected boolean taskFinished() {
		CustomerVO customer = centerPanel.getCustomerVO();
        return customer != null && customerBL.add(customer);
	}

	@Override
	protected String getSuccessMsg() {
        return "添加客户成功";
	}

}
