package presentation.tools;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * @author ã¢Ò¶Ïö
 */
public final class StyleTools {
    
    public static void setNimbusLookAndFeel(){
  		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
        JFrame.setDefaultLookAndFeelDecorated(true);
    }

}
