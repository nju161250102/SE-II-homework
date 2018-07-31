package presentation.mailui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import blservice.MailBLService;
import blservice.infoservice.GetUserInterface;
import businesslogic.MailBL;
import businesslogic.UserBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;
import presentation.main.MainWindow;
import vo.MailVO;

public class MailPanel implements PanelInterface {

	private JPanel panel = new JPanel(new TableLayout(new double[][]{{TableLayout.FILL},{0.1,TableLayout.FILL}}));
	private JPanel mailPanel;
	private JScrollPane scrollPane = new JScrollPane();
	private MailBLService mailBL = new MailBL();
	private GetUserInterface userInfo = new UserBL();
	
	public MailPanel(ActionListener closeListener) {
		TopButtonPanel buttonPanel = new TopButtonPanel();
		buttonPanel.addButton("新邮件", new ImageIcon("resource/SendMail.png"), e->{
			String[] input = new NewMailWindow().getMessage();
			if (input != null) {
				if (mailBL.saveMail(MainWindow.getUser().getId(), input[0], input[1])) new InfoWindow("邮件发送成功");
			}
		});
		buttonPanel.addButton("全部已读", new ImageIcon("resource/ReadAll.png"), e->{
			ArrayList<MailVO> mailList = mailBL.getMailList(MainWindow.getUser());
			for(MailVO mail : mailList) if (! mail.isRead()) mailBL.readMail(mail);
		});
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
		panel.add(buttonPanel.getPanel(), "0,0");
		getMailList();
	}
	
	@Override
	public boolean close() {
		return true;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	class UpdateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			getMailList();
		}
	}
	
	private void getMailList() {
		ArrayList<MailVO> mailList = mailBL.getMailList(MainWindow.getUser());
		double[][] size = new double[2][];
		size[0] = new double[]{TableLayout.FILL};
		size[1] = new double[mailList.size()];
		for (int i = 0; i < mailList.size(); i++) size[1][i] = TableLayout.PREFERRED;
		mailPanel = new JPanel(new TableLayout(size));
		
		for (int i = 0; i < mailList.size(); i++) mailPanel.add(new OneMailPanel(mailList.get(i),mailBL,userInfo,new UpdateListener()), "0, "+i);
		scrollPane.setViewportView(mailPanel);
		panel.add(scrollPane, "0,1,f");
		panel.updateUI();
	}
}
