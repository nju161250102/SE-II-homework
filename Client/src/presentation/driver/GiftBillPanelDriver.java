package presentation.driver;

import javax.swing.JFrame;
import javax.swing.JPanel;

import presentation.billui.BillPanelHelper;
import presentation.tools.StyleTools;
import vo.MyTableModel;
import vo.billvo.BillVO;
import vo.billvo.GiftBillVO;

public class GiftBillPanelDriver {
    
    public GiftBillPanelDriver(){
        JFrame frame = new JFrame();
        frame.setLocation(400, 200);
        StyleTools.setNimbusLookAndFeel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] columnNames = {"编号", "名称", "型号", "单价", "数量"};
        String[][] data = {{"000001", "", "", "", ""}};
        MyTableModel gifts = new MyTableModel(data, columnNames);
        GiftBillVO bill = new GiftBillVO(
            "SPZSD-20171230-12344", "20171230", "14:12:34", "0002", 
            BillVO.PASS, gifts, "XSD-20171230-12013", "000002"
        );
        frame.setContentPane((JPanel)BillPanelHelper.createInner(bill));
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        new GiftBillPanelDriver();
    }

}
