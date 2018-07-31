package presentation.dataui.commodityui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import blservice.CategoryBLService;
import businesslogic.CategoryBL;
import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CategoryChooseWin;
import presentation.tools.InputCheck;
import vo.CategoryVO;
import vo.CommodityVO;

@SuppressWarnings("serial")
class InputCommodityPanel extends JPanel{
    
    private JTextField[] textFields;
    private CategoryBLService categoryBl;

    public InputCommodityPanel(String[] data) {
        super();
        this.categoryBl = new CategoryBL();
        double[] rows = new double[25];
        rows[0] = TableLayout.FILL;
        for(int i = 0; i < 12; i++){
            rows[2 * i + 1] = TableLayout.PREFERRED;
            rows[2 * i + 2] = 10.0;
        }
        rows[rows.length - 1] = TableLayout.FILL;
        double[][] size = {{TableLayout.FILL, TableLayout.PREFERRED, 10.0, 0.5, TableLayout.FILL}, rows};
        this.setLayout(new TableLayout(size));
        
        String[] labelTexts = {"商品编号", "商品名称", "商品型号", "商品所属库存", "商品数量", "商品警戒值"
                , "商品所属分类编号", "商品所属分类名称", "商品进价", "商品售价"};
        JLabel[] labels = new JLabel[labelTexts.length];
        for(int i = 0; i < labels.length; i++){
            labels[i] = new JLabel(labelTexts[i]);
            this.add(labels[i], "1 " + (2 * i + 1));
        }
        
        textFields = new JTextField[labels.length];
        for(int i = 0; i < textFields.length; i++){
            textFields[i] = new JTextField();
            if(data.length > i && data[i] != null)
                textFields[i].setText(data[i]);
            this.add(textFields[i], "3 " + (2 * i + 1));
        }
        //textFields[6].setText("单击此处选择商品分类");
        if ("".equals(textFields[6].getText())) {
        	textFields[6].setText("单击此处选择商品分类");
        }
        textFields[0].setEditable(false);
        textFields[3].setEditable(false);
        textFields[6].setEditable(false);
        textFields[7].setEditable(false);
        
        if(data[4] != null){
            textFields[4].setEditable(false);
            textFields[8].setEditable(false);
            textFields[9].setEditable(false);
        }
        
        textFields[6].addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		CategoryVO category = new CategoryChooseWin().getCategory();
        		if (category != null) {
            		textFields[6].setText(category.getId());
            		textFields[7].setText(category.getName());
        		}
        	}
        });
   }

    public CommodityVO getCommodityVO(){
    	if (! InputCheck.isLegal(textFields[1].getText())) new InfoWindow("商品名称非法");
    	else if (! InputCheck.isLegal(textFields[2].getText())) new InfoWindow("商品类型非法");
    	else if (! InputCheck.isAllNumber(textFields[4].getText(), 0)) new InfoWindow("请输入正确的商品数量");
    	else if (! InputCheck.isAllNumber(textFields[5].getText(), 0)) new InfoWindow("请输入正确的商品警戒值");
    	else if(categoryBl.hasContent(textFields[6].getText())) new InfoWindow("请选择只包含商品的分类");
    	else if (! InputCheck.isLegal(textFields[7].getText())) new InfoWindow("请选择商品分类");
    	else if (! InputCheck.isDouble(textFields[8].getText())) new InfoWindow("请输入正确的商品进货价格");
    	else if (! InputCheck.isDouble(textFields[9].getText())) new InfoWindow("请输入正确的商品销售价格");
    	else {
    		String id = textFields[0].getText(),
	               name = textFields[1].getText(),
	               type = textFields[2].getText(),
	               store = textFields[3].getText(),
	               categoryId = textFields[6].getText();
	        long amount = Long.parseLong(textFields[4].getText()),
	             alarm = Long.parseLong(textFields[5].getText());
	        double inPrice = Double.parseDouble(textFields[8].getText()),
	               salePrice = Double.parseDouble(textFields[9].getText());
	        return new CommodityVO(id, name, type, store, categoryId, amount, alarm, inPrice, salePrice, inPrice, salePrice);
    	}
        return null;
    }

}
