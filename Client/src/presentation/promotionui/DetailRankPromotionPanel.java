package presentation.promotionui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import layout.TableLayout;
import presentation.tools.DoubleField;
import presentation.tools.IntField;


@SuppressWarnings("serial")
public class DetailRankPromotionPanel extends JPanel {
    
    private IntField rankField;
    private DoubleField discountField, couponField;
    
    public DetailRankPromotionPanel(JScrollPane sp){
        super();
        init(sp);
    }
    
    public IntField getRankField(){
        return rankField;
    }
    
    public DoubleField getDiscountField() {
        return discountField;
    }

    public DoubleField getCouponField() {
        return couponField;
    }
    
    private void init(JScrollPane sp){
        double[][] size = {
                {70.0, -2.0, 10.0, -1.0, 70.0},
                {-1.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, 200.0, -1.0}
        };
        super.setLayout(new TableLayout(size));
        super.add(new JLabel("顾客等级"), "1 1");
        super.add(rankField = new IntField(10), "3 1");
        super.add(new JLabel("价格折让（元）"), "1 3");
        super.add(discountField = new DoubleField(10), "3 3");
        super.add(new JLabel("赠送代金券（元）"), "1 5");
        super.add(couponField = new DoubleField(10), "3 5");
        super.add(new JLabel("赠品（最多三种）"), "1 7 3 7");
        super.add(sp, "1 9 3 9");
    }

}
