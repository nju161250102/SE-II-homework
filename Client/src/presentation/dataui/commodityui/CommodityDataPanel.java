package presentation.dataui.commodityui;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import blservice.CommodityBLService;
import presentation.component.InfoWindow;
import presentation.dataui.DataPanelInterface;
import vo.MyTableModel;

public class CommodityDataPanel extends JScrollPane implements DataPanelInterface{
	private JTable table = new JTable();
    private CommodityBLService commodityBl;

    public CommodityDataPanel(CommodityBLService commodityBl) {
        this.commodityBl = commodityBl;
        table.setModel(commodityBl.update());
		table.getTableHeader().setReorderingAllowed(false);
		this.setViewportView(table);
    }

    @Override
    public void addAction() {
    	new AddCommodityWindow(commodityBl);
        table.setModel(commodityBl.update());
    }

    @Override
    public void updateAction() {
    	MyTableModel model = (MyTableModel)table.getModel();
        if (table.getSelectedRow() != -1) {
        	new UpdateCommodityWindow(commodityBl, model.getValueAtRow(table.getSelectedRow()));
            table.setModel(commodityBl.update());
        }
        else new InfoWindow("请选择需要修改的商品");
    }

    @Override
    public void searchAction() {
    	MyTableModel model = new SearchCommodityWindow(commodityBl).getModel();
        if(model != null) table.setModel(model);
    }

	@Override
	public void deleteAction() {
		int index = table.getSelectedRow();
	    if(index < 0) {new InfoWindow("请选择需要删除的商品信息");return;}
		int response = JOptionPane.showConfirmDialog(null, "确认要删除此条商品信息？", "提示", JOptionPane.YES_NO_OPTION);
		if (response == 0) {
		    String id = (String)((MyTableModel)table.getModel()).getValueAt(index, 0);
			if (commodityBl.delete(id)) new InfoWindow("选中的商品信息已成功删除");
			table.setModel(commodityBl.update());
		}
	}

}
