package presentation.billui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import blservice.billblservice.PurchaseReturnBillBLService;
import businesslogic.PurchaseReturnBillBL;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.PurchaseBillChooseWin;
import presentation.tools.Timetools;
import vo.MyTableModel;
import vo.UserVO;
import vo.billvo.BillVO;
import vo.billvo.PurchaseBillVO;
import vo.billvo.PurchaseReturnBillVO;

/**
 * 进货退货单面板
 * @author 恽叶霄
 */
public class PurchaseReturnBillPanel extends CommonSaleBillPanel {
    
    private PurchaseReturnBillBLService purchaseReturnBl = new PurchaseReturnBillBL();
    private JTextField originalPBIdField;
    private JButton purchaseBillChooseButton;
    private PurchaseBillVO originalPB;

    public PurchaseReturnBillPanel(UserVO user) {
        super(user);
        this.billIdField.setText(purchaseReturnBl.getNewId());
        this.operatorField.setText(user.getName());
    }

    public PurchaseReturnBillPanel(UserVO user, PurchaseReturnBillVO saleBill) {
        super(user, saleBill);
    }

    @Override
    protected String getObjectType() {
        return "供应商";
    }

    @Override
    protected String getTableTitle() {
        return "出库商品列表";
    }

    @Override
    protected JPanel getCustomerPanel(){
        customerIdField = new JTextField(10);
        customerIdField.setEditable(false);
        customerNameField = new JTextField(10);
        customerNameField.setEditable(false);
        originalPBIdField = new JTextField(15);
        originalPBIdField.setEditable(false);
        purchaseBillChooseButton = new JButton("选择源进货单");
        purchaseBillChooseButton.addActionListener(e -> handleChoosePb());
        customerChooseButton = new JButton("选择");
        customerChooseButton.addActionListener(e -> handleChooseCustomer());
        
        double[][] size = {
                {20.0, -2.0, 10.0, -2.0, -2.0, -2.0, 20.0
                    , -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0},
                {8, -2.0, -1.0}
        };
        JPanel customerPanel = new JPanel(new TableLayout(size));
        customerPanel.add(new JLabel(getObjectType()), "1 1");
		customerPanel.add(customerIdField,"3,1");
		customerPanel.add(new JLabel("--"),"4,1");
		customerPanel.add(customerNameField,"5,1");
		customerPanel.add(customerChooseButton,"7,1");
		//customerPanel.add(new JLabel("源进货单"), "9 1");
		//customerPanel.add(originalPBIdField, "11 1");
		//customerPanel.add(purchaseBillChooseButton, "13 1");

        return customerPanel;
    }

    public PurchaseReturnBillVO getBill(int state){
        if(!isCorrectable()) return null;
        String id = bill == null ? purchaseReturnBl.getNewId().split("-")[2] : getId()
        	 , operater = user.getId()
             , customerId = customerIdField.getText()
             , remark = remarkField.getText();
        MyTableModel model = (MyTableModel)goodsListTable.getModel();
        double sum = Double.parseDouble(sumField.getText());
        return new PurchaseReturnBillVO(
        		bill == null ? Timetools.getDate() : bill.getDate(),
        		bill == null ? Timetools.getTime() : bill.getTime(), 
        		id, operater, state, customerId, model, remark, sum);
    }

    private void handleChoosePb(){
        String customerId = customerIdField.getText();
        if(customerId.length() == 0){
            new InfoWindow("请先选择进货商");
            return;
        }
        originalPB = new PurchaseBillChooseWin(customerId).getPurchaseBill();
        if(originalPB == null) return;
        originalPBIdField.setText(originalPB.getAllId());
    }

	@Override
	protected void handleChooseCustomer() {
		handleChooseCustomer(true);
	}
	
	public void setEditable(boolean b) {
		super.setEditable(b);
		purchaseBillChooseButton.setEnabled(b);
		customerChooseButton.setEnabled(b);
	}

	@Override
	public void newAction() {
		int response = JOptionPane.showConfirmDialog(null, "确认要新建一张进货退货单吗？", "提示", JOptionPane.YES_NO_OPTION);
        if(response == 1) return;
        clear();
        billIdField.setText(purchaseReturnBl.getNewId());
        operatorField.setText(user.getName());
	}

	@Override
	public void saveAction() {
		PurchaseReturnBillVO bill = getBill(BillVO.SAVED);
        if (bill != null && purchaseReturnBl.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已保存。");
	}

	@Override
	public void commitAction() {
		PurchaseReturnBillVO bill = getBill(BillVO.COMMITED);
        if (bill != null && purchaseReturnBl.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已提交。");
	}
}
