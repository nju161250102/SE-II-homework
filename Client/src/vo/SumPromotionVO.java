package vo;

import java.util.ArrayList;

import businesslogic.GiftItemTools;
import po.SumPromotionPO;
import po.billpo.GiftItem;

/**
 * 针对消费总额制定的促销策略
 * 
 * @author 恽叶霄
 */
public class SumPromotionVO extends PromotionVO {
    
    private double startPrice, endPrice;
    private double coupon;
    private MyTableModel gifts;

    public SumPromotionVO(String id, String from, String to, double startPrice, 
        double endPrice, double coupon, MyTableModel gifts) {
        super(id, from, to, gifts);
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.coupon = coupon;
        this.gifts = gifts;
        this.reduction = 0.0;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public double getCoupon() {
        return coupon;
    }

    public MyTableModel getGifts() {
        return gifts;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        if(coupon > 0){
            buffer.append("；代金券：");
            buffer.append(coupon);
        }
        int rows = gifts.getRowCount();
        if(rows > 0){
            buffer.append("；赠品：");
            for(int i = 0; i < rows; i++){
                buffer.append(gifts.getValueAt(i, 1) + "*" + gifts.getValueAt(i, 4));
                buffer.append(',');
            }
            buffer.delete(buffer.length() - 1, buffer.length());
        }
        return buffer.toString();
    }

    @Override
    public SumPromotionPO toPO() {
        ArrayList<GiftItem> gifts = GiftItemTools.toArrayList(this.gifts);
        return new SumPromotionPO(this.getId(), this.getFromDate(), this.getToDate(), 
            startPrice, endPrice, coupon, gifts);
    }

}
