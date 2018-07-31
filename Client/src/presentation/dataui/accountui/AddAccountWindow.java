package presentation.dataui.accountui;

import java.awt.BorderLayout;

import blservice.AccountBLService;
import presentation.dataui.FatherWindow;
import vo.AccountVO;

public class AddAccountWindow extends FatherWindow{
	
    private InputAccountPanel centerPanel;
	private AccountBLService accountBL;
	
	public AddAccountWindow(AccountBLService accountBl) {
        super();
        this.accountBL = accountBl;
        
        frame.setTitle("增加账户");
        centerPanel = new InputAccountPanel(new String[]{null, null, "0.0"}, true);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setSize(400, 200);
        frame.setVisible(true);
	}

	@Override
	protected boolean taskFinished() {
		AccountVO account = centerPanel.getAccountVO();
        return account != null && accountBL.add(account);
	}

	@Override
	protected String getSuccessMsg() {
        return "添加账户成功";
	}

}
