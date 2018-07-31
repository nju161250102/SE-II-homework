package businesslogic.inter;

import vo.PromotionVO;

/**
 * 特定种类的促销策略的最优解
 * 
 * @author 恽叶霄
 */
public interface IBestPromotion {
    
    PromotionVO getBest();
    
    double getBenefit();

}
