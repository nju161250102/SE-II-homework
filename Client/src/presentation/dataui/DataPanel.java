package presentation.dataui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import blservice.AccountBLService;
import blservice.CommodityBLService;
import blservice.CustomerBLService;
import blservice.UserBLService;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.TopButtonPanel;
import presentation.dataui.accountui.AccountDataPanel;
import presentation.dataui.commodityui.CommodityDataPanel;
import presentation.dataui.customerui.CustomerDataPanel;
import presentation.dataui.userui.UserDataPanel;
/**
 * 处理基础数据的通用Panel组件 </br>
 * 包括商品信息、客户信息、账户信息、用户信息</br>
 * @author 钱美缘
 *
 */
public class DataPanel implements PanelInterface {
	private JPanel panel = new JPanel();

	public DataPanel(AccountBLService accountBL, ActionListener closeListener) {
		initDataPanel(new AccountDataPanel(accountBL), closeListener);
	}
	public DataPanel(CommodityBLService commodityBL, ActionListener closeListener) {
		initDataPanel(new CommodityDataPanel(commodityBL), closeListener);
	}
	public DataPanel(CustomerBLService customerBL, ActionListener closeListener) {
		initDataPanel(new CustomerDataPanel(customerBL), closeListener);
	}
	public DataPanel(UserBLService userBL, ActionListener closeListener) {
		initDataPanel(new UserDataPanel(userBL), closeListener);
	}
	private void initDataPanel(DataPanelInterface dataImpl, ActionListener closeListener) {
		double[][] size = {{TableLayout.FILL},{TableLayout.PREFERRED,TableLayout.FILL}};
		panel.setLayout(new TableLayout(size));
		
		TopButtonPanel buttonPanel = new TopButtonPanel();
		buttonPanel.addButton("增加", new ImageIcon("resource/AddData.png"), e->dataImpl.addAction());
		buttonPanel.addButton("修改", new ImageIcon("resource/ChangeData.png"), e->dataImpl.updateAction());
		buttonPanel.addButton("查询", new ImageIcon("resource/SearchData.png"), e->dataImpl.searchAction());
		buttonPanel.addButton("删除", new ImageIcon("resource/DeleteData.png"), e->dataImpl.deleteAction());
		//buttonPanel.addButton("刷新", new ImageIcon("resource/Refresh.png"), e -> {table.setModel(dataBL.update());TableTools.autoFit(table);});
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		panel.add(buttonPanel.getPanel(), "0,0");
		panel.add((JScrollPane)dataImpl, "0,1");
	}
	
	@Override
	public boolean close() {
		return true;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
