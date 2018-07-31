package presentation;

import javax.swing.JPanel;

import vo.UserVO;

public interface PanelInterface {
	
	/**
	 * Panel关闭自身时调用close确认退出并保存等
	 * @return 确认退出返回true,否则false(界面阶段默认为true)
	 * @author 钱美缘
	 */
	public boolean close();
	
	//返回所持有的JPanel对象的引用
	public JPanel getPanel();
	
}
