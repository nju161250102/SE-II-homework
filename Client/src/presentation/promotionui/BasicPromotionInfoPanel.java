package presentation.promotionui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.DateChooser;


@SuppressWarnings("serial")
public class BasicPromotionInfoPanel extends JPanel {
    
    private JTextField idField, fromField, toField;
    
    public BasicPromotionInfoPanel(){
        super();
        init();
    }
    
    public JTextField getIdField(){
        return idField;
    }
    
    public JTextField getFromField(){
        return fromField;
    }
    
    public JTextField getToField(){
        return toField;
    }
    
    private void init(){
        idField = new JTextField(10);
        idField.setEditable(false);
        fromField = new JTextField(10);
        DateChooser.getInstance().register(fromField);
        toField = new JTextField(10);
        DateChooser.getInstance().register(toField);
        double[][] size = {
                {80.0, -2.0, 80.0},
                {-1.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0}
        };
        super.setLayout(new TableLayout(size));
        super.add(new JLabel("编号"), "1 1");
        super.add(idField, "1 3");
        super.add(new JLabel("开始日期"), "1 5");
        super.add(fromField, "1 7");
        super.add(new JLabel("结束日期"), "1 9");
        super.add(toField, "1 11");
    }
    
}
