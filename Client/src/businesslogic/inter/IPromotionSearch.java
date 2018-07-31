package businesslogic.inter;

import java.util.ArrayList;

import po.GroupDiscountPO;
import po.PromotionPO;
import po.RankPromotionPO;
import po.SumPromotionPO;
import vo.PromotionVO;

/**
 * 提供给<code>IBestPromotion</code>的实现者的接口
 * @author 恽叶霄
 */
public interface IPromotionSearch {
    
    ArrayList<GroupDiscountPO> searchGroupDiscountPO(String date);
    
    ArrayList<RankPromotionPO> searchRankPromotionPO(String date, int rank);
    
    ArrayList<SumPromotionPO> searchSumPromotionPO(String date);
    
    PromotionVO toVO(PromotionPO po);

}
