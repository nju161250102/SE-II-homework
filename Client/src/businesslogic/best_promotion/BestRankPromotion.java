package businesslogic.best_promotion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import businesslogic.PromotionBL;
import businesslogic.inter.IBestPromotion;
import businesslogic.inter.IPromotionSearch;
import po.RankPromotionPO;
import vo.PromotionVO;


public class BestRankPromotion implements IBestPromotion {
    
    private int rank;
    private IPromotionSearch promotionBl;
    private PromotionVO best;
    private double benefit;

    public BestRankPromotion(int rank) {
        this.rank = rank;
        this.promotionBl = new PromotionBL();
        judge();
    }

    @Override
    public PromotionVO getBest() {
        return best;
    }

    @Override
    public double getBenefit() {
        return benefit;
    }
    
    private void judge(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        ArrayList<RankPromotionPO> promotions = promotionBl.searchRankPromotionPO(date, rank);
        if(promotions.isEmpty()){
            return;
        }
        Collections.sort(promotions, (a,b)->compare(a,b));
        best = promotionBl.toVO(promotions.get(0));
        benefit = getBenefit(promotions.get(0));
    }
    
    private static int compare(RankPromotionPO a, RankPromotionPO b){
        double benefitA = getBenefit(a), benefitB = getBenefit(b);
        if(benefitA > benefitB) return -1;
        if(benefitA == benefitB) return 0;
        return 1;
    }
    
    private static double getBenefit(RankPromotionPO promotion){
        double benefit = promotion.getCoupon() + promotion.getReduction();
        benefit += promotion.getGifts().stream().mapToDouble(e -> e.getPrice() * e.getNum()).sum();
        return benefit;
    }

}
