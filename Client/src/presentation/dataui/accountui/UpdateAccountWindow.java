package presentation.dataui.accountui;

import java.awt.BorderLayout;

import blservice.AccountBLService;
import presentation.dataui.FatherWindow;
import presentation.dataui.accountui.InputAccountPanel;
import vo.AccountVO;

public class UpdateAccountWindow extends FatherWindow{
	
	private InputAccountPanel centerPanel;
    private AccountBLService accountBl;

	public UpdateAccountWindow(AccountBLService accountBl, String[] data) {
		super();
		this.accountBl = accountBl;
        frame.setTitle("修改账户");
        centerPanel = new InputAccountPanel(data, false);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setSize(400, 200);
        frame.setVisible(true);
	}

    @Override
    protected boolean taskFinished() {
        AccountVO account = centerPanel.getAccountVO();
        return account != null && accountBl.change(account);
    }

    @Override
    protected String getSuccessMsg() {
        return "账户信息已更新";
    }
}
