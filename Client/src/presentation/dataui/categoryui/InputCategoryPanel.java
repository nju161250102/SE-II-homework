package presentation.dataui.categoryui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.tools.InputCheck;
import vo.CategoryVO;

@SuppressWarnings("serial")
class InputCategoryPanel extends JPanel {
    
    private JTextField idTextField, nameTextField, fatherIdTextField, fatherNameTextField;

    public InputCategoryPanel(String[] data) {
        super();
        double[][] size = {{40.0, TableLayout.PREFERRED, 10.0, TableLayout.FILL, 40.0}
            , {TableLayout.FILL, TableLayout.PREFERRED, 10.0, TableLayout.PREFERRED, 10.0
                , TableLayout.PREFERRED, 10.0, TableLayout.PREFERRED, TableLayout.FILL}};
        this.setLayout(new TableLayout(size));
        
        JLabel idLabel = new JLabel("分类编号"),
               nameLabel = new JLabel("分类名称"),
               fatherIdLabel = new JLabel("上一级分类编号"),
               fatherNameLabel = new JLabel("上一级分类名称");
        idTextField = new JTextField(data[0]);
        nameTextField = new JTextField(data[1]);
        fatherIdTextField = new JTextField(data[2]);
        fatherNameTextField = new JTextField(data[3]);
        
        idTextField.setEditable(false);
        fatherIdTextField.setEditable(false);
        fatherNameTextField.setEditable(false);
        this.add(idLabel, "1 1");
        this.add(nameLabel, "1 3");
        this.add(fatherIdLabel, "1 5");
        this.add(fatherNameLabel, "1 7");
        this.add(idTextField, "3 1");
        this.add(nameTextField, "3 3");
        this.add(fatherIdTextField, "3 5");
        this.add(fatherNameTextField, "3 7");
    }
    
    public CategoryVO getCategoryVO(){
    	if (! InputCheck.isLegal(fatherNameTextField.getText())) {new InfoWindow("非法输入"); return null;}
        return new CategoryVO(fatherIdTextField.getText(), idTextField.getText(), nameTextField.getText());
    }

}
