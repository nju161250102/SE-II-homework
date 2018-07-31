package vo.billvo;

import vo.MyTableModel;

public class SalesBillVO extends MarketBillVO {

	private double beforeDiscount, discount, coupon;
	private String promotionId;
	
	public SalesBillVO(String date, String time, String id, String operator, int state, String customerId
	    , MyTableModel model, String remark, double beforeDiscount, double discount
	    , double coupon, double sum, String promotionId) {
		super(date, time, id, operator, state, customerId, model, remark, sum);
		this.beforeDiscount = beforeDiscount;
		this.discount = discount;
		this.coupon = coupon;
		this.promotionId = promotionId;
	}
	
    @Override
    protected String getPrefix() {
        return "XSD";
    }

    public double getBeforeDiscount() {
        return beforeDiscount;
    }

    public double getDiscount() {
        return discount;
    }
    
    public double getCoupon(){
        return coupon;
    }
    
    public String getPromotionId(){
        return promotionId;
    }

}
