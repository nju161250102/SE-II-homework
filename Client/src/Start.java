import java.rmi.server.RMISocketFactory;

import presentation.LoginWindow;
import rmi.SMRMISocket;

public class Start {
	public static void main(String[] args) {
		try {
	        RMISocketFactory.setSocketFactory(new SMRMISocket());
	    } catch (Exception ex) {}
		new LoginWindow();
	}
}
