package presentation.analysisui;

import java.awt.BorderLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import blservice.InventoryDynamicBLService;
import businesslogic.InventoryDynamicBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.DateChooser;
import presentation.component.InfoWindow;
import presentation.tools.TableTools;

public class InventoryDynamicPanel implements PanelInterface{
    
    private InventoryDynamicBLService inventoryDynamicBl;
    private JPanel panel;
    private JTextField fromDateField, toDateField;
    private JTable table;

    public InventoryDynamicPanel() {
        inventoryDynamicBl = new InventoryDynamicBL();
        panel = new JPanel(new BorderLayout());
        initNorth();
        initCenter();
    }
    
    private void initNorth(){
        JLabel fromDateLabel = new JLabel("开始日期");
        JLabel toDateLabel = new JLabel("结束日期");
        fromDateField = new JTextField(10);
        toDateField = new JTextField(10);
        DateChooser fromDateChooser = DateChooser.getInstance(),
                    toDateChooser = DateChooser.getInstance();
        fromDateChooser.register(fromDateField);
        toDateChooser.register(toDateField);
        JButton searchButton = new JButton("搜索", new ImageIcon("resource/SearchData.png"));
        searchButton.addActionListener(e -> search());
        
        double[][] size = {
                {20.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 20.0, -2.0, -1.0},
                {10.0, -2.0, -1.0}
        };
        JPanel northPanel = new JPanel(new TableLayout(size));
        northPanel.add(fromDateLabel, "1 1");
        northPanel.add(fromDateField, "3 1");
        northPanel.add(toDateLabel, "5 1");
        northPanel.add(toDateField, "7 1");
        northPanel.add(searchButton, "9 1");
        
        panel.add(northPanel, BorderLayout.NORTH);
    }
    
    private void initCenter(){
        table = new JTable(inventoryDynamicBl.getDefault());
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane sp = new JScrollPane(table);
        panel.add(sp, BorderLayout.CENTER);
    }
    
    private void search(){
        String from = fromDateField.getText(),
               to = toDateField.getText();
        if(from.length() == 0 || to.length() == 0){
            new InfoWindow("请输入正确的日期@_@");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date fromDate = sdf.parse(from), toDate = sdf.parse(to);
            if(fromDate.after(toDate)){
                new InfoWindow("请输入正确的日期@_@");
            } else {
                table.setModel(inventoryDynamicBl.getDynamic(from, to));
                TableTools.autoFit(table);
            }
        }catch(ParseException e){
            e.printStackTrace();
            new InfoWindow("请输入正确的日期@_@");
        }
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

}
