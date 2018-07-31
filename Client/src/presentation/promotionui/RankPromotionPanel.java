package presentation.promotionui;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import blservice.PromotionBLService;
import presentation.component.InfoWindow;
import vo.MyTableModel;
import vo.PromotionVO;
import vo.RankPromotionVO;


@SuppressWarnings("serial")
public class RankPromotionPanel extends PromotionDetailPanel {
    
    private DetailRankPromotionPanel panel;
    private GiftTable giftTable;

    public RankPromotionPanel(PromotionBLService promotionAdder, ActionListener closeListener) {
        this(promotionAdder, closeListener, null);
    }
    
    public RankPromotionPanel(PromotionBLService promotionAdder, ActionListener closeListener, RankPromotionVO promotion){
        super(promotionAdder, closeListener, promotion);
    }

    @Override
    protected JPanel getCenterPanel(PromotionVO promotion) {
        JScrollPane sp = new JScrollPane();
        giftTable = new GiftTable(sp);
        panel = new DetailRankPromotionPanel(sp);
        if(promotion != null){
            RankPromotionVO rp = (RankPromotionVO)promotion;
            panel.getRankField().setValue(rp.getRank());
            panel.getDiscountField().setValue(rp.getReduction());
            panel.getCouponField().setValue(rp.getCoupon());
            giftTable.setModel(rp.getGifts());
        }
        return panel;
    }

    @Override
    protected boolean isFinished() {
        boolean dateValid = super.isFinished();
        if(dateValid){
            double discount = panel.getDiscountField().getValue(), 
                   coupon = panel.getCouponField().getValue();
            if(discount == 0.0 && coupon == 0.0 && giftTable.getRowCount() == 0){
                new InfoWindow("请在价格折让、代金券、赠品中至少选择一种优惠策略。");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean addPromotionImpl() {
        String id = getId(),
               from = getFromDate(),
               to = getToDate();
        int rank = panel.getRankField().getValue();
        double discount = panel.getDiscountField().getValue(),
               coupon = panel.getCouponField().getValue();
        MyTableModel gifts = (MyTableModel)this.giftTable.getModel();
        RankPromotionVO promotion = new RankPromotionVO(id, from, to, discount, coupon, rank, gifts);
        return promotionAdder.add(promotion);
    }
    
}
