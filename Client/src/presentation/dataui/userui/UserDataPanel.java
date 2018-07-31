package presentation.dataui.userui;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.UserBLService;
import presentation.component.InfoWindow;
import presentation.dataui.DataPanelInterface;
import vo.MyTableModel;

public class UserDataPanel extends JScrollPane implements DataPanelInterface{
	private JTable table = new JTable();
	private UserBLService userBL;

    public UserDataPanel(UserBLService userBL) {
        this.userBL = userBL;
        table.setModel(userBL.update());
		table.getTableHeader().setReorderingAllowed(false);
		this.setViewportView(table);
    }

    @Override
    public void addAction() {
    	new AddUserWindow(userBL);
    	table.setModel(userBL.update());
    }

    @Override
    public void updateAction() {
    	MyTableModel tableModel = (MyTableModel)table.getModel();
        if (table.getSelectedRow() != -1) {
        	new UpdateUserWindow(userBL, tableModel.getValueAtRow(table.getSelectedRow()),false);
        	table.setModel(userBL.update());
        } else new InfoWindow("请选择需要修改的用户");
    }
    
    @Override
    public void searchAction() {
    	MyTableModel model = new SearchUserWindow(userBL).getModel();
    	if(model != null)table.setModel(model);
    }

	@Override
	public void deleteAction() {
		int index = table.getSelectedRow();
	    if(index < 0) {new InfoWindow("请选择需要删除的商品信息");return;}
		int response = JOptionPane.showConfirmDialog(null, "确认要删除此条商品信息？", "提示", JOptionPane.YES_NO_OPTION);
		if (response == 0) {
		    String id = (String)((MyTableModel)table.getModel()).getValueAt(index, 0);
			if (userBL.delete(id)) new InfoWindow("选中的商品信息已成功删除");
			table.setModel(userBL.update());
		}
	}
}
