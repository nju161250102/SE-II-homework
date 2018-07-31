package presentation.promotionui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.tools.DoubleField;


@SuppressWarnings("serial")
public class DetailSumPromotionPanel extends JPanel {

    private DoubleField startPriceField, couponField;
    private JTextField endPriceField;
    
    public DetailSumPromotionPanel(JScrollPane sp){
        super();
        init(sp);
    }
    
    public DoubleField getStartPriceField(){
        return startPriceField;
    }
    
    public DoubleField getCouponField(){
        return couponField;
    }
    
    public JTextField getEndPriceField(){
        return endPriceField;
    }
    
    private void init(JScrollPane sp){
        startPriceField = new DoubleField(10);
        endPriceField = new JTextField(10);
        couponField = new DoubleField(10);

        double[][] size = {
                {70.0, -2.0, 10.0, -2.0, 20.0, -2.0, -1.0, 70.0},
                {-1.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, 200.0, -1.0}
        };
        super.setLayout(new TableLayout(size));
        super.add(new JLabel("消费区间（元）"), "1 1");
        super.add(startPriceField, "3 1");
        super.add(new JLabel("--"), "4 1");
        super.add(endPriceField, "5 1");
        super.add(new JLabel("赠送代金券（元）"), "1 3");
        super.add(couponField, "3 3");
        super.add(new JLabel("赠品"), "1 5");
        super.add(sp, "1 7 6 7");
    }

}
