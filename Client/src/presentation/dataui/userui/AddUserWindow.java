package presentation.dataui.userui;

import java.awt.BorderLayout;

import blservice.UserBLService;
import presentation.dataui.FatherWindow;
import vo.UserVO;

/**
 * 与通用数据模块对应的通用数据添加窗体</br>
 * 同样根据UserType判别类型，可留待修改
 * @author 钱美缘
 */
public class AddUserWindow extends FatherWindow{
    
    private InputUserPanel centerPanel;
    private UserBLService userBL;
	
	public AddUserWindow(UserBLService userBl) {
		super();
		this.userBL = userBl;

        frame.setTitle("增加用户");
        centerPanel = new InputUserPanel(new String[]{userBl.getNewId(), null, null, null, null, null, null, null});
        frame.add(centerPanel, BorderLayout.CENTER);
        
		frame.setVisible(true);
	}

    @Override
    protected boolean taskFinished() {
        UserVO user = centerPanel.getUserVO();
        return user != null && userBL.add(user);
    }

    @Override
    protected String getSuccessMsg() {
        return "添加用户成功";
    }

}