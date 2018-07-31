package presentation.promotionui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import layout.TableLayout;
import presentation.tools.DoubleField;


@SuppressWarnings("serial")
public class DetailGroupDiscountPanel extends JPanel {
    
    private DoubleField discountField;
    
    public DetailGroupDiscountPanel(JScrollPane sp){
        super();
        init(sp);
    }
    
    public DoubleField getDiscountField(){
        return discountField;
    }
    
    private void init(JScrollPane sp){
        double[][] size = {
                {70.0, -2.0, 10.0, -1.0, 70.0},
                {-1.0, -2.0, 10.0, 100.0, 10.0, -2.0, -1.0}
        };
        super.setLayout(new TableLayout(size));
        super.add(new JLabel("商品组合"), "1 1");
        super.add(sp, "1 3 3 3");
        super.add(new JLabel("降价（元）"), "1 5");
        super.add(discountField = new DoubleField(), "3 5");
    }

}
