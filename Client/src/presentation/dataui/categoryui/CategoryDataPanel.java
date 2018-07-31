package presentation.dataui.categoryui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import blservice.CategoryBLService;
import blservice.infoservice.GetCommodityInterface;
import businesslogic.CommodityBL;
import layout.TableLayout;
import presentation.PanelInterface;
import presentation.component.InfoWindow;
import presentation.component.TopButtonPanel;
import vo.CategoryVO;

public class CategoryDataPanel implements PanelInterface{
    
    private JPanel panel;
    private JTree tree;

    public CategoryDataPanel(CategoryBLService categoryBl, ActionListener closeListener) {
    	GetCommodityInterface commodityBL = new CommodityBL();
    	
        tree = new JTree(categoryBl.getModel());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);//设置单选
        tree.addMouseListener(viewCommodities(commodityBL));
        double[][] size = {{TableLayout.FILL}, {TableLayout.PREFERRED, TableLayout.FILL}};
        panel = new JPanel(new TableLayout(size));
        
        TopButtonPanel buttonPanel = new TopButtonPanel();
        buttonPanel.addButton("增加", new ImageIcon("resource/AddData.png"), new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	if (tree.getSelectionCount() == 1) {
            		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            		CategoryVO category = (CategoryVO)node.getUserObject();
            		if (commodityBL.hasCommodity(category.getId())) {
            			new InfoWindow("请选择一个空节点作为父节点");
            		}
            		else {
                        new AddCategoryWindow(categoryBl, category);
                        tree.setModel(categoryBl.getModel());
            		}
            	} else {
            		new InfoWindow("请选择一个节点作为父节点");
            	}
            }
        });
		buttonPanel.addButton("修改", new ImageIcon("resource/ChangeData.png"), new ActionListener(){
		    @Override
		    public void actionPerformed(ActionEvent e){
		    	if (tree.getSelectionCount() == 1) {
			        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
			        if (node.isRoot()) new InfoWindow("不能修改父节点");
			        else {
				        new UpdateCategoryWindow(categoryBl, (CategoryVO)node.getUserObject());
				        tree.setModel(categoryBl.getModel());
			        }
		    	} else {
		    		new InfoWindow("请选择一个需要修改的节点");
		    	}
		    }
		});
		buttonPanel.addButton("删除", new ImageIcon("resource/DeleteData.png"), new ActionListener(){
		    @Override
		    public void actionPerformed(ActionEvent e){
		        if(tree.isSelectionEmpty()) {new InfoWindow("请选择需要删除的节点"); return;}
				int response = JOptionPane.showConfirmDialog(null, "确认要删除此条信息？", "提示", JOptionPane.YES_NO_OPTION);
				if(response == 0){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                    if (node.isRoot()) new InfoWindow("不能删除父节点");
                    else {
                    	CategoryVO category = (CategoryVO)node.getUserObject();
                    	if (categoryBl.hasContent(category.getId()) || commodityBL.hasCommodity(category.getId())) {
                			new InfoWindow("不能删除非空节点");
                		} else {
                			if(categoryBl.delete(category.getId())){
        				        JOptionPane.showMessageDialog(null, "信息已成功删除", "系统", JOptionPane.INFORMATION_MESSAGE); 
        				        tree.setModel(categoryBl.getModel());
        				    }
                		}
                    }
				}
		    }
		});
		buttonPanel.addButton("关闭", new ImageIcon("resource/Close.png"), closeListener);
	
		panel.add(buttonPanel.getPanel(), "0 0");
		panel.add(tree, "0 1");
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    private MouseListener viewCommodities(GetCommodityInterface commodityInfo){
        return new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() < 2)return;
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if(model.isLeaf(node)){
                    CategoryVO category = (CategoryVO)node.getUserObject();
                    new ViewCommodityWin(commodityInfo, category);
                }
            }

        };
    }

}
