package presentation.dataui.commodityui;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import blservice.CommodityBLService;
import presentation.dataui.SearchWindow;

public class SearchCommodityWindow extends SearchWindow{
    
    public SearchCommodityWindow(CommodityBLService commodityBl) {
        super(commodityBl);
        frame.setTitle("查询商品");
        frame.setSize(600, 200);
        frame.setVisible(true);
    }

    @Override
    protected ButtonGroup initTypeGroup() {
        JRadioButton idRadioButton = new JRadioButton("按编号搜索"),
                     nameRadioButton = new JRadioButton("按名称搜索"),
                     categoryIdRadioButton = new JRadioButton("按所属分类编号搜索"),
                     categoryNameRadioButton = new JRadioButton("按所属分类名称搜索");
        idRadioButton.setSelected(true);
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(idRadioButton);
        typeGroup.add(nameRadioButton);
        typeGroup.add(categoryIdRadioButton);
        typeGroup.add(categoryNameRadioButton);
        return typeGroup;
    }
    
}
