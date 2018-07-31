package presentation.mailui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import blservice.MailBLService;
import blservice.infoservice.GetUserInterface;
import layout.TableLayout;
import presentation.component.InfoWindow;
import vo.MailVO;

public class OneMailPanel extends JPanel {

	public OneMailPanel(MailVO mail, MailBLService mailBL, GetUserInterface userInfo, ActionListener a) {
		this.setSize(500, 250);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.setBackground(Color.WHITE);
		
		String name = "0000".equals(mail.getFromId()) ? "系统" : userInfo.getUser(mail.getFromId()).getName();
		String read = "<html><span style=\"font-size:10px;color:" + (mail.isRead() ? "black\">已读" : "red\">未读") + "</span></html>";
		String text = "<html><span style=\"font-size:10px;\">&emsp;&emsp;发信人：" 
				+ name + "&emsp;&emsp;发送时间："
				+ mail.getTime() + "</span><br/><span style=\"font-size:16px;\">&emsp;&emsp;"
				+ mail.getContent() + "</span></html>";
		this.setLayout(new TableLayout(new double[][]{{35,TableLayout.FILL,25},{20,TableLayout.FILL,20}}));
		JButton readInfo = new JButton(read);
		if (mail.isRead()) readInfo.setEnabled(false);
		readInfo.addActionListener(a);
		if (! mail.isRead()) readInfo.addActionListener(e -> {if (!mailBL.readMail(mail)) new InfoWindow("邮件标记为已读失败");});
		this.add(readInfo, "0,0,0,1");
		this.add(new JLabel(text), "1,1");
	}
}
