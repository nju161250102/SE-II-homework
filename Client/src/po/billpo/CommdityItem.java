package po.billpo;

import java.io.Serializable;

public class CommdityItem implements Serializable{

	private String commodityId;
	private double price;
	private int num;
	private String remark;
	/**
	 * 列表项必须存储当时的单价
	 * @param commodityId 商品的id
	 * @param price 商品的单价
	 * @param num 商品的购买数量
	 * @param remark 备注
	 */
	public CommdityItem(String commodityId, double price, int num, String remark) {
		this.commodityId = commodityId;
		this.price = price;
		this.num = num;
		this.remark = remark;
	}
	
	public String getCommodityId() {
		return this.commodityId;
	}

	public double getPrice() {
		return this.price;
	}

	public int getNum() {
		return this.num;
	}

	public String getRemark() {
		return this.remark;
	}

}
