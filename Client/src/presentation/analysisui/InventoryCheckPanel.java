package presentation.analysisui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import businesslogic.CommodityBL;
import presentation.PanelInterface;
import presentation.component.TopButtonPanel;
import presentation.tools.ExcelExporter;
import vo.MyTableModel;


public class InventoryCheckPanel implements PanelInterface {
    
    private JPanel panel;
    private JTable table;

    public InventoryCheckPanel(ActionListener closeListener) {
        panel = new JPanel(new BorderLayout());
        TopButtonPanel northPanel = new TopButtonPanel();
        northPanel.addButton("导出为Excel表", new ImageIcon("resource/Export.png"), e -> export());
        panel.add(northPanel.getPanel(), BorderLayout.NORTH);
        northPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
        
        table = new JTable(new CommodityBL().update());
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane sp = new JScrollPane(table);
        panel.add(sp, BorderLayout.CENTER);
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    private void export(){
        String path = ExcelExporter.openFileChooser();
        if(path == null) return;
        if(ExcelExporter.export((MyTableModel)table.getModel(), path)){
            JOptionPane.showMessageDialog(null, "导出成功^_^");
        } else {
            JOptionPane.showMessageDialog(null, "导出失败，请重试@_@");
        }
    }

}
