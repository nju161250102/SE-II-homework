package presentation.component.choosewindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import blservice.CommodityBLService;
import blservice.infoservice.GetCommodityInterface;
import businesslogic.CommodityBL;
import presentation.tools.TableTools;
import vo.CategoryVO;
import vo.CommodityVO;

public class CommodityChooseWin extends ChooseWindow {
	
	private CommodityVO data;
	private CommodityBLService commodityBL;

	public CommodityChooseWin() {
		super();
	}

	@Override
	public void init() {
		commodityBL = new CommodityBL();
		setTypes(new String[]{"按编号搜索", "按名称搜索", "按分类搜索"});
		table.setModel(commodityBL.update());
		TableTools.autoFit(table);
		
		frame.setTitle("选择商品");
		frame.setVisible(true);
	}

	@Override
	protected void yesAction() {
		GetCommodityInterface commodityInfo = new CommodityBL();
		if (table.getSelectedRow() != -1) {
			data = commodityInfo.getCommodity((String) table.getValueAt(table.getSelectedRow(), 0));
			frame.dispose();
		}
	}

	public CommodityVO getCommodity() {
		return data;
	}

	@Override
	protected void searchAction() {
		if ("".equals(keyField.getText())) table.setModel(commodityBL.update());
		else if ("按编号搜索".equals(searchTypeBox.getSelectedItem())) table.setModel(commodityBL.search("按编号搜索", keyField.getText()));
		else if ("按名称搜索".equals(searchTypeBox.getSelectedItem())) table.setModel(commodityBL.search("按名称搜索", keyField.getText()));
		else if ("按分类搜索".equals(searchTypeBox.getSelectedItem())) table.setModel(commodityBL.search("按分类搜索", keyField.getText()));
		TableTools.autoFit(table);
	}
}
