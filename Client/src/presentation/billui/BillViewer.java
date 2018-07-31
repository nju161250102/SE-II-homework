package presentation.billui;

import javax.swing.JDialog;
import javax.swing.JPanel;

import vo.billvo.BillVO;

public class BillViewer {
    
    private JDialog frame;
    private JPanel panel;
    
    public BillViewer(BillVO bill) {
    	frame = new JDialog();
        frame.setModal(true);
        frame.setTitle("查看单据具体内容");
        BillPanelInterface billPanelImp = BillPanelHelper.createInner(bill);
        billPanelImp.setEditable(true);
        panel = (JPanel) billPanelImp;
        frame.setContentPane(panel);
        frame.setLocation(300, 100);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
