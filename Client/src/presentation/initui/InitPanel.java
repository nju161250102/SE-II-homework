package presentation.initui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import blservice.InitBLService;
import businesslogic.InitBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;

public class InitPanel implements PanelInterface {

	private JPanel panel = new JPanel();
	private JTable commodityTable = new JTable();
	private JTable customerTable = new JTable();
	private JTable accountTable = new JTable();
	private String year;
	private InitBLService initBL;
	
	public InitPanel(ActionListener closeListener) {
		initBL = new InitBL();
		year = initBL.getYear();
		
		double[][] size = {{TableLayout.FILL},{0.1,TableLayout.FILL}};
		panel.setLayout(new TableLayout(size));

		JTabbedPane tabbedPane = new JTabbedPane();
		commodityTable.getTableHeader().setReorderingAllowed(false);
		customerTable.getTableHeader().setReorderingAllowed(false);
		accountTable.getTableHeader().setReorderingAllowed(false);
		update();
		
		class FindListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				year = new InitChooseWin(initBL).getYear();
				update();
			}
		};
		class InitListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int response = JOptionPane.showConfirmDialog(null, "确认期初信息无误并建立新的账套？", "建账确认", JOptionPane.YES_NO_OPTION);
				if (response == 0) {
					if (initBL.initNewOne()) {
						new InfoWindow("期初建账成功");
						year = initBL.getYear();
						update();
					}
					else new InfoWindow("期初建账失败");
				}
			}
		};
		
		TopButtonPanel buttonPanel = new TopButtonPanel();
		buttonPanel.addButton("期初信息查询", new ImageIcon("resource/SearchData.png"), new FindListener());
		buttonPanel.addButton("期初建账", new ImageIcon("resource/Init.png"), new InitListener());
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		panel.add(buttonPanel.getPanel(), "0,0");
		
		JScrollPane commodityPane = new JScrollPane(commodityTable);
		JScrollPane customerPane = new JScrollPane(customerTable);
		JScrollPane accountPane = new JScrollPane(accountTable);
		tabbedPane.add("商品信息", commodityPane);
		tabbedPane.add("客户信息", customerPane);
		tabbedPane.add("账户信息", accountPane);
		panel.add(tabbedPane, "0,1");
	}
	
	@Override
	public boolean close() {
		return true;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	private void update() {
		commodityTable.setModel(initBL.getCommodityInfo(year));
		customerTable.setModel(initBL.getCustomerInfo(year));
		accountTable.setModel(initBL.getAccountInfo(year));
	}
}
