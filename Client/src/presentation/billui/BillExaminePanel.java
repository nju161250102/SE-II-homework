package presentation.billui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import blservice.billblservice.BillSearchBLService;
import businesslogic.BillExamineBL;
import businesslogic.BillOperationBL;
import businesslogic.BillSearchBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.CloseListener;
import presentation.component.DateChoosePanel;
import presentation.component.IdNamePanel;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;
import presentation.component.choosewindow.UserChooseWin;
import presentation.main.MainWindow;
import presentation.tools.ExcelExporter;
import presentation.tools.Timetools;
import vo.MyTableModel;
import vo.UserType;
import vo.UserVO;
import vo.billvo.BillVO;

/**
 * 单据审批面板
 * @author 郝睿
 */
public class BillExaminePanel implements PanelInterface {
    
    private BillSearchBLService billSearchBl;
    private BillOperationService billOperationBl;
    private BillExamineService billExamineBl;

	private JPanel panel;
    private DateChoosePanel fromPanel, toPanel;
    private IdNamePanel operatorPanel, choosePanel;
    private JComboBox<String> choiceBox;
    protected JTable table;
    
    private UserType type = UserType.KEEPER;

    public BillExaminePanel(MainWindow mainWindow, ActionListener closeListener) {
        billSearchBl = new BillSearchBL();
        billOperationBl = new BillOperationBL();

        double[][] size = new double[][]{{TableLayout.FILL},{0.1,0.15,TableLayout.FILL}};
    	TopButtonPanel buttonPanel = new TopButtonPanel();
        buttonPanel.addButton("搜索", new ImageIcon("resource/SearchData.png"), e->search());
    	buttonPanel.addButton("通过", new ImageIcon("resource/Examine.png"), e->pass());
    	buttonPanel.addButton("不通过", new ImageIcon("resource/NotPass.png"), e->notPass());
		buttonPanel.addButton("导出为Excel", new ImageIcon("resource/Save.png"), e -> ExcelExporter.export((MyTableModel)table.getModel()));
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), new CloseListener(mainWindow));
		
		table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2) {
        			int index = table.getSelectedRow();
        			String billId = table.getValueAt(index, 0).toString();
        			new BillViewer(billOperationBl.getBillById(billId));
        		}
        	}
        });
        
        panel = new JPanel(new TableLayout(size));
		panel.add(buttonPanel.getPanel(), "0,0");
        panel.add(getNorthPanel(closeListener), "0,1");
        panel.add(sp, "0,2");
 
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    private JPanel getNorthPanel(ActionListener closeListener) {
        fromPanel = new DateChoosePanel("开始时间");
        toPanel = new DateChoosePanel("结束时间");
        choiceBox = new JComboBox<String>(new String[]{"全部","报溢单","报损单","进货单","进货退货单","销售单","销售退货单","收款单","付款单","现金费用单"});
        choiceBox.addItemListener(e -> itemChanged(e));
        operatorPanel = new IdNamePanel("操作员", 7, 7);
        operatorPanel.addMouseListener(chooseOperator());
        choosePanel = new IdNamePanel("商品", 7, 7);


        
        double[][] size = {
                {20.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0},
                {10.0, -2.0, 10.0, -2.0, -1.0}
        };
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(fromPanel, "1 1");
        panel.add(toPanel, "1 3");
        panel.add(choiceBox, "3 1");
        panel.add(operatorPanel, "3 3");
        panel.add(choosePanel, "5 1");
        return panel;
    }

    private void itemChanged(ItemEvent e){
        if(e.getStateChange() == ItemEvent.DESELECTED) return;
        switch ((String)choiceBox.getSelectedItem()) {
        case "报溢单": choosePanel.setLabelText("商品");type = UserType.KEEPER;break;
        case "报损单": choosePanel.setLabelText("商品");type = UserType.KEEPER;break;
        case "进货单": choosePanel.setLabelText("客户");type = UserType.SALESMAN;break;
        case "进货退货单" : choosePanel.setLabelText("客户");type = UserType.SALESMAN;break;
        case "销售单" : choosePanel.setLabelText("客户");type = UserType.SALESMAN;break;
        case "销售退货单" : choosePanel.setLabelText("客户");type = UserType.SALESMAN;break;
        case "收款单" : choosePanel.setLabelText("账户");type = UserType.ACCOUNTANT;break;
        case "付款单" : choosePanel.setLabelText("账户");type = UserType.ACCOUNTANT;break;
        case "现金费用单" : choosePanel.setLabelText("账户");type = UserType.ACCOUNTANT;break;
        }
        operatorPanel.setId(""); 
        operatorPanel.setItsName("");
        choosePanel.setId("");
        choosePanel.setItsName("");
    }

    private MouseListener chooseOperator(){
    	 return new MouseAdapter(){
             @Override
             public void mouseClicked(MouseEvent e){
                 UserVO operator = new UserChooseWin().getUser();
                 if(operator == null) return;
                 if(type == operator.getType()){
                     operatorPanel.setId(operator.getId());
                     operatorPanel.setItsName(operator.getName());
                 } else {
                     new InfoWindow("请选择正确的操作员");
                 }
             }
         };
    }

    protected void search(){
        String from = fromPanel.getText(), to = toPanel.getText();
        if(!Timetools.checkDate(from, to)){
            new InfoWindow("请输入正确的日期段@_@");
            return;
        }
        String operatorId = operatorPanel.getId();
        operatorId = operatorId.length() == 0 ? null : operatorId;
        String chooseId = choosePanel.getId();
        chooseId = chooseId.length() == 0 ? null : chooseId;
        switch ((String)choiceBox.getSelectedItem()) {
        case "报溢单": table.setModel(billSearchBl.filterInventoryBills(from, to, chooseId, operatorId, true, BillVO.COMMITED));break;
        case "报损单": table.setModel(billSearchBl.filterInventoryBills(from, to, chooseId, operatorId, false, BillVO.COMMITED));break;
        case "进货单": table.setModel(billSearchBl.filterPurchaseBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "进货退货单" : table.setModel(billSearchBl.filterPurchaseReturnBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "销售单" : table.setModel(billSearchBl.filterSalesBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "销售退货单" : table.setModel(billSearchBl.filterSalesReturnBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "收款单" : table.setModel(billSearchBl.filterReceiptBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "付款单" : table.setModel(billSearchBl.filterPaymentBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        case "现金费用单" : table.setModel(billSearchBl.filterCashCostBills(from, to, chooseId, operatorId, BillVO.COMMITED));break;
        default:table.setModel(billSearchBl.filterBills(from, to));break;
        }
    }
    
    private void pass() {
    	boolean flag = true;
    	billExamineBl = new BillExamineBL();
        int index[] = table.getSelectedRows();
        
    	if(index.length == 0){
            new InfoWindow("请选择一张单据审批@_@");
            return;
        }else if (index.length == 1) {
            String id = table.getValueAt(index[0], 0).toString();
        	if(billExamineBl.examineBill(id)){
                new InfoWindow("通过单据审批！");
            }else {
            	String type = id.split("-")[0];
            	switch (type) {
            	case "XJFYD": JOptionPane.showMessageDialog(null, "审核未通过：账户余额不足@_@"); break;
            	case "BYD": JOptionPane.showMessageDialog(null, "操作失败，请重试@_@");
            	case "BSD": JOptionPane.showMessageDialog(null, "审核未通过：报损数量超过库存数量@_@"); break;
            	case "FKD": JOptionPane.showMessageDialog(null, "审核未通过：账户余额不足@_@"); break;
            	case "JHD": JOptionPane.showMessageDialog(null, "审核未通过：应收额度不足@_@"); break;
            	case "JHTHD": JOptionPane.showMessageDialog(null, "审核未通过：商品退货数量超过库存数量@_@"); break;
            	case "XSD": JOptionPane.showMessageDialog(null, "审核未通过：商品销售数量超过库存数量@_@"); break;
            	
            	default: JOptionPane.showMessageDialog(null, "操作失败，请重试@_@");
            	}
            }
        }else {
        	for (int i = 0; i < index.length; i++) {
                String id = table.getValueAt(index[i], 0).toString();
                if(billExamineBl.examineBill(id)){
                	flag = true;
                }else {
                	flag = false;
                    JOptionPane.showMessageDialog(null, "单据" + table.getValueAt(index[i], 0) + "操作失败，请重试@_@");
                    JOptionPane.showMessageDialog(null, "批量审批终止@_@");
                    break;
                }	
            }	
        	if (flag) {
                JOptionPane.showMessageDialog(null, "批量审批成功^_^");
                Arrays.sort(index);
                for (int i = index.length - 1; i >= 0; i--) 
                	((MyTableModel) table.getModel()).removeRow(index[i]);
        	}
        }
    	
    }
    
    private void notPass() {
    	boolean flag = true;
    	billExamineBl = new BillExamineBL();
        int index[] = table.getSelectedRows();
        
    	if(index.length == 0){
            new InfoWindow("请选择一张单据审批@_@");
            return;
        }else if (index.length == 1) {
            String id = table.getValueAt(index[0], 0).toString();
        	if(billExamineBl.notPassBill(id)){
                JOptionPane.showMessageDialog(null, "单据审批不通过成功^_^");
            }else {
                JOptionPane.showMessageDialog(null, "操作失败，请重试@_@");
            }
        }else {
        	for (int i = 0; i < index.length; i++) {
                String id = table.getValueAt(index[i], 0).toString();
                if(billExamineBl.notPassBill(id)){
                	flag = true;
                }else {
                	flag = false;
                    JOptionPane.showMessageDialog(null, "单据" + table.getValueAt(index[i], 0) + "操作失败，请重试@_@");
                    JOptionPane.showMessageDialog(null, "批量不通过终止@_@");
                    break;
                }	
            }	
        	if (flag) {
                JOptionPane.showMessageDialog(null, "批量不通过成功^_^");
        	}
        }
    }
}
