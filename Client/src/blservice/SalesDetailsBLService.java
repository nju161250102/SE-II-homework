package blservice;

import vo.MyTableModel;

public interface SalesDetailsBLService {
    /**获取销售明细表*/
    MyTableModel filter(String from, String to, String commodityId, String store
        , String customerId, String operatorId);
}
