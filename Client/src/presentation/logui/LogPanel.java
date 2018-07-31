package presentation.logui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.LogBLService;
import businesslogic.LogBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.TopButtonPanel;
import presentation.main.MainWindow;

/**
 * 查看日志使用的Panel
 * @author 钱美缘
 *
 */
public class LogPanel implements PanelInterface {
	
	private JPanel panel;
	private LogBLService logBL;

	public LogPanel(ActionListener closeListener) {
		logBL = new LogBL(MainWindow.getUser());
		
		double[][] size = {{TableLayout.FILL},{0.1,TableLayout.FILL}};
		panel = new JPanel(new TableLayout(size));
		
		JTable table = new JTable(logBL.getLogInfo());
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, "0,1");
		
		class SearchListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] input = new SearchWindow().getInput();
				table.setModel(logBL.searchByTime(input[0], input[1], input[2]));
			}
		}
		
		TopButtonPanel buttonPanel = new TopButtonPanel();
		buttonPanel.addButton("查询", new ImageIcon("resource/SearchData.png"), new SearchListener());
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		panel.add(buttonPanel.getPanel(), "0,0");
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
