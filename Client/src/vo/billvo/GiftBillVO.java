package vo.billvo;

import vo.MyTableModel;


public class GiftBillVO extends BillVO {
    
    private MyTableModel gifts;
    private String salesBillId, customerId;
    
    public GiftBillVO(String id, String date, String time, String operator, int state,
        MyTableModel gifts, String salesBillId, String customerId){
        super(id, date, time, operator, state);
        this.gifts = gifts;
        this.salesBillId = salesBillId;
        this.customerId = customerId;
    }

    public MyTableModel getGifts() {
        return gifts;
    }

    public String getSalesBillId() {
        return salesBillId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getAllId() {
        return "SPZSD-" + this.getDate() + "-" + this.getId();
    }
    

}
