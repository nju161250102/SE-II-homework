package presentation.promotionui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import blservice.PromotionBLService;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CommodityChooseWin;
import vo.CommodityVO;
import vo.GroupDiscountVO;
import vo.MyTableModel;
import vo.PromotionVO;


@SuppressWarnings("serial")
public class GroupDiscountPanel extends PromotionDetailPanel {
    
    private GroupTable groupTable;
    private DetailGroupDiscountPanel panel;

    public GroupDiscountPanel(PromotionBLService promotionAdder, ActionListener closeListener) {
        this(promotionAdder, closeListener, null);
    }
    
    public GroupDiscountPanel(PromotionBLService promotionAdder, ActionListener closeListener, GroupDiscountVO promotion){
        super(promotionAdder, closeListener, promotion);
    }

    @Override
    protected JPanel getCenterPanel(PromotionVO promotion) {
        JScrollPane sp = new JScrollPane(groupTable = new GroupTable());
        groupTable.register(sp);
        panel = new DetailGroupDiscountPanel(sp);
        if(promotion != null){
            GroupDiscountVO gd = (GroupDiscountVO) promotion;
            panel.getDiscountField().setValue(gd.getReduction());
            groupTable.setModel(gd.getGroup());
        }
        return panel;
    }

    @Override
    protected boolean addPromotionImpl() {
        String id = getId(),
               from = getFromDate(),
               to = getToDate();
        MyTableModel goods = (MyTableModel)groupTable.getModel();
        double discount = panel.getDiscountField().getValue();
        GroupDiscountVO promotion = new GroupDiscountVO(id, from, to, discount, goods);
        return promotionAdder.add(promotion);
    }

    @Override
    protected boolean isFinished() {
        boolean dateValid = super.isFinished();
        if(dateValid){
            double discount = panel.getDiscountField().getValue();
            if(groupTable.getRowCount() == 0){
                new InfoWindow("请至少选择一种商品。");
                return false;
            }
            if(discount == 0){
                new InfoWindow("请填写降价一栏。");
                return false;
            }
            return true;
        }
        return false;
    }
    
}

@SuppressWarnings("serial")
class GroupTable extends JTable{
    
    private MouseListener chooseHandler;
    private JPopupMenu popup;
    
    public GroupTable(){
        super();
        super.getTableHeader().setReorderingAllowed(false);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        super.setModel(initModel());
        initChooseHandler();
        initPopup();
        super.addMouseListener(chooseHandler);
        super.setComponentPopupMenu(popup);
    }
    
    public void register(JScrollPane sp){
        sp.addMouseListener(chooseHandler);
        sp.setComponentPopupMenu(popup);
    }

    private void initChooseHandler(){
        chooseHandler = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() >= 2){
                    int index = GroupTable.this.getSelectedRow();
                    if(index < 0){
                        addCommodity();
                    }
                }
            }
        };
    }
    
    private void initPopup(){
        popup = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("添加商品");
        addItem.addActionListener(e->addCommodity());
        JMenuItem delItem = new JMenuItem("删除商品");
        delItem.addActionListener(e->delCommodity(this.getSelectedRow()));
        popup.add(addItem);
        popup.add(delItem);
    }
    
    private void delCommodity(int row){
        if(row < 0)return;
        MyTableModel model = (MyTableModel)this.getModel();
        model.removeRow(row);
    }
    
    private void addCommodity(){
        String[] data = new CommodityInputWin().getData();
        if(data == null) return;
        int index = findId(data[0]);
        MyTableModel model = (MyTableModel)this.getModel();
        if(index < 0) model.addRow(data);
        else {
            new InfoWindow("该组合中已存在该商品。");
        }
    }
    
    private int findId(String id){
        int rowCount = this.getRowCount();
        for(int i = 0; i < rowCount; i++){
            if(this.getValueAt(i, 0).equals(id)){
                return i;
            }
        }
        return -1;
    }
    
    private MyTableModel initModel(){
        String[] columnNames = {"商品编号", "名称", "型号"};
        String[][] data = new String[0][columnNames.length];
        return new MyTableModel(data, columnNames);
    }

}

class CommodityInputWin{
    
    private JDialog frame;
    private String[] data;
    private JTextField[] fields;
    
    public CommodityInputWin(){
        super();
        frame = new JDialog();
        frame.setModal(true);
        frame.setSize(400, 300);
        frame.setLocation(300, 200);
        frame.setLayout(new BorderLayout());
        frame.add(initCenter(), BorderLayout.CENTER);
        frame.add(initSouth(), BorderLayout.SOUTH);
        frame.setTitle("商品信息");
        frame.setVisible(true);
    }
    
    public String[] getData(){
        return data;
    }
    
    private JPanel initCenter(){
        String[] labelTexts = {"商品编号", "名称", "型号"};

        double[][] size = {
                {-1.0, -2.0, 10.0, -1.0, -1.0},
                {-1.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0}
        };
        JPanel panel = new JPanel(new TableLayout(size));
        fields = new JTextField[labelTexts.length];
        for(int i = 0; i < fields.length; i++){
            panel.add(new JLabel(labelTexts[i]), "1 " + (i * 2 + 1));
            panel.add(fields[i] = new JTextField(10), "3 " + (i * 2 + 1));
            fields[i].addMouseListener(chooseCommodity());
            fields[i].setEditable(false);
        }
        return panel;
    }
    
    private JPanel initSouth(){
        JButton okButton = new JButton("确认");
        okButton.addActionListener(e->ok());
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e->cancel());

        double[][] size = {
                {-1.0, -2.0, 10.0, -2.0, 20.0}, 
                {10.0, -2.0, 10.0}
        };
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(okButton, "1 1");
        panel.add(cancelButton, "3 1");
        return panel;
    }
    
    private void ok(){
        data = new String[fields.length];
        for(int i = 0; i < fields.length; i++){
            data[i] = fields[i].getText();
        }
        if(data[0].length() == 0){
            data = null;
            new InfoWindow("请选择商品。");
            return;
        }
        frame.dispose();
    }
    
    private void cancel(){
        frame.dispose();
    }
    
    private MouseListener chooseCommodity(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                CommodityVO commodity = new CommodityChooseWin().getCommodity();
                if(commodity == null) return;
                fields[0].setText(commodity.getId());
                fields[1].setText(commodity.getName());
                fields[2].setText(commodity.getType());
            }
        };
    }

}
