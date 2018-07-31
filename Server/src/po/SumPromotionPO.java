package po;

import java.io.Serializable;
import java.util.ArrayList;

import po.billpo.GiftItem;

/**
 * 针对总价所在区间所设置的促销策略
 * 
 * @author 恽叶霄
 */
public class SumPromotionPO extends PromotionPO implements Serializable{
    
    /** 享受该促销策略的消费总额区间的端点值 */
    private double startPrice, endPrice;
    private double coupon;
    private ArrayList<GiftItem> gifts;
    
    public SumPromotionPO(){
        this(null, null, null, 0.0, 0.0, 0.0, null);
    }

    public SumPromotionPO(String id, String fromDate, String toDate, double startPrice, double endPrice, 
        double coupon, ArrayList<GiftItem> gifts) {
        super(id, fromDate, toDate);
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.coupon = coupon;
        this.gifts = gifts;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    public double getCoupon() {
        return coupon;
    }

    public void setCoupon(double coupon) {
        this.coupon = coupon;
    }

    public ArrayList<GiftItem> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<GiftItem> gifts) {
        this.gifts = gifts;
    }

}
