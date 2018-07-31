package po.billpo;

import java.io.Serializable;

/**
 * 表示一个赠品的相关信息
 * 
 * @author 恽叶霄
 */
@SuppressWarnings("serial")
public class GiftItem implements Serializable{
    
    private String comId;
    private int num;
    private double price;

    public GiftItem(String comId, int num, double price) {
        this.comId = comId;
        this.num = num;
        this.price = price;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
