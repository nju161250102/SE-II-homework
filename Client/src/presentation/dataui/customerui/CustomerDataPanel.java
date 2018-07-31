package presentation.dataui.customerui;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.CustomerBLService;
import presentation.component.InfoWindow;
import presentation.dataui.DataPanelInterface;
import presentation.main.MainWindow;
import presentation.tools.TableTools;
import vo.MyTableModel;
import vo.UserVO;

public class CustomerDataPanel extends JScrollPane implements DataPanelInterface{
	private JTable table = new JTable();
    private CustomerBLService customerBL;
    private UserVO user = MainWindow.getUser();

	public CustomerDataPanel(CustomerBLService customerBL) {
		this.customerBL = customerBL;
		table.setModel(customerBL.update());
		table.getTableHeader().setReorderingAllowed(false);
		TableTools.autoFit(table);
		this.setViewportView(table);
	}

	@Override
	public void addAction() {
		new AddCustomerWindow(user.getRank(), customerBL);
		table.setModel(customerBL.update());
		TableTools.autoFit(table);
	}

	@Override
	public void updateAction() {
		MyTableModel tableModel = (MyTableModel)table.getModel();
        if (table.getSelectedRow() != -1) {
            new UpdateCustomerWindow(customerBL, tableModel.getValueAtRow(table.getSelectedRow()), user.getRank());
            table.setModel(customerBL.update());
    		TableTools.autoFit(table);
        }
        else new InfoWindow("请选择需要修改的客户");
	}

	@Override
	public void searchAction() {
		MyTableModel model = new SearchCustomerWindow(customerBL).getModel();
        if(model != null) table.setModel(model);
 		TableTools.autoFit(table);
	}

	@Override
	public void deleteAction() {
		int index = table.getSelectedRow();
	    if(index < 0) {new InfoWindow("请选择需要删除的客户信息");return;}
		int response = JOptionPane.showConfirmDialog(null, "确认要删除此条客户信息？", "提示", JOptionPane.YES_NO_OPTION);
		if (response == 0) {
		    String id = (String)((MyTableModel)table.getModel()).getValueAt(index, 0);
			if (customerBL.delete(id)) new InfoWindow("选中的客户信息已成功删除");
			table.setModel(customerBL.update());
			TableTools.autoFit(table);
		}
	}
}
