package vo.billvo;

import vo.MyTableModel;


public class PurchaseReturnBillVO extends MarketBillVO {

    public PurchaseReturnBillVO(String date, String time, String id, String operator, int state, String customerId,
        MyTableModel model, String remark, double sum) {
        super(date, time, id, operator, state, customerId, model, remark, sum);
    }

    @Override
    protected String getPrefix() {
        return "JHTHD";
    }

}
