package presentation.dataui.categoryui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.infoservice.GetCommodityInterface;
import layout.TableLayout;
import presentation.component.IdNamePanel;
import vo.CategoryVO;
import vo.MyTableModel;

/**
 * 查看一个分类下的所有商品的弹窗，在商品分类界面下双击某个叶节点分类后用于显示该分类下的所有商品。
 * @author 恽叶霄
 */
public class ViewCommodityWin {
    
    private JDialog frame;
    
    public ViewCommodityWin(GetCommodityInterface commodityInfo, CategoryVO category){
        frame = new JDialog();
        frame.setModal(true);
        double[][] size = {{-1.0}, {-2.0, -1.0}};
        frame.setLayout(new TableLayout(size));
        initCategoryInfoPanel(category);
        initCommodityPanel(commodityInfo, category.getId());
        frame.setLocation(200, 100);
        frame.setTitle("分类\"" + category.getName() + "\"下的商品");
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }
    
    private void initCategoryInfoPanel(CategoryVO category){
        IdNamePanel categoryPanel = new IdNamePanel("商品分类", 7, 7);
        categoryPanel.getIdField().setText(category.getId());
        categoryPanel.getIdField().setEditable(false);
        categoryPanel.getNameField().setText(category.getName());
        categoryPanel.getNameField().setEditable(false);
        
        double[][] size = {{20.0, -2.0, -1.0}, {10.0, -2.0, 10.0}};
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(categoryPanel, "1 1");
        frame.add(panel, "0 0");
    }
    
    private void initCommodityPanel(GetCommodityInterface commodityInfo, String categoryId){
        MyTableModel model = commodityInfo.getCategoryCommodities(categoryId);
        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane sp = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(sp, BorderLayout.CENTER);
        frame.add(panel, "0 1");
    }

}
