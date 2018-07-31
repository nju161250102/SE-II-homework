package businesslogic.best_promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import businesslogic.PromotionBL;
import businesslogic.inter.IBestPromotion;
import businesslogic.inter.IPromotionSearch;
import po.SumPromotionPO;
import vo.PromotionVO;


public class BestSumPromotion implements IBestPromotion {
    
    private double sum;
    private IPromotionSearch promotionBl;
    private PromotionVO best;
    private double benefit;

    public BestSumPromotion(double sum) {
        this.sum = sum;
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
        ArrayList<SumPromotionPO> promotions = promotionBl.searchSumPromotionPO(date);
        if(promotions.isEmpty()){
            return;
        }
        try{
            SumPromotionPO best = promotions.stream().filter(e -> valid(e, date))
                        .sorted((a, b)->compare(a, b)).findFirst().get();
            this.best = promotionBl.toVO(best);
            benefit = getBenefit(best);
        }catch(NoSuchElementException e){
            return;
        }
    } 
    
    private boolean valid(SumPromotionPO promotion, String date){
        boolean timeValid = !before(date, promotion.getFromDate()) && !before(promotion.getToDate(), date);
        boolean sumValid = sum >= promotion.getStartPrice() && sum <= promotion.getEndPrice();
        return timeValid && sumValid;
    }
    
    private static boolean before(String dateA, String dateB){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date a = formatter.parse(dateA), b = formatter.parse(dateB);
            return a.before(b);
        }catch(ParseException e){
            e.printStackTrace();
            return false;
        }
    }
    
    private static int compare(SumPromotionPO a, SumPromotionPO b){
        double benefitA = getBenefit(a), benefitB = getBenefit(b);
        if(benefitA > benefitB) return -1;
        if(benefitA == benefitB) return 0;
        return 1;
    }
    
    private static double getBenefit(SumPromotionPO promotion){
        double benefit = promotion.getCoupon();
        benefit += promotion.getGifts().stream().mapToDouble(e -> e.getNum() * e.getPrice()).sum();
        return benefit;
    }
    
}
