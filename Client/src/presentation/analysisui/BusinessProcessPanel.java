package presentation.analysisui;


import javax.swing.*;
import layout.TableLayout;
import java.awt.*;
import javax.swing.UIManager;

import presentation.main.MainWindow;
import presentation.PanelInterface;
import vo.UserVO;

/**
 * 查看经营历程表的展示面板。<br>
 * @author 郝睿
 */
public class BusinessProcessPanel implements PanelInterface {
    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel operationPanel,scrollPanel;
    private JButton findButton,exportButton,closeButton;
    private JTable businessProcessListTable;
	private JScrollPane businessProcessListPane;
	private String[] billListAttributes = {"单据编号", "单据类型", "操作员", "提交时间"};
	private String[][] billInfo = {{"0001", "进货单", "清流", "2017.10.26"} , {"0002", "进货退货单", "浊流","2017.10.28"}};
	
	public BusinessProcessPanel(MainWindow mainWindow) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格，jdk6 update10版本以后的才会出现
		}catch(Exception e){
			e.printStackTrace();
		}
		
		findButton = new JButton ("查询");
		exportButton = new JButton ("导出");
		closeButton = new JButton ("关闭");
		
		operationPanel = new JPanel();
		double operationPanelSize[][]={
				{20,100,100,100,100,100,TableLayout.FILL,100,100,20},
				{TableLayout.FILL,25,TableLayout.FILL}
		};
		operationPanel.setLayout(new TableLayout(operationPanelSize));
		operationPanel.add(findButton,"1,1");
		operationPanel.add(exportButton,"3,1");
		operationPanel.add(closeButton,"8,1");
		
		scrollPanel=new JPanel();
		double scrollPanelSize[][]={
				{20,TableLayout.FILL,20},
				{TableLayout.FILL},
		};
		scrollPanel.setLayout(new TableLayout(scrollPanelSize));
		businessProcessListTable=new JTable(billInfo, billListAttributes);
		businessProcessListPane=new JScrollPane(businessProcessListTable);
		scrollPanel.add(businessProcessListPane, "1,0");
		
		panel = new JPanel();
		double mainPanelSize[][]={
				{TableLayout.FILL},
				{0.1,0.1,0.1,0.45,0.15,0.1}	
		};
		panel.setLayout(new TableLayout(mainPanelSize));
		panel.add(operationPanel, "0,0");
		panel.add(scrollPanel, "0,1");
	}
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return panel;
	}
	
/*	public static void main(String[] args){
		UserVO user =new UserVO();
		ViewBusinessProcessTablePanel test=new ViewBusinessProcessTablePanel();
		test.init(user);
		
		JFrame testFrame=new JFrame("BillsReview");
		testFrame.add(test.getPanel());
		testFrame.setVisible(true);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(testFrame);

	}		*/

}