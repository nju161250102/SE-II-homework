package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.AccountBLService;
import blservice.infoservice.GetAccountInterface;
import businesslogic.inter.AddLogInterface;
import dataservice.AccountDataService;
import ds_stub.AccountDs_stub;
import po.AccountPO;
import presentation.main.MainWindow;
import rmi.Rmi;
import vo.AccountVO;
import vo.MyTableModel;

public class AccountBL implements AccountBLService, GetAccountInterface {

	private AccountDataService accountDataService = Rmi.flag ? Rmi.getRemote(AccountDataService.class) : new AccountDs_stub();
	private AddLogInterface addLog = new LogBL();
	private String[] tableHeader = {"银行账号", "账户名称", "余额"};
	private int userRank = MainWindow.getUser().getRank();
	
	private String[] getLine(AccountPO account) {
		return new String[] {
				account.getId(), 
				account.getName(),
				userRank==1?Double.toString(account.getMoney()):"****"
		};
	}
	
	@Override
	public String getNewId() {
		return "";
	}

	@Override
	public boolean delete(String id) {
		try {
			if (accountDataService.delete(id)) {
				addLog.add("删除账户", "删除的账户卡号为："+id);
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public MyTableModel search(String type, String key) {
		try {
			ArrayList<AccountPO> list = null;
			if ("按账号搜索".equals(type)) {
				list = accountDataService.getAccountsBy("AccountID", key, true);
			} else {
			    // search by name
			    list = accountDataService.getAccountsBy("AccountName", key, true);
			}
			String[][] data = new String [list.size()][tableHeader.length];
			for (int i = 0; i < list.size(); i++) {
				data[i] = getLine(list.get(i));
			}
			MyTableModel searchTable = new MyTableModel (data, tableHeader);
			return searchTable;
		} catch (Exception e) {
			return new MyTableModel (new String[][]{{}}, tableHeader);
		}
	}

	@Override
	public MyTableModel update() {
		try {
			ArrayList<AccountPO> list = accountDataService.getAllAccount();
			String[][] data = new String [list.size()][tableHeader.length];
			for (int i = 0; i < list.size(); i++) {
				data[i] = getLine(list.get(i));
			}
			MyTableModel updateTable = new MyTableModel (data, tableHeader);
			return updateTable;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean add(AccountVO account) {
		try {
			AccountPO accountPO = account.toPO();
			if (accountDataService.add(accountPO)) {
				addLog.add("增加账户", "新账户卡号："+account.getNumber());
				return true;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean change(AccountVO account) {
		try {
			AccountPO accountPO = account.toPO();
			if (accountDataService.update(accountPO)) {
				addLog.add("修改账户", "修改的账户号码为："+account.getNumber());
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public AccountVO getAccount(String id) {
		AccountPO account = null;
		try {
			account = accountDataService.findById(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (account != null) {
			return new AccountVO(
					account.getName(),
					account.getId(),
					account.getMoney()); 
		}
		return null;
	}
}
