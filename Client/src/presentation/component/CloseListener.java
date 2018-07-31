package presentation.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import presentation.main.MainWindow;
/**
 * 将关闭子Panel的监听器单独作为一个类</br>
 * 也许写成单例模式会更好？？？（陷入沉思）
 * @author 钱美缘
 */
public class CloseListener implements ActionListener {

	private static MainWindow mw;
	
	public CloseListener(MainWindow mainWindow) {
		mw = mainWindow;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		mw.changePanel();
	}

}
