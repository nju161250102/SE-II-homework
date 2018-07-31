package presentation.analysisui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.billblservice.BillOperationService;
import blservice.billblservice.BillSearchBLService;
import businesslogic.BillOperationBL;
import businesslogic.BillSearchBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.billui.BillViewer;
import presentation.component.DateChoosePanel;
import presentation.component.IdNamePanel;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;
import presentation.component.choosewindow.UserChooseWin;
import presentation.tools.ExcelExporter;
import presentation.tools.Timetools;
import vo.MyTableModel;
import vo.UserType;
import vo.UserVO;
import vo.billvo.BillVO;

/**
 * 查看经营历程表的面板,除了实现基本的单据查看，还实现了红冲以及红冲并复制的功能，红冲、红冲并复制均是直接生效，这一点由BillOperationService决定
 * @author 恽叶霄
 */
public class ViewBusinessHistoryPanel implements PanelInterface {
    
    private BillSearchBLService billSearchBl;
    private BillOperationService billOperationBl;
    
    private JPanel panel;
    private DateChoosePanel fromPanel, toPanel;
    private IdNamePanel operatorPanel, choosePanel;
    private JComboBox<String> choiceBox;
    private JTable table;
    
    private UserType type = UserType.KEEPER;

    public ViewBusinessHistoryPanel(UserVO user, ActionListener closeListener) {
        billSearchBl = new BillSearchBL();
        billOperationBl = new BillOperationBL();
        
        double[][] size = new double[][]{{TableLayout.FILL},{0.1,0.15,TableLayout.FILL}};
        TopButtonPanel buttonPanel = new TopButtonPanel();
        buttonPanel.addButton("搜索", new ImageIcon("resource/SearchData.png"), e->search());
		buttonPanel.addButton("导出", new ImageIcon("resource/Export.png"), e->ExcelExporter.export((MyTableModel)table.getModel()));
		if (user.getType() != UserType.GM) buttonPanel.addButton("红冲", new ImageIcon("resource/Offset.png"), e->offsetBill());
		if (user.getType() != UserType.GM) buttonPanel.addButton("红冲并复制", new ImageIcon("resource/OffsetCopy.png"), e->copyBill());
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		
		table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2) {
        			int index = table.getSelectedRow();
        	        new BillViewer(billOperationBl.getBillById(table.getValueAt(index, 0).toString()));
        		}
        	}
        });
        
        panel = new JPanel(new TableLayout(size));
        panel.add(buttonPanel.getPanel(), "0,0");
        panel.add(getNorthPanel(), "0,1");
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
    
    private JPanel getNorthPanel() {
        fromPanel = new DateChoosePanel("开始时间");
        toPanel = new DateChoosePanel("结束时间");
        choiceBox = new JComboBox<String>(new String[]{"报溢单","报损单","进货单","进货退货单","销售单","销售退货单","收款单","付款单","现金费用单"});
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
    private void search(){
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
        case "报溢单": table.setModel(billSearchBl.filterInventoryBills(from, to, chooseId, operatorId, true, BillVO.PASS));break;
        case "报损单": table.setModel(billSearchBl.filterInventoryBills(from, to, chooseId, operatorId, false, BillVO.PASS));break;
        case "进货单": table.setModel(billSearchBl.filterPurchaseBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "进货退货单" : table.setModel(billSearchBl.filterPurchaseReturnBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "销售单" : table.setModel(billSearchBl.filterSalesBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "销售退货单" : table.setModel(billSearchBl.filterSalesReturnBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "收款单" : table.setModel(billSearchBl.filterReceiptBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "付款单" : table.setModel(billSearchBl.filterPaymentBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        case "现金费用单" : table.setModel(billSearchBl.filterCashCostBills(from, to, chooseId, operatorId, BillVO.PASS));break;
        }
    }
    private void offsetBill(){
        int index = table.getSelectedRow();
        if(index < 0){
            new InfoWindow("请选择一张单据红冲@_@");
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "确认要红冲该单据吗？", "提示", JOptionPane.YES_NO_OPTION);
        if(response == 1) return;
        String id = table.getValueAt(index, 0).toString();
        if(billOperationBl.offsetBill(id)){
            JOptionPane.showMessageDialog(null, "红冲单据成功^_^");
            search();
        } else {
            JOptionPane.showMessageDialog(null, "红冲单据失败，请重试@_@");
        }
    }
    private void copyBill(){
        int index = table.getSelectedRow();
        if(index < 0){
            new InfoWindow("请选择一张单据进行操作@_@");
            return;
        }
        new CopyBillViewer(billOperationBl.getBillById(table.getValueAt(index, 0).toString()), true);
    }
}
