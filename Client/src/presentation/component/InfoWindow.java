package presentation.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class InfoWindow {

	private JDialog frame = new JDialog();
	
	public InfoWindow(String str) {
		frame.setModal(true);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(Color.WHITE);
		
		JLabel label = new JLabel(str, JLabel.CENTER);
		label.setBorder(new LineBorder(Color.BLACK));
		Font f = new Font("µÈÏß", Font.PLAIN, 23);
		label.setFont(f);
		FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
		frame.setSize(fm.stringWidth(str)+80, fm.getHeight()+50);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width-frame.getWidth())/2, (screenSize.height-frame.getHeight())/2);
		
		frame.add(label);
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			private int count = 50;
			@Override
			public void run() {
				if (count > 0) {
					count--;
					frame.setOpacity(count * 0.02f);
				} else frame.dispose();
			}
		};
		timer.schedule(task, 700L, 10L);
		
		frame.setVisible(true);
	}
}
