package presentation.analysisui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import blservice.ViewBusinessSituationBLService;
import businesslogic.ViewBusinessSituationBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.DateChoosePanel;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;
import presentation.tools.ExcelExporter;
import presentation.tools.Timetools;
import vo.MyTableModel;
import vo.UserVO;

public class ViewBusinessSituationPanel implements PanelInterface {
	
	private ViewBusinessSituationBLService viewBusinessSituationBl;
    private JPanel panel;
    private DateChoosePanel fromPanel, toPanel;
	private JTable incomeTable, expenseTable;
	private JTextField sumField;
	JScrollPane incomePane, expensePane;
	
    public ViewBusinessSituationPanel(UserVO user, ActionListener closeListener) {
        
        double[][] size = new double[][]{{TableLayout.FILL},{0.1,0.15,0.3,0.3,TableLayout.FILL}};
        TopButtonPanel buttonPanel = new TopButtonPanel();
        buttonPanel.addButton("搜索", new ImageIcon("resource/SearchData.png"), e->search());
		buttonPanel.addButton("导出收入表", new ImageIcon("resource/Export.png"), e->ExcelExporter.export((MyTableModel)incomeTable.getModel()));
		buttonPanel.addButton("导出支出表", new ImageIcon("resource/Export.png"), e->ExcelExporter.export((MyTableModel)expenseTable.getModel()));
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		
        panel = new JPanel(new TableLayout(size));
        panel.add(buttonPanel.getPanel(), "0,0");
        panel.add(getNorthPanel(), "0,1");
        panel.add(getCentrePanel(), "0,2");
        panel.add(getSouthPanel(), "0,3");
        panel.add(getButtonPanel(), "0,4");
}

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    private JPanel getNorthPanel() {
        fromPanel = new DateChoosePanel("开始时间");
        toPanel = new DateChoosePanel("结束时间");
        
        double[][] size = {
                {20.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0},
                {10.0, -2.0, 10.0, -2.0, -1.0}
        };
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(fromPanel, "1 1");
        panel.add(toPanel, "1 3");
        return panel;
    }
    
    private JPanel getCentrePanel() {
    	String[] incomeAttributes={"总收入", "销售收入", "商品报溢收入", "成本调价收入", "进货退货差价收入", "代金券"};
		incomeTable = new JTable(new MyTableModel(null, incomeAttributes));
		incomeTable.getTableHeader().setReorderingAllowed(false);
		incomePane = new JScrollPane(incomeTable);
		
		double size[][]={
				{20,0.8,20,0.2},
				{10,25,5,TableLayout.FILL}
		};
        JPanel panel = new JPanel(new TableLayout(size));

		panel.add(new JLabel("收入类"),"1,1");
		panel.add(incomePane, "1,3");
    	return panel;
    }
    
    private JPanel getSouthPanel() {
    	String[] expenseAttributes={"总支出", "销售成本", "商品报损支出", "商品赠出支出"};
		expenseTable = new JTable(new MyTableModel(null, expenseAttributes));
		expenseTable.getTableHeader().setReorderingAllowed(false);
		expensePane = new JScrollPane(expenseTable);
		
		double size[][]={
				{20,0.8,20,0.2},
				{10,25,5,TableLayout.FILL,15,15}
		};
        JPanel panel = new JPanel(new TableLayout(size));
		panel.add(new JLabel("支出类"),"1,1");
		panel.add(expensePane, "1,3");
       	return panel;
    }
    
    private JPanel getButtonPanel() {
		sumField = new JTextField(40);
		sumField.setEditable(false);
    	double size[][]={
				{20,40,1,70,0.8,0.2},
				{10,25,5,TableLayout.FILL,15,15}
		};
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(new JLabel("利润:"), "1,1");
        panel.add(sumField, "3,1");
        return panel;
    }

    private void search() {
    	viewBusinessSituationBl = new ViewBusinessSituationBL();
    	String from = fromPanel.getText(), to = toPanel.getText();
        if(!Timetools.checkDate(from, to)){
            new InfoWindow("请输入正确的日期段@_@");
            return;
        }
        incomeTable.setModel(viewBusinessSituationBl.fillIncomeTable(from, to));
        expenseTable.setModel(viewBusinessSituationBl.fillExpenseTable(from, to));
		sumField.setText(Double.toString(viewBusinessSituationBl.getProfit(from, to)));
    }
}