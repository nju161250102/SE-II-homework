package vo;

import po.PromotionPO;

/**
 * 促销策略VO的抽象父类
 * 
 * @author 恽叶霄
 */
public abstract class PromotionVO {
    
    private String id;
    private String from, to;
    /** 一个促销策略所产生的赠品 */
    private MyTableModel gifts;
    /** 一个促销策略所产生的降价总额 */
    protected double reduction;

    public PromotionVO(String id, String from, String to, MyTableModel gifts) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.gifts = gifts;
    }
    
    @Override
    public String toString(){
        return "编号：" + id;
    }
    
    abstract public PromotionPO toPO();
    
    public String getId() {
        return id;
    }

    public String getFromDate() {
        return from;
    }

    public String getToDate() {
        return to;
    }

    public MyTableModel getGifts() {
        return gifts;
    }

    public double getReduction() {
        return reduction;
    }

    /**
     * @param num 组合降价专用的参数，表示客户购买该组合的数量。该参数对其他促销策略无效。
     */
    public void setReduction(int num) {}

}
