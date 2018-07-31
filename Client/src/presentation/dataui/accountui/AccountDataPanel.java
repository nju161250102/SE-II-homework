package presentation.dataui.accountui;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.AccountBLService;
import presentation.component.InfoWindow;
import presentation.dataui.DataPanelInterface;
import presentation.main.MainWindow;
import vo.MyTableModel;
import vo.UserVO;

public class AccountDataPanel extends JScrollPane implements DataPanelInterface{
	private JTable table = new JTable();
	private AccountBLService accountBL;
	private UserVO user = MainWindow.getUser();

	public AccountDataPanel(AccountBLService accountBL) {
		this.accountBL = accountBL;
		table.setModel(accountBL.update());
		table.getTableHeader().setReorderingAllowed(false);
		this.setViewportView(table);
	}
	@Override
	public void addAction() {
		if (user.getRank() != 0) {
    		new AddAccountWindow(accountBL);
    		table.setModel(accountBL.update());
    	} else new InfoWindow("您的权限不足");
	}
	@Override
	public void updateAction() {
		if (user.getRank() != 0) {
            MyTableModel tableModel = (MyTableModel)table.getModel();
            if (table.getSelectedRow() != -1) {
            	new UpdateAccountWindow(accountBL, tableModel.getValueAtRow(table.getSelectedRow()));
        		table.setModel(accountBL.update());
            }
            else new InfoWindow("请选择需要修改的银行账户");
    	} else new InfoWindow("您的权限不足");
	}
	@Override
	public void searchAction() {
		if (user.getRank() != 0) {
            MyTableModel model = new SearchAccountWindow(accountBL).getModel();
            if(model != null) table.setModel(model);
    	} else new InfoWindow("您的权限不足");
	}
	@Override
	public void deleteAction(){
		if (user.getRank() != 0) {
			int index = table.getSelectedRow();
		    if(index < 0) {new InfoWindow("请选择需要删除的银行账户");return;}
		    //暂且不管余额多少
			int response = JOptionPane.showConfirmDialog(null, "确认要删除此条信息？", "提示", JOptionPane.YES_NO_OPTION);
			if (response == 0) {
			    String id = (String)((MyTableModel)table.getModel()).getValueAt(index, 0);
				if (accountBL.delete(id)) new InfoWindow("信息已成功删除");
				table.setModel(accountBL.update());
			}
    	} else new InfoWindow("您的权限不足");
	}
}
