package presentation.component.choosewindow;

import javax.swing.JTable;

import blservice.AccountBLService;
import blservice.infoservice.GetAccountInterface;
import businesslogic.AccountBL;
import vo.AccountVO;

public class AccountChooseWin extends ChooseWindow {

	private AccountVO account;
	
	public AccountChooseWin() {
		super();
	}
	
	@Override
	public void init() {
		AccountBLService accountBl = new AccountBL();
        setTypes(new String[]{"按账号id搜索", "按账户名称搜索"});
        table.setModel(accountBl.update());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        frame.setTitle("选择账户");
        frame.setVisible(true);		
	}

	@Override
	protected void yesAction() {
		GetAccountInterface accountInfo = new AccountBL();
        int index = table.getSelectedRow();
        if(index < 0) return;
        String id = (String) table.getValueAt(index, 0);
        account = accountInfo.getAccount(id);
        frame.dispose();		
	}

	public AccountVO getAccount(){
        return account;
    }

	@Override
	protected void searchAction() {
		// TODO Auto-generated method stub
		
	}
}
