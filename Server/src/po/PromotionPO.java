package po;

import java.io.Serializable;

/**
 * 促销策略PO的抽象父类
 * @author 恽叶霄
 */
public abstract class PromotionPO implements Serializable{
    
    private String id;
    /** 该促销策略的起讫日期 */
    private String fromDate, toDate;
    private boolean isExist;
    
    protected PromotionPO(){
        this(null, null, null);
    }
    
    protected PromotionPO(String id, String fromDate, String toDate){
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.isExist = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }

}
