package presentation.component.choosewindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public abstract class ChooseWindow {

	protected JDialog frame = new JDialog();
	protected JTable table = new JTable();
	protected JComboBox<String> searchTypeBox;
	protected JTextField keyField = new JTextField(15);
	
	public ChooseWindow() {
		frame.setModal(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width/2, screenSize.height/2);
		frame.setLocation(screenSize.width/4, screenSize.height/4);
		frame.setResizable(false);
		
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(new JLabel("选择搜索方式"));
		searchTypeBox = new JComboBox<String>();
		searchPanel.add(searchTypeBox);
		searchPanel.add(keyField);
		JButton searchButton = new JButton("搜索");
		searchPanel.add(searchButton);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		JScrollPane tablePane = new JScrollPane(table);
		//tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton yesButton = new JButton("确定");
		JButton quitButton = new JButton("取消");
		southPanel.add(yesButton);
		southPanel.add(quitButton);
		
		frame.setLayout(new BorderLayout());
		frame.add(searchPanel, BorderLayout.NORTH);
		frame.add(tablePane, BorderLayout.CENTER);
		frame.add(southPanel, BorderLayout.SOUTH);
		
		searchButton.addActionListener(e -> searchAction());
		quitButton.addActionListener(e -> frame.dispose());
		yesButton.addActionListener(e -> yesAction());

		init();
	}
		     
	protected void setTypes(String[] searchTypes) {
		for (String str : searchTypes) {
			searchTypeBox.addItem(str);
		}
	}
	
	/**
	 * 初始化选择窗口，包括搜索方式字符串数组和表格，最后一行一定记得写frame.setVisible(true);
	 */
	abstract public void init();
	
	abstract protected void yesAction();
	
	abstract protected void searchAction();
}
