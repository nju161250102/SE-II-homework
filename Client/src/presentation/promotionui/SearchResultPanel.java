package presentation.promotionui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.PromotionBLService;
import vo.MyTableModel;
import vo.PromotionVO;

@SuppressWarnings("serial")
public class SearchResultPanel extends CenterPanel {
    
    private JTable table;
    private MouseListener viewDetailHandler;
    private PromotionBLService promotionBl;
    private JMenuItem viewItem, delItem, editItem;
    private JPopupMenu popup;
    
    public SearchResultPanel(MyTableModel model, PromotionBLService promotionBl){
        super();
        this.promotionBl = promotionBl;
        JScrollPane sp = new JScrollPane(table = new JTable(model));
        table.getTableHeader().setReorderingAllowed(false);
        initPopup();
        table.setComponentPopupMenu(popup);
        this.initViewDetailHandler();
        table.addMouseListener(viewDetailHandler);
        super.setLayout(new BorderLayout());
        super.add(sp, BorderLayout.CENTER);
    }
    
    private void initViewDetailHandler(){
        viewDetailHandler = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(table.getSelectedRow() >= 0){
                    viewItem.setEnabled(true);
                    delItem.setEnabled(true);
                    editItem.setEnabled(true);
                } else {
                    viewItem.setEnabled(false);
                    delItem.setEnabled(false);
                    editItem.setEnabled(false);
                }
                if(e.getClickCount() >= 2){
                    view();
                }
            }
        };
    }
    
    private void initPopup(){
        viewItem = new JMenuItem("≤Èø¥");
        viewItem.addActionListener(e->view());
        viewItem.setEnabled(false);
        delItem = new JMenuItem("…æ≥˝");
        delItem.addActionListener(e->del());
        delItem.setEnabled(false);
        editItem = new JMenuItem("–ﬁ∏ƒ");
        editItem.addActionListener(e->edit());
        editItem.setEnabled(false);
        popup = new JPopupMenu();
        popup.add(viewItem);
        popup.add(delItem);
        popup.add(editItem);
    }
    
    private void view(){
        PromotionVO promotion = getSelectedPromotion();
        if(promotion == null) return;
        new ViewPromotionDetailWin(promotion);
    }
    
    private void del(){
        int selected = table.getSelectedRow();
        System.out.println(selected);
        if(canDelAndEdit(selected)){
            String id = table.getValueAt(selected, 0).toString();
            if(promotionBl.delete(id)){
                JOptionPane.showMessageDialog(this, "…æ≥˝≥…π¶");
                MyTableModel model = (MyTableModel)table.getModel();
                model.removeRow(selected);
            } else {
                JOptionPane.showMessageDialog(this, "…æ≥˝ ß∞‹£¨«Î÷ÿ ‘°£");
            }
        }
    }
    
    private void edit(){
        int selected = table.getSelectedRow();
        if(canDelAndEdit(selected)){
            PromotionVO promotion = getSelectedPromotion();
            new EditPromotionWin(promotionBl, promotion);
            PromotionVO newPromotion = promotionBl.findById(promotion.getId());
            table.getModel().setValueAt(newPromotion.getFromDate(), selected, 1);
            table.getModel().setValueAt(newPromotion.getToDate(), selected, 2);
        }
    }
    
    private boolean canDelAndEdit(int selected){        
        if(selected < 0) return false;
        try{
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(table.getValueAt(selected, 1).toString());
            Date today = new Date();
            if(today.before(from)){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private PromotionVO getSelectedPromotion(){
        int selected = table.getSelectedRow();
        if(selected < 0) return null;
        String id = table.getValueAt(selected, 0).toString();
        return promotionBl.findById(id);
    }

}
