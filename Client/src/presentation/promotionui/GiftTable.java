package presentation.promotionui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import presentation.component.InfoWindow;
import vo.MyTableModel;


@SuppressWarnings("serial")
public class GiftTable extends JTable {
    
    private MouseListener giftEventHandler;
    private JPopupMenu popup;

    public GiftTable(JScrollPane sp) {
        super();
        super.getTableHeader().setReorderingAllowed(false);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        super.setModel(initModel());
        initGiftEventHandler();
        initPopupMenu();
        super.addMouseListener(giftEventHandler);
        super.setComponentPopupMenu(popup);
        register(sp);
    }
    
    private void register(JScrollPane sp){
        sp.setViewportView(this);
        sp.addMouseListener(giftEventHandler);
        sp.setComponentPopupMenu(popup);
    }

    private void initGiftEventHandler(){
        giftEventHandler = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() >= 2){
                    int index = GiftTable.this.getSelectedRow();
                    if(index >= 0){
                        editGift(index);
                    } else {
                        addGift();
                    }
                }
            }
        };
    }
    
    private void initPopupMenu(){
        popup = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("添加商品");
        addItem.addActionListener(e->addGift());
        JMenuItem editItem = new JMenuItem("编辑商品");
        editItem.addActionListener(e->editGift(this.getSelectedRow()));
        JMenuItem delItem = new JMenuItem("删除商品");
        delItem.addActionListener(e->delGift(this.getSelectedRow()));
        popup.add(addItem);
        popup.add(editItem);
        popup.add(delItem);
    }
    
    private void delGift(int row){
        if(row < 0) return;
        MyTableModel model = (MyTableModel)this.getModel();
        model.removeRow(row);
    }
    
    private void editGift(int row){
        if(row < 0) return;
        MyTableModel model = (MyTableModel)this.getModel();
        String[] data = new GiftInputWin(model.getValueAtRow(row)).getData();
        if(data == null) return;
        for(int i = 0; i < data.length; i++){
            model.setValueAt(data[i], row, i);
        }
    }
    
    private void addGift(){
        if(this.getRowCount() >= 3){
            new InfoWindow("最多可以选择三个赠品。");
            return;
        }
        String[] data = new GiftInputWin().getData();
        if(data == null) return;
        int index = findId(data[0]);
        MyTableModel model = (MyTableModel)this.getModel();
        if(index < 0) model.addRow(data);
        else {
            int oldNum = Integer.parseInt(model.getValueAt(index, 4).toString());
            int newNum = oldNum + Integer.parseInt(data[4]);
            model.setValueAt(newNum + "", index, 4);
        }
    }
    
    private int findId(String id){
        int rowCount = this.getRowCount();
        for(int i = 0; i < rowCount; i++){
            if(this.getValueAt(i, 0).equals(id))
                return i;
        }
        return -1;
    }

    private MyTableModel initModel(){
        String[] columnNames = {"商品编号", "名称", "型号", "单价", "数量"};
        String[][] data = new String[0][5];
        return new MyTableModel(data, columnNames);
    }
    
}
