package po;

import java.util.ArrayList;

/**
 * 商品的组合促销策略
 * 
 * @author 恽叶霄
 */
public class GroupDiscountPO extends PromotionPO {
    
    private ArrayList<String> group;
    private double reduction;
    
    public GroupDiscountPO(){
        this(null, null, null, null, 0.0);
    }

    public GroupDiscountPO(String id, String fromDate, String toDate, 
        ArrayList<String> group, double reduction) {
        super(id, fromDate, toDate);
        this.group = group;
        this.reduction = reduction;
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> group) {
        this.group = group;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

}
