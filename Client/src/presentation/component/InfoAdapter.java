package presentation.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import presentation.main.MainWindow;

public class InfoAdapter extends MouseAdapter {

	private String str;
	
	public InfoAdapter(String str) {
		this.str = str;
	}
	
	@Override  
    public void mouseEntered(MouseEvent e) {
		MainWindow.setInfo(str);  
    }  
  
    @Override  
    public void mouseExited(MouseEvent e) {
    	MainWindow.setInfo();  
    }  
}
