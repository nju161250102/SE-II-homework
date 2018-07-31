package presentation.billui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.AccountChooseWin;
import presentation.tools.InputCheck;
import vo.AccountVO;

/**
 * 输入转账清单的面板
 * @author 郝睿
 */
public class InputTransferItemInfoWin {
    
    private JTextField[] fields; 
    private JDialog frame;
    private String[] rowData = null;
    private String[] labelTexts = {"银行账户", "转账金额", "备注"};

    public InputTransferItemInfoWin() {
        frame = new JDialog();
        frame.setTitle("填写转账清单信息");
        frame.setModal(true);
        frame.setSize(400, 250);
        frame.setLocation(400, 200);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        initCenter();
        initSouth();
        frame.setVisible(true);
    }

    public String[] getRowData(){
        return rowData;
    }

    private void initSouth() {
        JButton okButton = new JButton("确定");
        okButton.addActionListener(e -> handleOk());
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> frame.dispose());

        double[][] size = {
                {-1.0, -2.0, 10.0, -2.0, 20.0},
                {10.0, -2.0, 10.0}
        };
        JPanel southPanel = new JPanel(new TableLayout(size));
        southPanel.add(okButton, "1 1");
        southPanel.add(cancelButton, "3 1");
        
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    private void initCenter() {
        double[] columnSize = {TableLayout.FILL, TableLayout.PREFERRED , 10.0, 0.6, 10.0, TableLayout.PREFERRED, TableLayout.FILL};
        double[] rowSize = new double[labelTexts.length * 2 + 1];
        rowSize[0] = 20.0;
        for(int i = 0; i < labelTexts.length; i++){
            rowSize[2 * i + 1] = -2.0;
            rowSize[2 * i + 2] = 10.0;
        }
        JPanel centerPanel = new JPanel(new TableLayout(
            new double[][]{columnSize, rowSize}));

        JLabel[] labels = new JLabel[labelTexts.length];
        fields = new JTextField[labelTexts.length];
        for(int i = 0; i < labelTexts.length; i++){
            labels[i] = new JLabel(labelTexts[i]);
            fields[i] = new JTextField(10);
            centerPanel.add(labels[i], "1 " + (2 * i + 1));
            centerPanel.add(fields[i], "3 " + (2 * i + 1));
        }
        fields[0].setEditable(false);
        
        JButton chooseButton = new JButton("选择账户");
        chooseButton.addActionListener(e -> handleChoose());
        centerPanel.add(chooseButton, "5 1");
        
        frame.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void handleOk(){
    	if (! InputCheck.isAllNumber(fields[0].getText(), 0)) new InfoWindow("请选择账户");
    	else if (! InputCheck.isDouble(fields[1].getText())) new InfoWindow("请输入正确的金额");
    	else if (! InputCheck.isLegalOrBlank(fields[2].getText())) new InfoWindow("备注非法");
    	else {
    		rowData = new String[fields.length];
            for(int i = 0; i < fields.length; i++){
                rowData[i] = fields[i].getText();
            }
            frame.dispose();
    	}
    }

    private void handleChoose(){
        AccountVO a = new AccountChooseWin().getAccount();
        if(a == null) return;
        fields[0].setText(a.getNumber());
    }
}
