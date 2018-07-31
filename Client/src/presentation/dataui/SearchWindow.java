package presentation.dataui;

import java.awt.BorderLayout;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import blservice.DataBLService;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.dataui.util.Tools;
import presentation.tools.InputCheck;
import vo.MyTableModel;

public abstract class SearchWindow extends FatherWindow {
    
    private DataBLService dataBl;
    private JTextField keyTextField;
    private ButtonGroup typeGroup;
    private MyTableModel model;

    public SearchWindow(DataBLService dataBl) {
        super();
        this.dataBl = dataBl;
        
        JLabel keyLabel = new JLabel("关键词"),
               typeLabel = new JLabel("搜索种类");
        keyTextField = new JTextField();
        typeGroup = initTypeGroup();
        JPanel typePanel = new JPanel();
        Enumeration<AbstractButton> eb = typeGroup.getElements();
        while(eb.hasMoreElements()){
            AbstractButton b = eb.nextElement();
            if(b != null)typePanel.add(b);
        }

        double[][] size = {{TableLayout.FILL, TableLayout.PREFERRED, 10.0, TableLayout.PREFERRED, TableLayout.FILL}
                , {TableLayout.FILL, TableLayout.PREFERRED, 10.0, TableLayout.PREFERRED, TableLayout.FILL}};
        JPanel centerPanel = new JPanel(new TableLayout(size));
        centerPanel.add(keyLabel, "1 1");
        centerPanel.add(typeLabel, "1 3");
        centerPanel.add(keyTextField, "3 1");
        centerPanel.add(typePanel, "3 3");
        
        frame.add(centerPanel, BorderLayout.CENTER);
    }
    
    abstract protected ButtonGroup initTypeGroup();
    
    public MyTableModel getModel(){
        return model;
    }

    @Override
    protected boolean taskFinished() {
    	if (InputCheck.isLegal(keyTextField.getText())) {
    		String key = keyTextField.getText(),
    	               type = Tools.getSelectedText(typeGroup);
    	        model = dataBl.search(type, key);
    	        return true;
    	} else {
    		new InfoWindow("请输入正确的查询关键词");
    		return false;
    	}
    }

    @Override
    protected String getSuccessMsg() {
        return "查询完毕";
    }

}
