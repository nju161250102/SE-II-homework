package po.billpo;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Create time: 2017/12/01<br>
 * Last update time: 2017/12/01<br>
 * <br>销售单相关的PO类
 * @author 万嘉雯
 */

public class SalesBillPO extends BillPO implements Serializable{

	 private static final long serialVersionUID = 2021998686222663097L;
	 private String customerId, salesManName, remark, promotionId;
	 private double beforeDiscount, discount, coupon, afterDiscount;
	 private ArrayList<SalesItemsPO> salesBillItems;
	 
	 public SalesBillPO(){};
	
	 public SalesBillPO(String date, String time, String id, String operatorId, int state, String customerId,
			String salesManName, String remark, String promotionId, double beforeDiscount, double discount,
			double coupon, double afterDiscount, ArrayList<SalesItemsPO> salesBillItems) {
		super(date, time, id, operatorId, state);
		this.customerId = customerId;
		this.salesManName = salesManName;
		this.remark = remark;
		this.promotionId = promotionId;
		this.beforeDiscount = beforeDiscount;
		this.discount = discount;
		this.coupon = coupon;
		this.afterDiscount = afterDiscount;
		this.salesBillItems = salesBillItems;
	}

	  
	 public void setCustomerId(String customerId){
		 this.customerId=customerId;
	 }
	 
	 public String getCustomerId(){
		 return customerId;
	 }

	 public void setSalesManName(String name){
		 this.salesManName=name;
	 }
	 
	 public String getSalesManName(){
		 return salesManName;
	 }


	 public void setRemark(String remark){
		 this.remark=remark;
	 }
	 
	 public String getRemark(){
		 return remark;
	 }
	 
	 public void setPromotionId(String id){
		 this.promotionId=id;
	 };
	 
	 public String getPromotionId(){
		 return promotionId;
	 }
	 
	 
	 public void setBeforeDiscount(double money){
		 this.beforeDiscount=money;
	 }
	 
	 public double getBeforeDiscount(){
		 return beforeDiscount;
	 }
	 
	 public void setDiscount(double dis){
		 this.discount=dis;
	 }
	 
	 public double getDiscount(){
		 return discount;
	 }
	 
	 public void setAfterDiscount(double money){
		 this.afterDiscount=money;
	 }
	 
	 public double getAfterDiscount(){
		 return afterDiscount;
	 }
	 
	 public void setCoupon(double cou){
		 this.coupon=cou;
	 }
	 
	 public double getCoupon(){
		 return coupon;
	 }
	 
	 public void setSalesBillItems(ArrayList<SalesItemsPO> sbis){
		 this.salesBillItems=sbis;
	 }
	 
	 public ArrayList<SalesItemsPO> getSalesBillItems(){
		 return salesBillItems;
	 }

	@Override
	public String getAllId() {
		return "XSD-" + this.getDate() + "-" + this.getId();
	}

}
