package presentation.dataui.userui;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.dataui.util.Tools;
import presentation.tools.InputCheck;
import vo.UserType;
import vo.UserVO;

/**
 * 用户信息的增加/修改面板
 * @author 恽叶霄
 *
 */
@SuppressWarnings("serial")
class InputUserPanel extends JPanel {

	private JPanel typePanel;
    private JTextField userIdTextField, userNameTextField, userKeyTextField, userAgeTextField, userTelTextField;
    private ButtonGroup sexButtonGroup, typeButtonGroup;
    private JComboBox<String> rankComboBox;

	/**
	 * 根据已有数据初始化界面
	 * @param 表格中取出的一行数据
	 */
	protected InputUserPanel(String[] user) {
		super();
        double[][] size = {{40.0, TableLayout.PREFERRED, 10.0, TableLayout.PREFERRED, 40.0}
            , {20.0, TableLayout.FILL, 10.0, TableLayout.FILL, 10.0, TableLayout.FILL
                , 10.0, TableLayout.FILL, 10.0, TableLayout.FILL, 10.0, TableLayout.FILL, 10.0
                , TableLayout.FILL, 10.0, TableLayout.FILL, 20.0,}};
		this.setLayout(new TableLayout(size));
		
		String[] texts = {"用户编号", "用户名", "用户密码", "用户级别", "用户性别", "用户年龄", "用户电话号码", "用户种类"};
		JLabel[] labels = new JLabel[texts.length];
		for(int i = 0; i < labels.length; i++){
		    labels[i] = new JLabel(texts[i]);
		    this.add(labels[i], "1 " + (2 * i + 1));
		}
	
        userIdTextField = new JTextField(user[0]);
		userNameTextField = new JTextField(user[1]);
		userKeyTextField = new JTextField(user[4]);
        userAgeTextField = new JTextField(user[6]);
        userTelTextField = new JTextField(user[7]);
        
        userIdTextField.setEditable(false);
        
        rankComboBox = new JComboBox<>();
        rankComboBox.addItem("默认");

		JRadioButton manRadioButton = new JRadioButton("男"),
			    	 womanRadioButton = new JRadioButton("女");
		manRadioButton.setSelected(true);
		JPanel sexPanel = new JPanel();
		sexPanel.add(manRadioButton);
		sexPanel.add(womanRadioButton);
		sexButtonGroup = new ButtonGroup();
		sexButtonGroup.add(manRadioButton);
		sexButtonGroup.add(womanRadioButton);

		JRadioButton typeAdminRadioButton = new JRadioButton(UserType.ADMIN.getName()),
					 typeKeeperRadioButton = new JRadioButton(UserType.KEEPER.getName()),
					 typeSalesmanRadioButton = new JRadioButton(UserType.SALESMAN.getName()),
					 typeAccountantRadioButton = new JRadioButton(UserType.ACCOUNTANT.getName()),
					 typeGmRadioButton = new JRadioButton(UserType.GM.getName());
		typeKeeperRadioButton.addActionListener(e -> defaultType());
		typeSalesmanRadioButton.addActionListener(e -> salesType());
		typeAccountantRadioButton.addActionListener(e -> accountantType());
		typeGmRadioButton.addActionListener(e -> defaultType());
		typeAdminRadioButton.addActionListener(e -> defaultType());
		
		typeKeeperRadioButton.setSelected(true);
		typePanel = new JPanel();
		typePanel.add(typeAdminRadioButton);
		typePanel.add(typeKeeperRadioButton);
		typePanel.add(typeSalesmanRadioButton);
		typePanel.add(typeAccountantRadioButton);
		typePanel.add(typeGmRadioButton);
		typeButtonGroup = new ButtonGroup();
		typeButtonGroup.add(typeAdminRadioButton);
		typeButtonGroup.add(typeKeeperRadioButton);
		typeButtonGroup.add(typeSalesmanRadioButton);
		typeButtonGroup.add(typeAccountantRadioButton);
		typeButtonGroup.add(typeGmRadioButton);
		
		int length = user.length;
		if(length > 2 && user[2] != null) {
        	if ("财务人员".equals(user[2])) accountantType();
        	if ("进货销售人员".equals(user[2])) accountantType();
		    Enumeration<AbstractButton> eb = typeButtonGroup.getElements();
		    while(eb.hasMoreElements()){
		        AbstractButton b = eb.nextElement(); 
		        if(b.getText().equals(user[2])) {
		            b.setSelected(true);
		            break;
		        }
		    }
		}
		
		if(length > 5 && user[5] != null) {
		    if(user[5].equals(manRadioButton.getText()))
		        manRadioButton.setSelected(true);
		    else womanRadioButton.setSelected(true);
		}
		
		if(length > 3 && user[3] != null) {
			if (rankComboBox.getItemAt(0).equals(user[3])) rankComboBox.setSelectedIndex(0);
			else rankComboBox.setSelectedIndex(1);
		}
	
		add(userIdTextField, "3 1");
		add(userNameTextField, "3 3");
		add(userKeyTextField, "3 5");
		add(rankComboBox, "3 7");
		add(sexPanel, "3 9");
		add(userAgeTextField, "3 11");
		add(userTelTextField, "3 13");
		add(typePanel, "3 15");
	}

	/**
	 * 得到面板输入的UserVO对象
	 * @return
	 */
	public UserVO getUserVO() {
		if (! InputCheck.isLegal(userNameTextField.getText())) new InfoWindow("用户姓名格式非法");
		else if (! InputCheck.isAlnum(userKeyTextField.getText(), 0)) new InfoWindow("用户密码格式非法");
		else if (! InputCheck.isAllNumber(userAgeTextField.getText(), 2)) new InfoWindow("请输入真实的用户年龄");
		else if (! InputCheck.isAllNumber(userTelTextField.getText(), 11)) new InfoWindow("请输入11位手机号码");
		else {
			String id = userIdTextField.getText()
			        , name = userNameTextField.getText()
			        , key = userKeyTextField.getText()
			        , sex = Tools.getSelectedText(sexButtonGroup)
			        , telNumber = userTelTextField.getText();
		    UserType type = getSelectedType();
		    int rank = rankComboBox.getSelectedIndex();
	        int age = Integer.parseInt(userAgeTextField.getText());
		    return new UserVO(name, key, type, rank, id, sex, telNumber, age);
		}
		return null;
	    
	}
	/**
	 * 防止用户更改种类和权限
	 */
	protected void lock() {
		rankComboBox.setEnabled(false);;
		for (Component c : typePanel.getComponents()) c.setEnabled(false);
	}
	
	private UserType getSelectedType(){
	    String type = Tools.getSelectedText(typeButtonGroup);
	    if(type.equals(UserType.ADMIN.getName()))
	        return UserType.ADMIN;
	    if(type.equals(UserType.KEEPER.getName()))
	        return UserType.KEEPER;
	    if(type.equals(UserType.SALESMAN.getName()))
	        return UserType.SALESMAN;
	    if(type.equals(UserType.ACCOUNTANT.getName()))
	        return UserType.ACCOUNTANT;
	    if(type.equals(UserType.GM.getName()))
	        return UserType.GM;
	    return null;
	}
	
	private void defaultType(){
	    rankComboBox.removeAllItems();
	    rankComboBox.addItem("默认");
	}
	
	private void salesType(){
	    rankComboBox.removeAllItems();
	    rankComboBox.addItem("普通销售员");
	    rankComboBox.addItem("销售经理");
	}

	private void accountantType(){
	    rankComboBox.removeAllItems();
	    rankComboBox.addItem("普通财务人员");
	    rankComboBox.addItem("最高权限财务人员");
	}
}
