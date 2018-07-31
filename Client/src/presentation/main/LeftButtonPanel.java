package presentation.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import businesslogic.AccountBL;
import businesslogic.CategoryBL;
import businesslogic.CommodityBL;
import businesslogic.CustomerBL;
import businesslogic.UserBL;
import layout.TableLayout;
import presentation.analysisui.InventoryCheckPanel;
import presentation.analysisui.InventoryDynamicPanel;
import presentation.analysisui.SalesDetailsPanel;
import presentation.analysisui.ViewBusinessHistoryPanel;
import presentation.analysisui.ViewBusinessSituationPanel;
import presentation.billui.BillExaminePanel;
import presentation.billui.BillPanelHelper;
import presentation.component.InfoAdapter;
import presentation.dataui.DataPanel;
import presentation.dataui.accountui.AccountDataPanel;
import presentation.dataui.categoryui.CategoryDataPanel;
import presentation.dataui.commodityui.CommodityDataPanel;
import presentation.dataui.customerui.CustomerDataPanel;
import presentation.dataui.userui.UserDataPanel;
import presentation.initui.InitPanel;
import presentation.logui.LogPanel;
import presentation.promotionui.PromotionPanel;
import vo.UserType;
import vo.UserVO;

/**
 * MainWindow左侧的按钮栏，此处继承了JPanel
 * @author 钱美缘
 */
@SuppressWarnings("serial")
class LeftButtonPanel extends JPanel{
    private JPanel innerPanel = new JPanel(new GridLayout(11, 1, 5, 5));
    private ArrayList<JToggleButton> buttonList = new ArrayList<JToggleButton>();
    /**
     * 向Panel增加按钮
     * @param text 按钮显示的文字
     * @param listener 按钮绑定的监听器（由Listener类生成）
     */
    private void addButton(String text, ActionListener listener) {
    	JToggleButton button = new JToggleButton(text);
		button.setFont(new Font("等线",Font.BOLD,18));
		button.addActionListener(listener);
		button.addActionListener(e->updateButtons(button));
		if ("退出".equals(text)) button.addMouseListener(new InfoAdapter("退出系统"));
		else button.addMouseListener(new InfoAdapter("进入<" + text + ">界面"));
		buttonList.add(button);
    }
    /**
     * 构造方法：根据MainWindow的UserVO初始化按钮栏
     * @param mw 传入的MainWindow引用
     */
	public LeftButtonPanel(MainWindow mw) {
		UserVO user = MainWindow.getUser();
		UserType type = user.getType();
		innerPanel.setOpaque(false);
		
		class CloseListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mw.close();
			}	
		}
		
		ActionListener closeListener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mw.changePanel();
            }
		    
		};
		
		BillPanelHelper.user = user;
		BillPanelHelper.closeListener = closeListener;
		// 修改按钮处------------------------------
		if (type == UserType.KEEPER) {
			addButton("商品分类管理", e -> mw.changePanel(new CategoryDataPanel(new CategoryBL(), closeListener)));
			addButton("商品管理", e -> mw.changePanel(new DataPanel(new CommodityBL(), closeListener)));
			addButton("库存查看", e -> mw.changePanel(new InventoryDynamicPanel()));
			addButton("库存盘点", e -> mw.changePanel(new InventoryCheckPanel(closeListener)));
			addButton("报溢/报损", e -> mw.changePanel(BillPanelHelper.create("ChangeBill")));
		}
		else if (type == UserType.SALESMAN) {
			addButton("客户管理", e -> mw.changePanel(new DataPanel(new CustomerBL(), closeListener)));
			addButton("制定进货单", e -> mw.changePanel(BillPanelHelper.create("PurchaseBill")));
			addButton("制定进货退货单", e -> mw.changePanel(BillPanelHelper.create("PurchaseReturnBill")));
			addButton("制定销售单", e -> mw.changePanel(BillPanelHelper.create("SalesBill")));
			addButton("制定销售退货单", e -> mw.changePanel(BillPanelHelper.create("SalesReturnBill")));
		}
		else if (type == UserType.ACCOUNTANT) {
			addButton("账户管理", e -> mw.changePanel(new DataPanel(new AccountBL(), closeListener)));
			addButton("制定收付款单", e -> mw.changePanel(BillPanelHelper.create("ReceiptOrPaymentBill")));
			addButton("制定现金费用单", e -> mw.changePanel(BillPanelHelper.create("CashCostBill")));
			addButton("查看销售明细表", e -> mw.changePanel(new SalesDetailsPanel()));
			addButton("查看经营状况表", e -> mw.changePanel(new ViewBusinessSituationPanel(user, closeListener)));
			addButton("查看经营历程表", e -> mw.changePanel(new ViewBusinessHistoryPanel(user, closeListener)));
			addButton("期初建账", e -> mw.changePanel(new InitPanel(closeListener)));
			addButton("查看日志", e -> mw.changePanel(new LogPanel(closeListener)));		
		}
		else if (type == UserType.GM) {
			addButton("审批单据", e -> mw.changePanel(new BillExaminePanel(mw,closeListener)));
			addButton("制定促销策略", e -> mw.changePanel(new PromotionPanel(closeListener)));
			addButton("查看销售明细表", e -> mw.changePanel(new SalesDetailsPanel()));
			addButton("查看经营状况表", e -> mw.changePanel(new ViewBusinessSituationPanel(user, closeListener)));
			addButton("查看经营历程表", e -> mw.changePanel(new ViewBusinessHistoryPanel(user, closeListener)));
			addButton("查看日志", e -> mw.changePanel(new LogPanel(closeListener)));		
		}
		else if (type == UserType.ADMIN) {
			addButton("用户管理", e -> mw.changePanel(new DataPanel(new UserBL(), closeListener)));
			addButton("查看日志", e -> mw.changePanel(new LogPanel(closeListener)));		
		}
		addButton("退出", new CloseListener());
		//-----------------------------------------
		
		for(JToggleButton button : buttonList) innerPanel.add(button);
		double[][] size = {{TableLayout.FILL, 0.8, TableLayout.FILL},{TableLayout.FILL}};
		this.setLayout(new TableLayout(size));
		this.add(innerPanel, "1,0");
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public void setBackground() {
		ImageIcon image = new ImageIcon("resource/LeftButtonPanel.png");   
        Image img = image.getImage();  
        img = img.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);  
        image.setImage(img);
		this.add(new JLabel(image), "0, 0, 2, 0");
	}
	
	public void updateButtons(JToggleButton button) {
		for (JToggleButton b : buttonList) {
			if (b != button) b.setSelected(false);
		}
	}
}