package presentation.dataui.util;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class Tools {

    private Tools() {}
    
	public static String getSelectedText(ButtonGroup group){
	    Enumeration<AbstractButton> eb = group.getElements();
	    while(eb.hasMoreElements()){
	        AbstractButton b = eb.nextElement();
	        if(b.isSelected())
	            return b.getText();
	    }
	    return "";
	}
}
