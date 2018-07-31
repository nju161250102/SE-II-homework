package presentation.billui;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CommodityChooseWin;
import presentation.tools.DoubleField;
import presentation.tools.InputCheck;
import presentation.tools.IntField;
import vo.CommodityVO;

/**
 * 输入商品的面板
 * @author 恽叶霄
 */
class InputCommodityInfoWin {
    
    private JTextField[] fields; 
    private JDialog frame;
    private String[] rowData = null;
    private String[] labelTexts = {"编号", "名称", "型号", "库存"
                , "单价", "数量", "总价", "备注"};

    public InputCommodityInfoWin() {
        frame = new JDialog();
        frame.setTitle("填写交易信息");
        frame.setModal(true);
        frame.setSize(400, 430);
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
        JPanel centerPanel = new JPanel(new TableLayout(new double[][]{columnSize, rowSize}));

        JLabel[] labels = new JLabel[labelTexts.length];
        fields = new JTextField[labelTexts.length];
        for(int i = 0; i < labelTexts.length; i++){
            labels[i] = new JLabel(labelTexts[i]);
            if(i == 5){
                fields[i] = new IntField(10);
            } else if(i == 4){
                fields[i] = new DoubleField(10);
            } else {
                fields[i] = new JTextField(10);
            }
            fields[i].setEditable(false);
            centerPanel.add(labels[i], "1 " + (2 * i + 1));
            centerPanel.add(fields[i], "3 " + (2 * i + 1));
        }
        FocusListener l = new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e) {
                sumUp();
            }
        };
        fields[4].setEditable(true);
        fields[4].addFocusListener(l);
        fields[5].setEditable(true);
        fields[7].setEditable(true);
        fields[5].addFocusListener(l);
        JButton chooseButton = new JButton("选择商品");
        chooseButton.addActionListener(e -> handleChoose());
        centerPanel.add(chooseButton, "5 1");
        
        frame.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void handleOk(){
        if (fields[0].getText().length() == 0) new InfoWindow("请选择商品");
        else if (Double.parseDouble(fields[4].getText()) == 0) new InfoWindow("请输入大于0的商品单价");
        else if (Integer.parseInt(fields[5].getText()) == 0) new InfoWindow("请输入大于0的数量");
        else if (! InputCheck.isLegalOrBlank(fields[7].getText())) new InfoWindow("备注非法");
        else if(fields[6].getText().length() == 0){
            if(!sumUp())return;
        } else {
	        rowData = new String[fields.length];
	        for(int i = 0; i < fields.length; i++){
	            rowData[i] = fields[i].getText();
	        }
	        frame.dispose();
        }
    }

    private void handleChoose(){
        CommodityVO c = new CommodityChooseWin().getCommodity();
        if(c == null) return;
        fields[0].setText(c.getId());
        fields[1].setText(c.getName());
        fields[2].setText(c.getType());
        fields[3].setText(c.getStore());
        if(fields[5].getText().length() > 0) sumUp();
    }

    private boolean sumUp(){
        try{
            int num = Integer.parseInt(fields[5].getText());
            double price = Double.parseDouble(fields[4].getText());
            fields[6].setText(num * price + "");
            return true;
        }catch(NumberFormatException e){
            //e.printStackTrace();
            return false;
        }
    }

}
