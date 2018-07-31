package presentation.component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;


public class DateChoosePanel extends JPanel {
    private static final long serialVersionUID = -5305728741044278631L;
    private JTextField dateField;
    
    public DateChoosePanel(String text){
        this(text, true, DateChooser.getInstance());
    }
    
    public DateChoosePanel(String text, DateChooser dateChooser){
        this(text, true, dateChooser);
    }
    
    public DateChoosePanel(String text, boolean editable){
        this(text, editable, DateChooser.getInstance());
    }
    
    public DateChoosePanel(String text, boolean editable, DateChooser dateChooser){
        super();
        double[][] size = {{-2.0, 10.0, -2.0}, {-2.0}};
        super.setLayout(new TableLayout(size));
        super.add(new JLabel(text), "0 0");
        dateField = new JTextField(10);
        dateChooser.register(dateField);
        dateField.setEditable(editable);
        super.add(dateField, "2 0");
    }

    /**
     * 注意，该方法不检查用户输入的日期文本是否合法
     */
    public String getText(){
        return this.dateField.getText();
    }

}
