package presentation.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.choosewindow.AccountChooseWin;
import presentation.component.choosewindow.CommodityChooseWin;
import presentation.component.choosewindow.CustomerChooseWin;
import presentation.component.choosewindow.UserChooseWin;
import vo.AccountVO;
import vo.CommodityVO;
import vo.CustomerVO;
import vo.UserVO;

/**
 * @author 恽叶霄
 */
public class IdNamePanel extends JPanel {

    /**
     * auto-generated UID
     */
    private static final long serialVersionUID = -4890419785265613083L;
    
    private JLabel label;
    private JTextField idField, nameField;

    public IdNamePanel(String text, int idSize, int nameSize) {
        super();
        double[][] size = {
                {-2.0, 10.0, -2.0, 5.0, -2.0},
                {-2.0}
        };
        super.setLayout(new TableLayout(size));
        super.add(label = new JLabel(text), "0 0");
        super.add(idField = new JTextField(idSize), "2 0");
        idField.setEditable(false);
        super.add(new JLabel("--"), "3 0");
        super.add(nameField = new JTextField(nameSize), "4 0");
        nameField.setEditable(false);
        
        idField.addMouseListener(new ChooseListener());
        nameField.addMouseListener(new ChooseListener());
    }
    
    public void setLabelText(String text){
        this.label.setText(text);
    }
    
    public void setId(String id){
        this.idField.setText(id);
    }
    
    public void setItsName(String name){
        this.nameField.setText(name);
    }

    public JLabel getLabel(){
        return this.label;
    }
    
    public JTextField getIdField(){
        return this.idField;
    }
    
    public JTextField getNameField(){
        return this.nameField;
    }
    
    public String getId(){
        return this.getIdField().getText();
    }
    
    public String getItsName(){
        return this.getNameField().getText();
    }

    public void addMouseListener(MouseListener l){
        idField.addMouseListener(l);
        nameField.addMouseListener(l);
    }

    class ChooseListener extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            switch (label.getText()) {
            case "赠品一":
            case "赠品二":
            case "赠品三":
            case "商品" : 
            	CommodityVO commodity = new CommodityChooseWin().getCommodity();
                if(commodity != null) {idField.setText(commodity.getId());nameField.setText(commodity.getName());}
                break;
            case "客户" :
            	CustomerVO customer = new CustomerChooseWin().getCustomer();
                if(customer != null) {idField.setText(customer.getId());nameField.setText(customer.getName());}
                break;
            case "账户" :
            	AccountVO account = new AccountChooseWin().getAccount();
                if(account != null) {idField.setText(account.getNumber());nameField.setText(account.getName());}
                break;
            case "用户" :
            	UserVO user = new UserChooseWin().getUser();
            	if(user != null) {idField.setText(user.getId());nameField.setText(user.getName());}
            	break;
            }
        }
     };
    
}
