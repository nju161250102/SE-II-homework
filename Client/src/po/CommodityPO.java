package po;

import java.io.Serializable;

/**
 * Create time: 2017/10/26<br>
 * Last update time: 2017/10/26<br>
 * <br>商品相关的PO类
 * @author 恽叶霄
 */
public class CommodityPO implements Serializable {
    private static final long serialVersionUID = 6222451958770566420L;
    private String id, name, type, store, categoryId;
    private long amount, alarmNum;
    private double inPrice, salePrice, recentInPrice, recentSalePrice;
	private boolean isExist;
    
    public CommodityPO(String id, String name, String type, String store, String categoryId
        , long amount, long alarmNum, double inPrice, double salePrice
        , double recentInPrice, double recentSalePrice, boolean isExist) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.store = store;
        this.categoryId = categoryId;
        this.amount = amount;
        this.alarmNum = alarmNum;
        this.inPrice = inPrice;
        this.salePrice = salePrice;
        this.recentInPrice = recentInPrice;
        this.recentSalePrice = recentSalePrice;
        this.isExist = isExist;
    }

    public String getId() {return id;}
    
    public String getName() {return name;}

    public String getType() { return type;}
    
    public String getStore() {return store;}

    public String getCategoryId() { return categoryId;}

    public long getAmount() {return amount;}

    public long getAlarmNum() {return alarmNum;}

    public double getInPrice() {return inPrice;}

    public double getSalePrice() {return salePrice;}

    public double getRecentInPrice() {return recentInPrice;}

    public double getRecentSalePrice() {return recentSalePrice;}
    
    public boolean getExistFlag() {return this.isExist;}
    
    public void setRecentInPrice(double price) {recentInPrice = price;}
    public void setRecentSalePrice(double price) {recentSalePrice = price;}
    
    public boolean setAmount(long n) {
    	if (n < 0) return false;
    	amount = n;
    	return true;
    }
}