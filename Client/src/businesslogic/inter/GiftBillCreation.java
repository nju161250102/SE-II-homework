package businesslogic.inter;

import vo.MyTableModel;

/**
 * 用于系统自动建立商品赠送单
 * 
 * @author 恽叶霄
 */
public interface GiftBillCreation {
    
    boolean createAndCommit(MyTableModel gifts, String salesBillId, String operatorId, String customerId);
}
