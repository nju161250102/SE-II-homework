package presentation.dataui.userui;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import blservice.UserBLService;
import presentation.dataui.SearchWindow;

public class SearchUserWindow extends SearchWindow{
    
    public SearchUserWindow(UserBLService userBl) {
        super(userBl);
        frame.setTitle("查询用户");
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    @Override
    protected ButtonGroup initTypeGroup() {
        JRadioButton idRadioButton = new JRadioButton("按编号搜索");
        idRadioButton.setSelected(true);
        JRadioButton nameRadioButton = new JRadioButton("按名称搜索");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(idRadioButton);
        typeGroup.add(nameRadioButton);
        return typeGroup;
    }
   
}
