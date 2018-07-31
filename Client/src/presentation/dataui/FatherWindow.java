package presentation.dataui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import presentation.component.InfoWindow;

public abstract class FatherWindow {

	protected JDialog frame = new JDialog();
	protected JButton yesButton = new JButton("确定");
	
	protected FatherWindow() {
		frame.setModal(true);
		
		//设置窗体大小及位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width/2, screenSize.height/2);
		frame.setLocation(screenSize.width/4, screenSize.height/4);
		frame.setResizable(false);
		
		//设置界面布局
		frame.setLayout(new BorderLayout());
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.RIGHT);
		JPanel southPanel = new JPanel(f);
		JButton quitButton = new JButton("取消");
		southPanel.add(yesButton);
		southPanel.add(quitButton);
		frame.add(southPanel, BorderLayout.SOUTH);
		
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}			
		});

		yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (taskFinished()) {
                    frame.dispose();
                    new InfoWindow(getSuccessMsg());
                }	
            }
        });

		//关闭窗体的适配器方法
		class WindowCloseListener extends WindowAdapter {
			@Override
		    public void windowClosing(WindowEvent e) {
				
			}
		}
		frame.addWindowListener(new WindowCloseListener());
	}
	
	abstract protected boolean taskFinished();
	
	abstract protected String getSuccessMsg();
}
