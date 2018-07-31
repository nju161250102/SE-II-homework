package presentation.billui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.TopButtonPanel;

/**
 * 两个构造方法分别对应新建单据和修改单据，其中也包含了两个不同的抽象方法初始化BillPanel(使用getBillPanel得到)<br/>
 * 三个获取监听器方法在子类中表现为不同的业务逻辑处理的监听器<br/>
 * 子类中将对应单据的BL作为私有成员变量，如果需要测试请自己写桩stub<br/>
 * 子类可能需要将单据的VO作为私有成员变量，尤其是带有表格的单据<br/>
 * 子类的构造方法中使用相应的BillVO的子类
 * @author 钱美缘
 *
 */
public class BillPanel implements PanelInterface {

	private JPanel panel;
	private TopButtonPanel buttonPanel;
	private BillPanelInterface billPanel;
	/**
	 * 新建一张单据时的构造方法
	 * @param closeListener MainWindow的关闭Panel监听器
	 */
	public BillPanel(ActionListener closeListener, BillPanelInterface billPanel) {
		this.billPanel = billPanel;
		buttonPanel = new TopButtonPanel();
		double[][] size = {{TableLayout.FILL},{0.1,TableLayout.FILL}};
		panel = new JPanel(new TableLayout(size));
		
		buttonPanel.addButton("新建", new ImageIcon("resource/New.png"), e->billPanel.newAction());
		buttonPanel.addButton("保存", new ImageIcon("resource/Save.png"), e->billPanel.saveAction());
		buttonPanel.addButton("提交", new ImageIcon("resource/Commit.png"), e->billPanel.commitAction());
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		
		panel.add(buttonPanel.getPanel(), "0 0");
		panel.add((JPanel)billPanel, "0 1");
	}
	/**
	 * 设置不可修改的方法
	 * @param b
	 */
	public void setEditable(boolean b) {
		buttonPanel.setEnable(b);
		billPanel.setEditable(b);
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
