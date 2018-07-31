package po.billpo;

import java.util.ArrayList;

/**
 * ÉÌÆ·ÔùËÍµ¥
 * 
 * @author ã¢Ò¶Ïö
 */
@SuppressWarnings("serial")
public class GiftBillPO extends BillPO {
    
    private ArrayList<GiftItem> gifts;
    private String salesBillId;
    private String customerId;

    public GiftBillPO() {}

    public GiftBillPO(String date, String time, String id, String operator, int state, 
        ArrayList<GiftItem> gifts, String salesBillId, String customerId) {
        super(date, time, id, operator, state);
        this.gifts = gifts;
        this.salesBillId = salesBillId;
        this.customerId = customerId;
    }

    @Override
    public String getAllId() {
        return "SPZSD-" + this.getDate() + "-" + this.getId();
    }

    public ArrayList<GiftItem> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<GiftItem> gifts) {
        this.gifts = gifts;
    }

    public String getSalesBillId() {
        return salesBillId;
    }

    public void setSalesBillId(String salesBillId) {
        this.salesBillId = salesBillId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
