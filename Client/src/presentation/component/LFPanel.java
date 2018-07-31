package presentation.component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;

/**
 * @author 恽叶霄
 * JLabel与JTextField水平混合的面板
 */
public class LFPanel extends JPanel {
    
    /**
     * auto-generated UID
     */
    private static final long serialVersionUID = 4708726257852745485L;
    private JLabel label;
    private JTextField textField;
    
    public LFPanel(String text, int columnSize){
        this(text, columnSize, true);
    }

    public LFPanel(String text, int columnSize, boolean editable) {
        super();
        double[][] size = {{-2.0, 10.0, -2.0}, {-2.0}};
        super.setLayout(new TableLayout(size));
        super.add(label = new JLabel(text), "0 0");
        super.add(textField = new JTextField(columnSize), "2 0");
        this.textField.setEditable(editable);
    }
    
    public void setEditable(boolean editable){
        this.textField.setEditable(editable);
    }
    
    public void setLabelText(String text){
        this.label.setText(text);
    }
    
    public void setFieldText(String text){
        this.textField.setText(text);
    }
    
    public JLabel getLabel(){
        return this.label;
    }
    
    public JTextField getTextField(){
        return this.textField;
    }
    
    public String getText(){
        return this.textField.getText();
    }

}
