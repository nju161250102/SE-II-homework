package presentation.component.choosewindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import blservice.CategoryBLService;
import businesslogic.CategoryBL;
import presentation.component.InfoWindow;
import vo.CategoryVO;

public class CategoryChooseWin{

	private JDialog frame = new JDialog();
	private JTree tree;
	private CategoryVO data = null;
	
	public CategoryChooseWin() {
		CategoryBLService categoryBL = new CategoryBL();
		
		frame.setModal(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width/2, screenSize.height/2);
		frame.setLocation(screenSize.width/4, screenSize.height/4);
		frame.setResizable(false);
		frame.setTitle("选择商品分类");
		
		tree = new JTree(categoryBL.getModel());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);//设置单选
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton yesButton = new JButton("确定");
		JButton quitButton = new JButton("取消");
		southPanel.add(yesButton);
		southPanel.add(quitButton);
		
		frame.setLayout(new BorderLayout());
		frame.add(tree, BorderLayout.CENTER);
		frame.add(southPanel, BorderLayout.SOUTH);
		
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tree.getSelectionCount() == 1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (node.isRoot()) {new InfoWindow("不能选择父节点");return;}
					CategoryVO category = (CategoryVO)node.getUserObject();
					data = category;
					frame.dispose();
				} else {
		    		new InfoWindow("请选择一个分类节点");
		    	}
			}
		});
		quitButton.addActionListener(e -> frame.dispose());
		frame.setVisible(true);
	}
	
	public CategoryVO getCategory() {
		return this.data;
	}
}
