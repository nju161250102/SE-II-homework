package presentation.promotionui;

import javax.swing.JDialog;

import blservice.PromotionBLService;
import presentation.tools.StyleTools;
import vo.GroupDiscountVO;
import vo.PromotionVO;
import vo.RankPromotionVO;
import vo.SumPromotionVO;

public class EditPromotionWin {
    
    private JDialog frame;
    private PromotionBLService promotionBl;
    private PromotionVO promotion;
    
    public EditPromotionWin(PromotionBLService promotionBl, PromotionVO promotion){
        super();
        this.promotionBl = promotionBl;
        this.promotion = promotion;
        frame = new JDialog();
        frame.setModal(true);
        StyleTools.setNimbusLookAndFeel();
        frame.setSize(800, 400);
        frame.setLocation(300, 200);
        frame.setContentPane(initContent());
        frame.setTitle("ÐÞ¸Ä´ÙÏú²ßÂÔ");
        frame.setVisible(true);
    }
    
    private CenterPanel initContent(){
        if(promotion == null){
            frame.dispose();
            return null;
        }
        if(promotion instanceof RankPromotionVO){
            return new RankPromotionPanel(promotionBl, e->frame.dispose(), (RankPromotionVO)promotion);
        } else if(promotion instanceof GroupDiscountVO){
            return new GroupDiscountPanel(promotionBl, e->frame.dispose(), (GroupDiscountVO)promotion);
        } else {
            return new SumPromotionPanel(promotionBl, e->frame.dispose(), (SumPromotionVO)promotion);
        }
    }

}
