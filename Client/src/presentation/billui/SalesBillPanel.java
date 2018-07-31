package presentation.billui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import blservice.billblservice.SalesBillBLService;
import blservice.infoservice.GetCustomerInterface;
import blservice.infoservice.GetPromotionInterface;
import businesslogic.CustomerBL;
import businesslogic.PromotionBL;
import businesslogic.SalesBillBL;
import layout.TableLayout;
import presentation.tools.DoubleField;
import presentation.tools.Timetools;
import vo.MyTableModel;
import vo.PromotionVO;
import vo.UserVO;
import vo.billvo.BillVO;
import vo.billvo.SalesBillVO;

/**
 * 销售单的面板<br>
 * 上一级抽象父类中的sumField为这里的折扣前总额的TextField
 * @author 恽叶霄
 */
public class SalesBillPanel extends CommonSaleBillPanel {
	
	private SalesBillBLService saleBillBL = new SalesBillBL();
	private GetCustomerInterface customerInfo = new CustomerBL();
	private DoubleField discountField, couponField, afterDiscountField;
	private JButton sumButton;
	private JTextArea promotionInfoArea;
	private PromotionVO promotion;
	
	public SalesBillPanel(UserVO user) {
		super(user);

		billIdField.setText(saleBillBL.getNewId());
		operatorField.setText(user.getName());
	}

	public SalesBillPanel(UserVO user, SalesBillVO bill) {
		super(user, bill);
		discountField.setValue(bill.getDiscount());
		couponField.setValue(bill.getCoupon());
		sumField.setValue(bill.getBeforeDiscount());
		afterDiscountField.setValue(bill.getSum());
		GetPromotionInterface promotionInfo = new PromotionBL();
		if(bill.getPromotionId() != null){
		    promotion = promotionInfo.findById(bill.getPromotionId());
		    promotionInfoArea.setText(promotion.toString());
		}
	}
	
	public void setEditable(boolean b) {
		super.setEditable(b);
		discountField.setEditable(b);
	    couponField.setEditable(b);
	    sumButton.setEnabled(b);
	    afterDiscountField.setEnabled(b);
	}
	@Override
	protected boolean isCorrectable() {
	    if(!super.isCorrectable()) return false;
	    try{
	        double discount = Double.parseDouble(discountField.getText());
	        if(user.getRank() == 0 && discount > 1000)
	            return false;
	        if(user.getRank() == 1 && discount > 5000)
	            return false;
	        Double.parseDouble(couponField.getText());
	        return true;
	    }catch(NumberFormatException e){
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@Override
	protected void clear(){
	    super.clear();
	    discountField.setText("");
	    discountField.setEditable(true);
	    afterDiscountField.setText("");
	    couponField.setText("");
	    couponField.setEditable(true);
	    promotionInfoArea.setText("");
	}

	@Override
	protected void sumUp(){
	    super.sumUp();

	    int rank = customerInfo.getCustomer(customerIdField.getText()).getRank();
	    MyTableModel goods = (MyTableModel)goodsListTable.getModel();
	    double sum = sumField.getValue();
	    promotion = saleBillBL.getBestPromotion(rank, goods, sum);
	    if(promotion != null)promotionInfoArea.setText(promotion.toString());

	    double before = 0, after = 0, reduction = promotion == null ? 0 : promotion.getReduction();
        before = sumField.getValue();
        after = before - discountField.getValue() - couponField.getValue() - reduction;
	    afterDiscountField.setValue(after);
	}

    @Override
    protected String getObjectType() {
        return "销售商";
    }

    @Override
    protected String getTableTitle() {
        return "销售列表";
    }

	@Override
	protected void initSouth(){
	    double[][] size = {{-1.0}, {-1.0, -1.0}};
	    JPanel southPanel = new JPanel(new TableLayout(size));
	    southPanel.add(getPromotionPanel(), "0 0");
	    southPanel.add(getTailPanel(), "0 1");
	    this.add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * 获得单据VO
	 * @return
	 */
	public SalesBillVO getBill(int state) {
		if (isCorrectable()) {
		    String id = bill == null ? saleBillBL.getNewId().split("-")[2] : getId();
		    String operater = user.getId()
		         , customerId = customerIdField.getText()
		         , remark = remarkField.getText();
		    MyTableModel model = (MyTableModel)goodsListTable.getModel();
		    double beforeDiscount = Double.parseDouble(sumField.getText())
		         , sum = Double.parseDouble(afterDiscountField.getText())
		         , discount = Double.parseDouble(discountField.getText())
		         , coupon = Double.parseDouble(couponField.getText());
		    String promotionId = promotion == null ? null : promotion.getId();
		    return new SalesBillVO(
	    		bill == null ? Timetools.getDate() : bill.getDate(),
				bill == null ? Timetools.getTime() : bill.getTime(), id, operater, state, 
		        customerId, model, remark, beforeDiscount, discount, coupon, sum, promotionId);
		}
		return null;
	}

	private JPanel getPromotionPanel(){
	    double[][] size = {{20.0, -1.0, -1.0}, {20.0, 15.0, 10.0, -1.0}};
	    JPanel promotionPanel = new JPanel(new TableLayout(size));
	    promotionPanel.add(new JLabel("促销信息"), "1 1");
	    promotionInfoArea = new JTextArea(2, 20);
	    promotionInfoArea.setEditable(false);
	    promotionInfoArea.setLineWrap(true);
	    promotionPanel.add(promotionInfoArea, "1 3");
	    return promotionPanel;
	}
	
	private JPanel getTailPanel(){
	    sumButton = new JButton("合计");
	    sumButton.addActionListener(e -> sumUp());

		sumField = new DoubleField(10);
		afterDiscountField = new DoubleField(10);
		sumField.setEditable(false);
		afterDiscountField.setEditable(false);
		discountField = new DoubleField(10);
		couponField = new DoubleField(10);
		remarkField = new JTextField(10);
		
		double size[][] = {
				{20,60,5,80,15,40,5,80,15,60,5,0.7,TableLayout.FILL},
				{15,25,10,25,TableLayout.FILL}
		};
		JPanel tailPanel = new JPanel(new TableLayout(size));
		tailPanel.add(new JLabel("    折让"),"5,1");
		tailPanel.add(discountField,"7,1");
		tailPanel.add(new JLabel("折让前总额"),"1,1");
		tailPanel.add(sumField,"3,1");
		tailPanel.add(sumButton, "9,1");
		tailPanel.add(new JLabel("折让后总额"),"1,3");
		tailPanel.add(afterDiscountField,"3,3");
		tailPanel.add(new JLabel("代金券"),"5,3");
		tailPanel.add(couponField,"7,3");
		tailPanel.add(new JLabel("      备注"),"9,3");
		tailPanel.add(remarkField, "11 3");
		return tailPanel;
	}

	@Override
	protected void handleChooseCustomer() {
		handleChooseCustomer(false);
	}

	@Override
	public void newAction() {
		int response = JOptionPane.showConfirmDialog(null, "确认要新建一张销售单吗？", "提示", JOptionPane.YES_NO_OPTION);
        if(response == 1)return;
        clear();
        billIdField.setText(saleBillBL.getNewId());
        operatorField.setText(user.getName());
	}

	@Override
	public void saveAction() {
		SalesBillVO bill = getBill(BillVO.SAVED);
        if (bill != null && saleBillBL.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已保存。");
	}

	@Override
	public void commitAction() {
		SalesBillVO bill = getBill(BillVO.COMMITED);
        if (bill != null && saleBillBL.saveBill(bill)) JOptionPane.showMessageDialog(null, "单据已提交。");
	}

}