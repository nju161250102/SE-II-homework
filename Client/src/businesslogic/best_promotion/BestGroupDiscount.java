package businesslogic.best_promotion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import businesslogic.PromotionBL;
import businesslogic.inter.IBestPromotion;
import businesslogic.inter.IPromotionSearch;
import po.GroupDiscountPO;
import vo.GroupDiscountVO;
import vo.MyTableModel;
import vo.PromotionVO;


public class BestGroupDiscount implements IBestPromotion {
    
    /** The goods a customer purchases. */
    private MyTableModel goods;
    private IPromotionSearch promotionBl;
    private PromotionVO best;
    private double benefit;

    public BestGroupDiscount(MyTableModel goods) {
        this.goods = goods;
        promotionBl = new PromotionBL();
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
        String today = formatter.format(new Date());
        ArrayList<GroupDiscountPO> promotions = promotionBl.searchGroupDiscountPO(today);
        ArrayList<GroupDiscountVO> properPromotions = new ArrayList<>();
        promotions.forEach(e->{
            int num = getGroupAmount(e);
            if(num > 0){
                GroupDiscountVO vo = (GroupDiscountVO)promotionBl.toVO(e);
                vo.setReduction(num);
                properPromotions.add(vo);
            }
        });
        if(properPromotions.isEmpty()){
            return;
        }
        Collections.sort(properPromotions, (a,b)->{
            if(a.getReduction() > b.getReduction()) return -1;
            if(a.getReduction() == b.getReduction()) return 0;
            return 1;
        });
        best = properPromotions.get(0);
        benefit = best.getReduction();
    }
    
    private int getGroupAmount(GroupDiscountPO group){
        ArrayList<String> comIds = group.getGroup();
        int size = comIds.size();
        int min = 0;
        for(int i = 0; i < size; i++){
            int num = getComAmount(comIds.get(i));
            if(num <= 0) return 0;
            if(min > num) min = num;
        }
        return min;
    }
    
    private int getComAmount(String comId){
        int rows = goods.getRowCount();
        for(int i = 0; i < rows; i++){
            String id = goods.getValueAt(i, 0).toString();
            if(id.equals(comId)){
                return Integer.parseInt(goods.getValueAt(i, 5).toString());
            }
        }
        return 0;
    }

}
