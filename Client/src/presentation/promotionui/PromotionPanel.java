package presentation.promotionui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blservice.PromotionBLService;
import businesslogic.PromotionBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.TopButtonPanel;
import vo.MyTableModel;


public class PromotionPanel implements PanelInterface {
    
    private JPanel panel;
    private CenterPanel centerPanel;
    private PromotionBLService promotionBl;

    public PromotionPanel(ActionListener closeListener) {
        this.promotionBl = new PromotionBL();
        double[][] size = {{-1.0}, {-2.0, -1.0} };
        panel = new JPanel(new TableLayout(size));
        panel.add(getNorthPanel(closeListener), "0 0");
    }

    @Override
    public boolean close() {
        if(centerPanel == null || centerPanel.close()) {
            return true;
        }
        return false;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    private JPanel getNorthPanel(ActionListener closeListener){
        TopButtonPanel topPanel = new TopButtonPanel();
        // TODO icons to be added
        topPanel.addButton("等级策略", new ImageIcon("resource/PromotionA.png"), e -> addRankPromotion());
        topPanel.addButton("商品组合降价", new ImageIcon("resource/PromotionB.png"), e -> addGroupDiscount());
        topPanel.addButton("总额策略", new ImageIcon("resource/PromotionC.png"), e -> addSumPromotion());
        topPanel.addButton("搜索", new ImageIcon("resource/SearchData.png"), e -> search());
        topPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
        return topPanel.getPanel();
    }
    
    private void addPromotion(Runnable changeCenter){
        if(centerPanel == null || centerPanel.close()){
            panel.setVisible(false);
            if(centerPanel != null) panel.remove(centerPanel);
            changeCenter.run();
            panel.add(centerPanel, "0 1");
            panel.setVisible(true);
        }
    }

    private void addRankPromotion(){
        addPromotion(()->centerPanel = new RankPromotionPanel(promotionBl, e->removeCenter()));
    }
    
    private void addGroupDiscount(){
        addPromotion(()->centerPanel = new GroupDiscountPanel(promotionBl, e->removeCenter()));
    }

    private void addSumPromotion(){
        addPromotion(()->centerPanel = new SumPromotionPanel(promotionBl, e->removeCenter()));
    }
    
    private void removeCenter(){
        panel.setVisible(false);
        panel.remove(centerPanel);
        panel.setVisible(true);
    }
    
    private void search(){
        if(centerPanel == null || centerPanel.close()){
            panel.setVisible(false);
            if(centerPanel != null) panel.remove(centerPanel);
            panel.setVisible(true);

            MyTableModel model = new PromotionSearchWin(promotionBl).getResult();
            if(model == null) return;
            panel.setVisible(false);
            centerPanel = new SearchResultPanel(model, promotionBl);
            panel.add(centerPanel, "0 1");
            panel.setVisible(true);
        }
    }

}
