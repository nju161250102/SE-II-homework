package presentation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import blservice.LoginBLService;
import businesslogic.LoginBL;
import layout.TableLayout;
import presentation.main.MainWindow;
import vo.UserVO;

public class LoginWindow {
	private JFrame loginWindow = new JFrame("灯具进销存管理系统-登录界面");
	private JTextField nameField = new JTextField();
	private JPasswordField keyField = new JPasswordField();
	private JButton buttonA = getButton("取消");
	private JButton buttonB = getButton("登录");
	private LoginBLService loginBL = new LoginBL();
	
	public LoginWindow() {
    	try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
        JFrame.setDefaultLookAndFeelDecorated(true);
        
		//set size of form according to screen's size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		loginWindow.setSize(screenSize.width, screenSize.height);
		loginWindow.setLocation(0, 0);
		
		//other setting
		loginWindow.setResizable(false);
		loginWindow.setUndecorated(true);
		loginWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("resource/mouse.png"), new Point(0, 0),"Slef"));
		loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		double[][] size = {{0.39019,0.05124,0.04319,0.02853,0.09424,TableLayout.FILL},
				{0.476,0.062,0.040,0.062,0.03,0.048,TableLayout.FILL}};
		
		buttonA.setOpaque(false);
		buttonB.setOpaque(false);
		buttonA.setBorderPainted(false);
		buttonB.setBorderPainted(false);
		keyField.setBorder(null);
		nameField.setBorder(null);
		keyField.setFont(new Font("宋体",Font.PLAIN,20));
		nameField.setFont(new Font("宋体",Font.PLAIN,20));
		
		//add components
		JPanel panel = (JPanel)loginWindow.getContentPane();
		panel.setLayout(new TableLayout(size));
		panel.setOpaque(false);
        panel.add(nameField, "2, 1, 4, 1");
        panel.add(keyField,"2, 3, 4, 3");
        panel.add(buttonA, "1, 5, 2, 5");
        panel.add(buttonB, "4, 5");
        
        ImageIcon image = new ImageIcon("resource/LoginBG.png");
        Image img = image.getImage();  
        img = img.getScaledInstance(loginWindow.getWidth(), loginWindow.getHeight(), Image.SCALE_DEFAULT);  
        image.setImage(img); 
		JLabel label = new JLabel(image);
		label.setBounds(0,0,loginWindow.getWidth(),loginWindow.getHeight());
		loginWindow.getLayeredPane().add(label ,new Integer(Integer.MIN_VALUE)); 
		loginWindow.setVisible(true);
		
		//add listener
		buttonA.addActionListener(e ->System.exit(1));
		buttonB.addActionListener(e -> login());
		nameField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
			}
		});
		keyField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
			}
		});
	}

	private JButton getButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("华文行楷", Font.PLAIN, 20));
		return button;
	}
	
	private void login() {
		UserVO user = loginBL.getUser(nameField.getText(), new String(keyField.getPassword()));
		if (user == null) {
			nameField.setText("");
			keyField.setText("");
			JOptionPane.showMessageDialog(null, "用户名或密码不正确，请重新输入", "系统消息", JOptionPane.ERROR_MESSAGE);
		} else {
			new MainWindow(user);
			loginWindow.dispose();
		}
	}
}